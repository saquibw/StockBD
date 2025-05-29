package com.stockbd.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.stockbd.model.Instrument;
import com.stockbd.model.InstrumentPrice;

import lombok.extern.slf4j.Slf4j;
import static com.stockbd.utils.ConversionUtils.parseDouble;
import static com.stockbd.utils.ConversionUtils.parseBigDecimal;
import static com.stockbd.utils.ConversionUtils.parseLong;

@Service
@Slf4j
public class InstrumentListParserService {
	private DocumentFetchService documentFetchService;
	private InstrumentService instrumentService;
	private InstrumentPriceService instrumentPriceService;

	public InstrumentListParserService(
			DocumentFetchService documentFetchService, 
			InstrumentService instrumentService, 
			InstrumentPriceService instrumentPriceService) {
		this.documentFetchService = documentFetchService;
		this.instrumentService = instrumentService;
		this.instrumentPriceService = instrumentPriceService;
	}

	
	public void getInstrumentList() {
		//if (!shallProceed("")) return;
		Document doc = documentFetchService.getDailyInstrumentList();
		Element body = doc.getElementById("RightBody");
		String currentDateTimeText = body.select(".topBodyHead").text();
		LocalDate latestDate = getCurrentDateFromString(currentDateTimeText);

		List<Instrument> existingInstruments = instrumentService.getAll();
		Map<String, Instrument> existingInstrumentsMap = existingInstruments.stream().collect(Collectors.toMap(Instrument::getCode, instrument -> instrument, (existing, replacement) -> existing));

		Elements instrumentElementList = body.select(".shares-table tbody");
		for (Element instrumentElement : instrumentElementList) {
			Elements row = instrumentElement.select("tr td");
			List<String> columns = row.eachText();
			String instrumentCode = columns.get(1);
			Instrument instrument = null;
			
			if (StringUtils.hasText(instrumentCode)) {
				
				if (!existingInstrumentsMap.containsKey(instrumentCode)) {
					log.info("Instrument {} not found in DB. Creating...", instrumentCode);
					
					instrument = new Instrument();
					instrument.setCode(instrumentCode);
					instrumentService.save(instrument);
					
					log.info("Instrument {} created successfully", instrumentCode);
				} else {
					instrument = existingInstrumentsMap.get(instrumentCode);
					
					log.info("Instrument {} found in DB.", instrument);
				}				
			}
			if (shallProceed(latestDate)) {
				saveDailyPrice(instrument, columns, latestDate);
			}
		}		
	}

	public void saveDailyPrice(Instrument instrument, List<String> columns, LocalDate date) {
		InstrumentPrice price = new InstrumentPrice();
		price.setLastTradePrice(parseDouble(columns.get(2)));
		price.setHighPrice(parseDouble(columns.get(3)));	
		price.setLowPrice(parseDouble(columns.get(4)));
		price.setClosePrice(parseDouble(columns.get(5)));
		price.setYesterdayClosePrice(parseDouble(columns.get(6)));
		price.setTradeCount(parseLong(columns.get(8)));
		
		BigDecimal tradeValue = parseBigDecimal(columns.get(9)).multiply(BigDecimal.valueOf(1_000_000));
		price.setTradeValue(tradeValue.doubleValue());
		
		price.setTradeVolume(parseLong(columns.get(10)));
		price.setDate(date);
		price.setInstrument(instrument);
		
		instrumentPriceService.save(price);
	}
	
	private LocalDate getCurrentDateFromString(String currentDateTimeText) {
		//currentDateTimeText = "Latest Share Price On Jul 30, 2024 at 2:20 PM";
		currentDateTimeText = currentDateTimeText.split("On")[1].split("at")[0].strip();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
		return LocalDate.parse(currentDateTimeText, formatter);
	}

	private boolean shallProceed(LocalDate lastUpdatedAt) {
		boolean shallProceed = false;
		
		InstrumentPrice latestPrice = instrumentPriceService.findLatest();
		
		if (ObjectUtils.isEmpty(latestPrice)) {
			log.info("No latest price information found. Shall proceed with price saving mechanism.");
			shallProceed = true;
		} else if(latestPrice.getDate().equals(lastUpdatedAt)) {
			log.info("Last updated price date in DB is same as last updated date in DSE. Should not proceed");
			shallProceed = false;
		} else {
			log.info("Shall proceed with the price saving mechanism");
			shallProceed = false;
		}
		return shallProceed;
	}

}

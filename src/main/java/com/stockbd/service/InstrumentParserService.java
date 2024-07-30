package com.stockbd.service;

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

@Service
@Slf4j
public class InstrumentParserService {
	private DocumentFetchService documentFetchService;
	private InstrumentService instrumentService;
	private InstrumentPriceService instrumentPriceService;

	public InstrumentParserService(
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

		List<Instrument> existingInstruments = instrumentService.getAll();
		Map<String, Instrument> existingInstrumentsMap = existingInstruments.stream().collect(Collectors.toMap(Instrument::getCode, instrument -> instrument, (existing, replacement) -> existing));
		boolean shallProceed = shallProceed(currentDateTimeText);

		Elements instrumentElementList = body.select(".shares-table tbody");
		for (Element instrumentElement : instrumentElementList) {
			Elements row = instrumentElement.select("tr td");
			List<String> columns = row.eachText();
			String instrumentCode = columns.get(1);
			Instrument instrument = null;
			if (StringUtils.hasText(instrumentCode)) {
				if (!existingInstrumentsMap.containsKey(instrumentCode)) {
					log.info("Instrument {} not found in DB. Creating...", instrumentCode);
					instrument = instrumentService.save(new Instrument(null, instrumentCode, "", ""));
					log.info("Instrument {} created successfully", instrumentCode);
				} else {
					instrument = existingInstrumentsMap.get(instrumentCode);
					log.info("Instrument {} found in DB.", instrument);
				}				
			}
			if (shallProceed) {
				saveDailyPrice(instrument, columns);
			}
		}		
	}

	public void saveDailyPrice(Instrument instrument, List<String> columns) {
		InstrumentPrice price = new InstrumentPrice();
		price.setLastTradePrice(parseDouble(columns.get(2)));
		price.setHighPrice(parseDouble(columns.get(3)));	
		price.setLowPrice(parseDouble(columns.get(4)));
		price.setClosePrice(parseDouble(columns.get(5)));
		price.setYesterdayClosePrice(parseDouble(columns.get(6)));
		price.setTradeCount(parseInteger(columns.get(8)));
		price.setTradeValue(parseDouble(columns.get(9)) * 1000000);
		price.setTradeVolume(parseInteger(columns.get(10)));
		price.setDate(LocalDate.now());
		
		instrumentPriceService.save(price);
	}
	
	private double parseDouble(String price) {
		if (StringUtils.hasText(price)) {
			return Double.parseDouble(price);
		}
		return 0;
	}
	
	private int parseInteger(String price) {
		if (StringUtils.hasText(price)) {
			return Integer.parseInt(price);
		}
		return 0;
	}

	private boolean shallProceed(String currentDateTimeText) {
		currentDateTimeText = "Latest Share Price On Jul 30, 2024 at 2:20 PM";
		currentDateTimeText = currentDateTimeText.split("On")[1].split("at")[0].strip();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
		LocalDate lastUpdatedAt = LocalDate.parse(currentDateTimeText, formatter);
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

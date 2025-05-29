package com.stockbd.service;

import java.math.BigDecimal;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stockbd.model.Instrument;

import lombok.extern.slf4j.Slf4j;
import static com.stockbd.utils.ConversionUtils.parseBigDecimal;
import static com.stockbd.utils.ConversionUtils.parseLong;

@Service
@Slf4j
public class InstrumentInfoParserService {
	private DocumentFetchService documentFetchService;
	private InstrumentService instrumentService;
	
	public InstrumentInfoParserService(
			DocumentFetchService documentFetchService, 
			InstrumentService instrumentService) {
		this.documentFetchService = documentFetchService;
		this.instrumentService = instrumentService;
	}
	
	@Transactional
	public void getInstrumentDetails(String instrumentCode) {
		Document doc = documentFetchService.getInstrumentDetails(instrumentCode);
		Element body = doc.getElementById("RightBody");
		Elements instrumentInfo = body.select("#company");
		Instrument instrument = instrumentService.getBy(instrumentCode);
		
		parseBasics(instrumentInfo, instrument);
		parseCategory(instrumentInfo, instrument);
		
		instrumentService.save(instrument);
	}
	
	private void parseBasics(Elements instrumentInfo, Instrument instrument) {
		Elements instrumentbasicInfo = instrumentInfo.get(2).select("tbody");
		Element info = instrumentbasicInfo.getFirst();
		Elements row = info.select("tr td");
		List<String> columns = row.eachText();
		
		BigDecimal authCapital = parseBigDecimal(columns.get(0)).multiply(BigDecimal.valueOf(1_000_000));
		instrument.setAuthorizedCapital(authCapital.doubleValue());
		
		BigDecimal paidCapital = parseBigDecimal(columns.get(1)).multiply(BigDecimal.valueOf(1_000_000));
		instrument.setPaidupCapital(paidCapital.doubleValue());
		
		instrument.setNumberOfSecurities(parseLong(columns.get(5)));
		instrument.setSector(columns.get(6));		
	}
	
	private void parseCategory(Elements instrumentInfo, Instrument instrument) {
		Elements instrumentCategoryInfo = instrumentInfo.get(10).select("tbody");
		Element info = instrumentCategoryInfo.getFirst();
		Elements row = info.select("tr td");
		List<String> columns = row.eachText();
		
		instrument.setCategory(columns.get(3));
		log.info("{}", columns);
	}

}

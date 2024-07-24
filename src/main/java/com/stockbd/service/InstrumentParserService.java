package com.stockbd.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.stockbd.model.Instrument;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InstrumentParserService {
	private DocumentFetchService documentFetchService;
	private InstrumentService instrumentService;
	
	public InstrumentParserService(DocumentFetchService documentFetchService, InstrumentService instrumentService) {
		this.documentFetchService = documentFetchService;
		this.instrumentService = instrumentService;
	}
	
	public void getInstrumentList() {
		Document doc = documentFetchService.getDailyInstrumentList();
		Element body = doc.getElementById("RightBody");
		String currentTimeText = body.select(".topBodyHead").text();
		
		List<Instrument> existingInstruments = instrumentService.getAll();
		Map<String, Instrument> existingInstrumentsMap = existingInstruments.stream().collect(Collectors.toMap(Instrument::getCode, instrument -> instrument, (existing, replacement) -> existing));
		
		Elements instrumentElementList = body.select(".shares-table tbody");
		for (Element instrumentElement : instrumentElementList) {
			Elements row = instrumentElement.select("tr td");
			List<String> columns = row.eachText();
			String instrumentCode = columns.get(1);
			Instrument instrument;
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
		}
		
	}

}

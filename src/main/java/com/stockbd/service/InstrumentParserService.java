package com.stockbd.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
		
		Elements instrumentList = body.select(".shares-table tbody");
		for (Element instrument : instrumentList) {
			Elements row = instrument.select("tr td");
			List<String> columns = row.eachText();
			String instrumentCode = columns.get(1);
			if (StringUtils.hasText(instrumentCode)) {
				if (!existingInstrumentsMap.containsKey(instrumentCode)) {
					log.info("Instrument {} not found in DB. Creating...", instrumentCode);
					instrumentService.save(new Instrument(null, instrumentCode, "", ""));
				} else {
					log.info("Instrument {} found in DB.", existingInstrumentsMap.get(instrumentCode));
				}
				
			}
		}
		
	}

}

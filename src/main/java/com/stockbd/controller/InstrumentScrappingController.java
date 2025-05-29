package com.stockbd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.stockbd.service.InstrumentInfoParserService;
import com.stockbd.service.InstrumentListParserService;

@RestController
public class InstrumentScrappingController {
	private InstrumentListParserService listParserService;
	private InstrumentInfoParserService infoParserService;
	
	public InstrumentScrappingController(
			InstrumentListParserService listParserService, 
			InstrumentInfoParserService infoParserService) {
		this.listParserService = listParserService;
		this.infoParserService = infoParserService;
	}
	
	@GetMapping(value = "")
	public void get() {
		this.listParserService.getInstrumentList();
	}
	
	@GetMapping(value = "get/{instrument}")
	public void getOne(@PathVariable(name = "instrument") String instrument) {
		this.infoParserService.getInstrumentDetails(instrument);
	}

}

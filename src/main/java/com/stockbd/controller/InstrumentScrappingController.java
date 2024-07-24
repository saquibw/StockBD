package com.stockbd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockbd.service.InstrumentParserService;

@RestController
public class InstrumentScrappingController {
	private InstrumentParserService service;
	public InstrumentScrappingController(InstrumentParserService service) {
		this.service = service;
	}
	
	@GetMapping(value = "")
	public void get() {
		service.getInstrumentList();
	}

}

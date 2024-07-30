package com.stockbd.service;

import org.springframework.stereotype.Service;

import com.stockbd.model.InstrumentPrice;
import com.stockbd.repository.InstrumentPriceRepository;

@Service
public class InstrumentPriceService {
	private InstrumentPriceRepository repository;
	public InstrumentPriceService(InstrumentPriceRepository repository) {
		this.repository = repository;
	}

	public void save(InstrumentPrice price) {
		repository.save(price);
	}
	
	public InstrumentPrice findLatest() {
		return repository.findTopByOrderByDateDesc();
	}
}

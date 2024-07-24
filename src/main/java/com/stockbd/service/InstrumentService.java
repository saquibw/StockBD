package com.stockbd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stockbd.model.Instrument;
import com.stockbd.repository.InstrumentRepository;

@Service
public class InstrumentService {
	private InstrumentRepository repository;
	
	public InstrumentService(InstrumentRepository repository) {
		this.repository = repository;
	}
	
	public List<Instrument> getAll() {
		return repository.findAll();
	}
	
	public void save(Instrument instrument) {
		repository.save(instrument);
	}

}

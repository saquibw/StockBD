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
	
	public Instrument save(Instrument instrument) {
		return repository.save(instrument);
	}
	
	public void saveAll(List<Instrument> instrumentList) {
		repository.saveAllAndFlush(instrumentList);
	}

}

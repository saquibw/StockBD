package com.stockbd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockbd.model.Instrument;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
	Instrument getByCode(String code);
}

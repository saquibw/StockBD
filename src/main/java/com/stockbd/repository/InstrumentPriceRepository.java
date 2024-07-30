package com.stockbd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockbd.model.InstrumentPrice;

public interface InstrumentPriceRepository extends JpaRepository<InstrumentPrice, Long> {
	InstrumentPrice findTopByOrderByDateDesc();
}

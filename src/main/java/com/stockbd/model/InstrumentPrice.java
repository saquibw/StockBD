package com.stockbd.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentPrice {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private double lastTradePrice;
	private double highPrice;
	private double lowPrice;
	private double closePrice;
	private double yesterdayClosePrice;
	private int tradeCount;
	private double tradeValue;
	private int tradeVolume;
	private LocalDate date;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

}

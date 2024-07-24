package com.stockbd.model;

import jakarta.persistence.Entity;
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
	private int tradeCount;
	private long tradeVolume;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

}

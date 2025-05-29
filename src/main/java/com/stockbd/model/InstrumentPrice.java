package com.stockbd.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(indexes = { @Index(name = "idx_instrument_date", columnList = "instrument_id, date") })
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentPrice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double lastTradePrice;
	private Double highPrice;
	private Double lowPrice;
	private Double closePrice;
	private Double yesterdayClosePrice;
	private Long tradeCount;
	private Double tradeValue;
	private Long tradeVolume;
	private LocalDate date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instrument_id", nullable = false)
	private Instrument instrument;
}

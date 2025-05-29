package com.stockbd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(indexes = { @Index(name = "idx_instrument_code", columnList = "code"),
		@Index(name = "idx_instrument_category", columnList = "category"),
		@Index(name = "idx_instrument_sector", columnList = "sector") })
@AllArgsConstructor
@NoArgsConstructor
public class Instrument {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String code;
	private String name;
	private String category;
	private Double authorizedCapital;
	private Double paidupCapital;
	private Long numberOfSecurities;
	private String sector;
}

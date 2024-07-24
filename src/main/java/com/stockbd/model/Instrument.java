package com.stockbd.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instrument {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String code;
	private String name;
	private String category;
	
	@OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InstrumentPrice> price = new ArrayList<>();
}

package com.stockbd.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class InstrumentParserService {
	
	public void getDailyInstrumentList() {
		Document doc;
		try {
			doc = Jsoup.connect("https://www.dsebd.org/latest_share_price_scroll_l.php").get();
			System.out.println(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

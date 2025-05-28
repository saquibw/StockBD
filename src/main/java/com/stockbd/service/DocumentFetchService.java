package com.stockbd.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentFetchService {
	public Document getDailyInstrumentList() {
		Document doc = null;
		try {
			log.info("Instruments fetch process initiated");
			doc = Jsoup.connect("https://www.dsebd.org/latest_share_price_scroll_l.php").get();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
	
	private void writeToFile(Document doc) throws IOException {
		System.out.println(doc);
		File file = new File("/home/saquibul.waheed@nad.neura-robotics.com/Saquib/bodyTable.txt");
		FileWriter writer = new FileWriter(file);
	    writer.write(doc.body().select("#RightBody table").html());
	    writer.close();
	}

}

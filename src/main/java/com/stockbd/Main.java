package com.stockbd;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {

	public static void main(String[] args) {
		Document doc;
		try {
			doc = Jsoup.connect("https://www.dsebd.org/displayCompany.php?name=AAMRANET").get();
			System.out.println(doc.select("#section-to-print"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

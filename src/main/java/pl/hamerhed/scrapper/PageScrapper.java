package pl.hamerhed.scrapper;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PageScrapper {
	private Document doc;
	
	public PageScrapper(){}
	
	public void parse(String url) throws IOException{
		int count = 0;
		boolean flag = true;
		while(count < 3 && flag){
			try {
				flag = false;
				doc = Jsoup.connect(url).get();
			} catch(SocketTimeoutException e){
				flag = true;
				count++;
			}
		}
	}
	
	public Document getDocument(){
		return doc;
	}
}

package pl.hamerhed.scrapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
class DocumentFromUrlExtractor {
	private static final Logger clog = Logger.getLogger(DocumentFromUrlExtractor.class);
	
	/**
	 * gets content of url.
	 * If there is time out errors increase timeout variable in connect
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public Document getDocumentFromLink(String url) throws IOException {
		int count = 0;
		boolean flag = true;
		Document doc = null;
		org.jsoup.Connection.Response response = null;
		while(count < 3 && flag){
			try {
				flag = false;
				
				response = Jsoup.connect(url)
				           .ignoreContentType(true)
				           .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")  
				           .timeout(60*1000) 
				           .followRedirects(true)
				           .execute();
				doc = response.parse();
				
			} catch(SocketTimeoutException e){
				clog.error("Błąd tworzenie dokumentu z linka " + url + " : " + e.getLocalizedMessage());
				e.printStackTrace();
				flag = true;
				count++;
				
				try {
					Thread.sleep(5000);
				} catch(InterruptedException ex){
				}
			}
		}
		return doc;
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException{
		System.out.println("start");
		Document doc = Jsoup.parse(new URL("https://www.jobs.pl/czy-mowi-pani-pan-po-polsku-i-niemiecku-program-szkoleniowy-i-poczatkowe-stanowisko-oferta-1191766"), 60000);
		System.out.println("doc=" + doc);
		System.out.println("end");
		
	}
}

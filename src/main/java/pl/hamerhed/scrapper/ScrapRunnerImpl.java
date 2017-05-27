package pl.hamerhed.scrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.hamerhed.datasource.AdvertismentsDataSource;
import pl.hamerhed.domain.Advertisment;

@Service
class ScrapRunnerImpl implements ScrapRunner {
	private Collection<Advertisment> advertisments = Collections.emptyList();
	
	@Autowired
	private AdvertismentsDataSource<String> dataSource;

	private PageScrapper scrapper = new PageScrapper();
	
	protected ScrapRunnerImpl(){}
	
	public ScrapRunnerImpl(AdvertismentsDataSource<String> dataSource){
		this.dataSource = dataSource;
	}
	
	public void parse() throws IOException {
		Iterator<String> iter = dataSource.iterator();
		while(iter.hasNext()){
			scrapper.parse(iter.next());
			
		    //Element elem = scrapper.getRoot().getElementById("ogloszenia");
			//System.out.println(elem);
	
		    RealEstatesListExtractorImpl estatesExtractor = new RealEstatesListExtractorImpl(scrapper.getDocument());
		    estatesExtractor.parse();
		    
		    advertisments.addAll(estatesExtractor.getEstates());
		}
		
		for(Iterator<Advertisment> iter2 = advertisments.iterator(); iter2.hasNext(); ){
		    	Advertisment item = iter2.next();
		    	pl.hamerhed.scrapper.PageScrapper estateItemParser = new PageScrapper();
		    	estateItemParser.parse(item.getLink());
		    	Document estateRootDoc = estateItemParser.getDocument();
		    	
		    	EstateItemExtractor extractor = new EstateItemExtractor(estateRootDoc, item.getFirstEstateVersion());
		    	extractor.extract();
		    	//break;
		    }
		
	}
	
	public Collection<Advertisment> getItems(){
		return advertisments;
	}
}

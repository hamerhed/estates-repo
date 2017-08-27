package pl.hamerhed.scrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.hamerhed.datasource.AdvertismentsDataSource;
import pl.hamerhed.domain.AddressLink;
import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.domain.Estate;
import pl.hamerhed.domain.ExtractionSource;
import pl.hamerhed.domain.ExtractionSourceType;

@Service
class ScrapRunnerImpl implements ScrapRunner {
	private Collection<Advertisment> advertisments = new ArrayList<>();
	
	@Autowired
	private AdvertismentsDataSource<AddressLink> dataSource;

	@Autowired
	private PageScrapper scrapper;
	
	//private PageScrapper estateItemParser = new PageScrapper();
	
	private boolean pretendHuman = false;
	
	private List<ExtractionSource> extractionSources = new ArrayList<>();
	
	@Autowired
	private ItemExtractor<Estate, ExtractionSource> estateItemExtractor;
	//private EstateItemExtractor estateItemExtractor;
	
	@Autowired
	private ListDataExtractor<Advertisment, ExtractionSource> listDataExtractor;
	//private RealEstatesListExtractorImpl listDataExtractor;
	
	protected ScrapRunnerImpl(){}
	
	public ScrapRunnerImpl(AdvertismentsDataSource<AddressLink> dataSource,
			GratkaplItemExtractorImpl estateItemExtractor,
			GratkaplListDataExtractorImpl listDataExtractor){
		this.dataSource = dataSource;
		this.estateItemExtractor = estateItemExtractor;
		this.listDataExtractor = listDataExtractor;
	}
	
		
	protected ExtractionSource createExtractionSource(String content, String url, ExtractionSourceType type){
		ExtractionSource item = new ExtractionSource();
		item.setContent(content);
		item.setOriginalLink(url);
		item.setProcessedTimestamp(new Date());
		item.setType(type);
		return item;
	}
	
	public void parse() throws Exception {
		Iterator<AddressLink> iter = dataSource.iterator();
		
		while(iter.hasNext()){
			int currentPage = 1;
			Integer allPages = 1;
			AddressLink address = iter.next();
			while (currentPage <= allPages){
				String url = address.updateLinkParams(new String[]{Integer.toString(currentPage)});
				scrapper.parse(url);
					
			    //RealEstatesListExtractorImpl listDataExtractor = new RealEstatesListExtractorImpl(scrapper.getDocument());
			    		    
			    listDataExtractor.parse(url);
			    allPages = listDataExtractor.getPagesNumber();
			    
			    ExtractionSource source = listDataExtractor.getExtractedSources();
			    extractionSources.add(source);
			    		
			    System.out.println("to moje estates " + listDataExtractor.getItems().size());
			    advertisments.addAll(listDataExtractor.getItems());
			    currentPage++;
			    if(pretendHuman){
			    	waitRandomTime(3000, 20000);
			    }
			}
		}
		
		System.out.println("mam tyle nieruchomosci " + advertisments.size());
		for(Iterator<Advertisment> iter2 = advertisments.iterator(); iter2.hasNext(); ){
		    	Advertisment item = iter2.next();
		    	
		    	//estateItemParser.parse(item.getLink());
		    	//Document estateRootDoc = estateItemParser.getDocument();
		    	//itemExtractor = new EstateItemExtractor(estateRootDoc, item.getFirstEstateVersion());
		    	estateItemExtractor.parse(item.getLink(), item.getFirstEstateVersion());
		    	//IExtractionSource source = createExtractionSource(scrapper.getDocument().outerHtml(), item.getLink(), ExtractionSourceType.OFFERS_ITEM_SOURCE);
			   extractionSources.add(estateItemExtractor.getExtractedSources());
		    	if(pretendHuman){
		    		waitRandomTime(3000, 20000);
		    	}
		    	//break;
		}
		
	}
	
	private void waitRandomTime(int minTimeMs, int maxTimeMs){
		Random rand = new Random(System.currentTimeMillis());
		int mytime = 0;
		do {
			rand.setSeed(System.currentTimeMillis());
			mytime = rand.nextInt() % maxTimeMs;
		} while (mytime < minTimeMs);
		System.out.println("to moj czas w ms " + mytime);
		try {
			Thread.sleep(mytime);
		} catch (InterruptedException e){}
	}
	
	public Collection<Advertisment> getItems(){
		return advertisments;
	}

	@Override
	public Collection<ExtractionSource> getExtractionSources() {
		return extractionSources;
	}
}

package pl.hamerhed.scrapper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import pl.hamerhed.TestConfig;
import pl.hamerhed.datasource.AdvertismentsDataSource;
import pl.hamerhed.domain.AddressLink;
import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.domain.Estate;
import pl.hamerhed.domain.ExtractionSource;
import pl.hamerhed.domain.IExtractionSource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
@ActiveProfiles(profiles="test")

public class ScrapRunnerTest {
	@Autowired
	@InjectMocks
	private ScrapRunner scrapRunner;
	
	@Mock
	private AdvertismentsDataSource<AddressLink> dataSource;
	
	@Mock
	private PageScrapper scrapper;
	
	private Document ratajePageList;
	
	@Mock
	private ItemExtractor<Estate, ExtractionSource> estateItemExtractor;
	
	@Mock
	private ListDataExtractor<Advertisment, ExtractionSource> listDataExtractor;
	
	@Before
	public void setUp() throws Exception{
		scrapRunner = new ScrapRunnerImpl();
		
		MockitoAnnotations.initMocks(this);
	
		try(InputStream in = this.getClass().getResourceAsStream("gratka-pl-rataje-list-page1.txt");) {
			 ratajePageList = Jsoup.parse(in, null, "http://dom.gratka.pl/");
		}
		
		
		
		Mockito.doNothing().when(scrapper).parse(anyString());
		//Mockito.doReturn(ratajePageList).when(scrapper).getDocument();

		Mockito.doNothing().when(estateItemExtractor).parse(any(), any());
		Mockito.doNothing().when(listDataExtractor).parse(anyString());
		
		System.out.println("to moj mock " + listDataExtractor);
		
		
		
	}
	
	
	@Test
	public void testGetEmptyLinksFromRepo(){
		//scrapRunner = new ScrapRunnerImpl(dataSource, estateItemExtractor, listDataExtractor);
		ScrapRunner spy = PowerMockito.spy(scrapRunner);
		
		when(dataSource.iterator()).thenReturn(new ArrayList<AddressLink>().iterator());
		
		try {
			spy.parse();
			verify(dataSource, times(1)).iterator();
		} catch (Exception e) {
			fail("should not happened");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testExecute1Page() {
		//scrapRunner = new ScrapRunnerImpl(dataSource, estateItemExtractor, listDataExtractor);
		ScrapRunner spy = PowerMockito.spy(scrapRunner);
		List<Advertisment> list = new ArrayList<>();
		Advertisment ad = new Advertisment();
		ad.setLink("www.item1.pl");
		
		Advertisment ad2 = new Advertisment();
		ad2.setLink("www.item2.pl");
		list.add(ad);
		list.add(ad2);
		
		Mockito.when(listDataExtractor.getItems()).thenReturn(list);
		
		List<AddressLink> links = new ArrayList<>();
		links.add(new AddressLink("www.link1.pl", null, new String[]{":dummyParam:"}));
		when(dataSource.iterator()).thenReturn(links.iterator());
		
		try {
			spy.parse();
						
			verify(dataSource, times(1)).iterator();
			verify(listDataExtractor, times(1)).parse(any());
			verify(estateItemExtractor, times(2)).parse(any(), any());
			assertEquals(3, spy.getExtractionSources().size());
		} catch (Exception e) {
			fail("should not happened");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testExecute3Pages() {
		//scrapRunner = new ScrapRunnerImpl(dataSource, estateItemExtractor, listDataExtractor);
		ScrapRunner spy = PowerMockito.spy(scrapRunner);
		List<Advertisment> list = new ArrayList<>();
		Advertisment ad = new Advertisment();
		ad.setLink("www.item1.pl");
		list.add(ad);
		
		
		Mockito.when(listDataExtractor.getItems()).thenReturn(list);
		Mockito.when(listDataExtractor.getPagesNumber()).thenReturn(new Integer(3));
		
		
		List<AddressLink> links = new ArrayList<>();
		links.add(new AddressLink("www.link1.pl/p=:dummyParam:", null, new String[]{":dummyParam:"}));
		when(dataSource.iterator()).thenReturn(links.iterator());
		
		try {
			spy.parse();
						
			verify(dataSource, times(1)).iterator();
			verify(listDataExtractor, times(3)).parse(any());
			verify(estateItemExtractor, times(3)).parse(any(), any());
			assertEquals(6, spy.getExtractionSources().size());
		} catch (Exception e) {
			fail("should not happened");
			e.printStackTrace();
		}
	}
}

package pl.hamerhed.scrapper;

import static org.mockito.ArgumentMatchers.anyString;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;
import pl.hamerhed.TestConfig;
import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.domain.ExtractionSource;
import pl.hamerhed.domain.ExtractionSourceType;
import pl.hamerhed.test.DateUtils;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=TestConfig.class)
@ActiveProfiles(profiles="test")
public class ListDataExtractorTest extends TestCase {
	
	private ListDataExtractor<Advertisment, ExtractionSource> listExtractor;
	
	private Document twoPagesListPage;
	private Document onePageListPage;
	private Document wielkopolskaListPage;
	
	@Before
	public void setUp() throws Exception{
		super.setUp();
		
		try(InputStream in = this.getClass().getResourceAsStream("gratka-pl-rataje-list-page1.txt");) {
			 twoPagesListPage = Jsoup.parse(in, null, "http://dom.gratka.pl/");
		}
		
		try(InputStream in = this.getClass().getResourceAsStream("gratka-pl-rataje-list-1page.txt");) {
			 onePageListPage = Jsoup.parse(in, null, "http://dom.gratka.pl/");
		}
		
		try(InputStream in = this.getClass().getResourceAsStream("gratka-pl-wielkopolska-list.txt");) {
			 wielkopolskaListPage = Jsoup.parse(in, null, "http://dom.gratka.pl/");
		}
		
		listExtractor = new GratkaplListDataExtractorImpl();
	
	}
	
	@Test
	public void testListExtraction() throws Exception{
		ListDataExtractor<Advertisment, ExtractionSource> spy = PowerMockito.spy(listExtractor);
		PowerMockito.doReturn(twoPagesListPage).when(spy, "getDocumentFromLink",  anyString());
		
		try {
			spy.parse("www.link.pl");
			
			assertEquals(new Integer(2), spy.getPagesNumber());
			assertEquals(40, spy.getItems().size());
			
			assertNotNull(spy.getExtractedSources());
			assertEquals(twoPagesListPage.outerHtml(), spy.getExtractedSources().getContent());
			assertEquals(ExtractionSourceType.OFFERS_ITEMS_LIST_SOURCE, spy.getExtractedSources().getType());
			assertTrue(DateUtils.isToday(spy.getExtractedSources().getCreationDate()));
			assertTrue(DateUtils.isToday(spy.getExtractedSources().getProcessedTimestamp()));
		} catch (Exception e){
			fail("Exception " + e.getLocalizedMessage() + " should not happened");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPageNumberOnePages() throws Exception{
		ListDataExtractor<Advertisment, ExtractionSource> spy = PowerMockito.spy(listExtractor);
		PowerMockito.doReturn(onePageListPage).when(spy, "getDocumentFromLink",  anyString());
		
		try {
			spy.parse("www.link.pl");
			
			assertEquals(null, spy.getPagesNumber());
			assertEquals(10, spy.getItems().size());
			
			assertNotNull(spy.getExtractedSources());
			assertEquals(onePageListPage.outerHtml(), spy.getExtractedSources().getContent());
			assertEquals(ExtractionSourceType.OFFERS_ITEMS_LIST_SOURCE, spy.getExtractedSources().getType());
			assertTrue(DateUtils.isToday(spy.getExtractedSources().getCreationDate()));
			assertTrue(DateUtils.isToday(spy.getExtractedSources().getProcessedTimestamp()));
		} catch (Exception e){
			fail("Exception " + e.getLocalizedMessage() + " should not happened");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPageNumbers26Pages() throws Exception{
		ListDataExtractor<Advertisment, ExtractionSource> spy = PowerMockito.spy(listExtractor);
		PowerMockito.doReturn(wielkopolskaListPage).when(spy, "getDocumentFromLink",  anyString());
		
		try {
			spy.parse("www.link.pl");
			
			assertEquals(new Integer(26), spy.getPagesNumber());
		} catch (Exception e){
			fail("Exception " + e.getLocalizedMessage() + " should not happened");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPageNumbers26PagesExistingEstates() throws Exception{
		ListDataExtractor<Advertisment, ExtractionSource> spy = PowerMockito.spy(listExtractor);
		PowerMockito.doReturn(wielkopolskaListPage).when(spy, "getDocumentFromLink",  anyString());
		
		try {
			spy.parse("www.link.pl");
			assertEquals(new Integer(26), spy.getPagesNumber());
			
			List<Advertisment> list = spy.getItems().stream().filter(x -> x.getLink().equals("http://dom.gratka.pl/tresc/397-72476291-wielkopolskie-poznan-grunwald-lazarz-centrum-chelmonskiego.html")).collect(Collectors.toList());
			Advertisment item = list.get(0);
			assertNotNull(item);
			assertNotNull(item.getAdditionDate());
			assertEquals("wtórny", item.getMarketType());
			assertTrue(DateUtils.isToday(item.getCreationDate()));
			assertEquals("72476291", item.getAdvertismentId());
			assertEquals(1, item.getEstateVersions().size());
			assertEquals(new BigDecimal(458175), item.getFirstEstateVersion().getPrice());
		} catch (Exception e){
			fail("Exception " + e.getLocalizedMessage() + " should not happened");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPageNumbers26PagesExistingRynekPierwotnyEstate() throws Exception{
		ListDataExtractor<Advertisment, ExtractionSource> spy = PowerMockito.spy(listExtractor);
		PowerMockito.doReturn(wielkopolskaListPage).when(spy, "getDocumentFromLink",  anyString());
		
		try {
			spy.parse("www.link.pl");
			assertEquals(new Integer(26), spy.getPagesNumber());
			
			List<Advertisment> list = spy.getItems().stream().filter(x -> x.getLink().equals("http://dom.gratka.pl/deweloperzy/tresc/422-69671377-1-osiedlebliskie.html")).collect(Collectors.toList());
			Advertisment item = list.get(0);
			assertNotNull(item);
			assertNotNull(item.getAdditionDate());
			assertEquals("pierwotny", item.getMarketType());
			assertTrue(DateUtils.isToday(item.getCreationDate()));
			assertEquals("69671377", item.getAdvertismentId());
			assertEquals(1, item.getEstateVersions().size());
			assertEquals(new BigDecimal(339000), item.getFirstEstateVersion().getPrice());
		} catch (Exception e){
			fail("Exception " + e.getLocalizedMessage() + " should not happened");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPageNumbers26PagesExistingRynekPierwotnyMDMEstate() throws Exception{
		ListDataExtractor<Advertisment, ExtractionSource> spy = PowerMockito.spy(listExtractor);
		PowerMockito.doReturn(wielkopolskaListPage).when(spy, "getDocumentFromLink",  anyString());
		
		try {
			spy.parse("www.link.pl");
			assertEquals(new Integer(26), spy.getPagesNumber());
			
			List<Advertisment> list = spy.getItems().stream().filter(x -> x.getLink().equals("http://dom.gratka.pl/deweloperzy/tresc/422-69662071-1-osiedlebliskie.html")).collect(Collectors.toList());
			assertEquals(1, list.size());
			Advertisment item = list.get(0);
			assertNotNull(item);
			assertNotNull(item.getAdditionDate());
			assertEquals("pierwotny", item.getMarketType());
			assertTrue(DateUtils.isToday(item.getCreationDate()));
			assertEquals("69662071", item.getAdvertismentId());
			assertEquals(1, item.getEstateVersions().size());
			assertEquals(new BigDecimal(330000), item.getFirstEstateVersion().getPrice());
		} catch (Exception e){
			fail("Exception " + e.getLocalizedMessage() + " should not happened");
			e.printStackTrace();
		}
	}
}

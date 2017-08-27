package pl.hamerhed.scrapper;

import static org.mockito.ArgumentMatchers.anyString;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;
import pl.hamerhed.TestConfig;
import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.domain.Estate;
import pl.hamerhed.domain.ExtractionSource;
import pl.hamerhed.domain.ExtractionSourceType;
import pl.hamerhed.domain.IExtractionSource;
import pl.hamerhed.test.DateUtils;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=TestConfig.class)
@ActiveProfiles(profiles="test")
public class GratkaplItemExtractorTest extends TestCase {

	@Autowired
	private ItemExtractor<Estate, ExtractionSource> itemExtractor;
	
	@Autowired
	private ListDataExtractor<Advertisment, ExtractionSource> listDataExtractor;
	
	private Document wielkopolskaListPage;
	private Document wielkopolska2ListPage;
	private Document item72476291Page;
	private Document item71657346Page;
	private Document item73269135Page;
	
	@Before
	public void setUp() throws Exception{
		super.setUp();
		
		MockitoAnnotations.initMocks(this);
		
		try(InputStream in = this.getClass().getResourceAsStream("gratka-pl-wielkopolska-list.txt");) {
			 wielkopolskaListPage = Jsoup.parse(in, null, "http://dom.gratka.pl/");
		}
		
		try(InputStream in = this.getClass().getResourceAsStream("gratka-pl-wielkopolska-list2.txt");) {
			 wielkopolska2ListPage = Jsoup.parse(in, null, "http://dom.gratka.pl/");
		}
		
		try(InputStream in = this.getClass().getResourceAsStream("gratka-pl-item-72476291.txt");) {
			item72476291Page = Jsoup.parse(in, null, "http://dom.gratka.pl/");
		}
		
		try(InputStream in = this.getClass().getResourceAsStream("gratka-pl-item-71657346.txt");) {
			item71657346Page = Jsoup.parse(in, null, "http://dom.gratka.pl/");
		}
		
		try(InputStream in = this.getClass().getResourceAsStream("gratka-pl-item-73269135.txt");) {
			item73269135Page = Jsoup.parse(in, null, "http://dom.gratka.pl/");
		}
		listDataExtractor = PowerMockito.spy(listDataExtractor);
		PowerMockito.doReturn(wielkopolskaListPage).when(listDataExtractor, "getDocumentFromLink",  "www.link1.pl");
		PowerMockito.doReturn(wielkopolska2ListPage).when(listDataExtractor, "getDocumentFromLink",  "www.link2.pl");
	}
	
	@Test
	public void testExtraction72476291Item() throws Exception{
		ItemExtractor<Estate, ExtractionSource> spy = PowerMockito.spy(itemExtractor);
		PowerMockito.doReturn(item72476291Page).when(spy, "getDocumentFromLink",  anyString());
		
		try {
			listDataExtractor.parse("www.link1.pl");
			List<Advertisment> list = listDataExtractor.getItems().stream().filter(x -> x.getLink().equals("http://dom.gratka.pl/tresc/397-72476291-wielkopolskie-poznan-grunwald-lazarz-centrum-chelmonskiego.html")).collect(Collectors.toList());
			assertEquals(1, list.size());
			
			System.out.println("aaa");
			Advertisment item = list.get(0); 
			
			spy.parse(item.getLink(), item.getLastEstateVersion());
			
			assertEquals("Mieszkanie", item.getLastEstateVersion().getEstateType());
			assertEquals(61.09f, item.getLastEstateVersion().getArea());
			assertEquals(new Integer(3), item.getLastEstateVersion().getRoomsNumber());
			assertEquals(new Integer(0), item.getLastEstateVersion().getFloor());
			assertEquals("własność", item.getLastEstateVersion().getPropertyForm());
			assertEquals("jednopoziomowe", item.getLastEstateVersion().getFlatLevels());
			assertEquals("nowa", item.getLastEstateVersion().getInstallationState());
			assertEquals("nowe plastikowe", item.getLastEstateVersion().getFlatWindows());
			assertEquals("loggia, komórka lokatorska", item.getLastEstateVersion().getAdditionalArea());
			assertTrue(item.getLastEstateVersion().getFlatDescription().length() > 0);
			assertNull(item.getLastEstateVersion().getBathroom());
			assertNull(item.getLastEstateVersion().getBathroomCondition());
			assertNull(item.getLastEstateVersion().getCarPlace());
			assertEquals("telefon, internet, TV kablowa, winda, ogrzewanie miejskie", item.getLastEstateVersion().getFlatAdditions());
			assertNull(item.getLastEstateVersion().getFlatCondition());
			assertNull(item.getLastEstateVersion().getFlatNoiseLevel());
			assertNull(item.getLastEstateVersion().getKitchen());
			assertNull(item.getLastEstateVersion().getNorthOrientation());
			assertNull(item.getLastEstateVersion().getPipesCondition());
			assertEquals(new BigDecimal(458175), item.getLastEstateVersion().getPrice());
			assertNotNull(item.getLastEstateVersion().getModificationDate());
			
			assertEquals("Poznań", item.getLastEstateVersion().getAddress().getCity());
			assertEquals("Grunwald,  Łazarz,  Centrum", item.getLastEstateVersion().getAddress().getDistrict());
			assertEquals("ul. Chełmońskiego", item.getLastEstateVersion().getAddress().getStreet());
			assertEquals("wielkopolskie", item.getLastEstateVersion().getAddress().getVoivodine());
			assertNotNull(item.getLastEstateVersion().getAddress().getCreationDate());
			
			assertEquals(new Integer(5), item.getLastEstateVersion().getBuilding().getFloorsNumber());
			assertEquals("cegła", item.getLastEstateVersion().getBuilding().getConstructionMaterial());
			assertEquals("kamienica", item.getLastEstateVersion().getBuilding().getBuildingType());
			assertNotNull(item.getLastEstateVersion().getBuilding().getBuildYear());
			
			
			IExtractionSource source = spy.getExtractedSources();
			assertEquals(item72476291Page.outerHtml(), source.getContent());
			assertEquals("http://dom.gratka.pl/tresc/397-72476291-wielkopolskie-poznan-grunwald-lazarz-centrum-chelmonskiego.html", source.getOriginalLink());
			assertEquals(ExtractionSourceType.OFFERS_ITEM_SOURCE, source.getType());
			assertTrue(DateUtils.isToday(source.getProcessedTimestamp()));
			
		} catch(Exception e){
			fail("Exception " + e.getLocalizedMessage() + " should not happened");
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testExtraction71657346Item() throws Exception{
		ItemExtractor<Estate, ExtractionSource> spy = PowerMockito.spy(itemExtractor);
		PowerMockito.doReturn(item71657346Page).when(spy, "getDocumentFromLink",  anyString());
		
		try {
			listDataExtractor.parse("www.link1.pl");
			List<Advertisment> list = listDataExtractor.getItems().stream().filter(x -> x.getLink().equals("http://dom.gratka.pl/deweloperzy/tresc/422-71657346-1-osiedlebliskie.html")).collect(Collectors.toList());
			assertEquals(1, list.size());
			
			Advertisment item = list.get(0); 
			
			spy.parse(item.getLink(), item.getLastEstateVersion());
			
			assertEquals("Nowe mieszkanie", item.getLastEstateVersion().getEstateType());
			assertEquals(73f, item.getLastEstateVersion().getArea());
			assertEquals(new Integer(4), item.getLastEstateVersion().getRoomsNumber());
			assertEquals(new Integer(0), item.getLastEstateVersion().getFloor());
			assertEquals("własność", item.getLastEstateVersion().getPropertyForm());
			assertEquals("dwupoziomowe", item.getLastEstateVersion().getFlatLevels());
			assertNull(item.getLastEstateVersion().getInstallationState());
			assertEquals("plastikowe", item.getLastEstateVersion().getFlatWindows());
			assertEquals("drzwi balkonowe, strych, ogród", item.getLastEstateVersion().getAdditionalArea());
			assertTrue(item.getLastEstateVersion().getFlatDescription().length() > 0);
			assertNull(item.getLastEstateVersion().getBathroom());
			assertNull(item.getLastEstateVersion().getBathroomCondition());
			assertEquals("miejsce parkingowe", item.getLastEstateVersion().getCarPlace());
			assertNull(item.getLastEstateVersion().getFlatAdditions());
			assertNull(item.getLastEstateVersion().getFlatCondition());
			assertNull(item.getLastEstateVersion().getFlatNoiseLevel());
			assertNull(item.getLastEstateVersion().getKitchen());
			assertNull(item.getLastEstateVersion().getNorthOrientation());
			assertNull(item.getLastEstateVersion().getPipesCondition());
			assertEquals(new BigDecimal(339000), item.getLastEstateVersion().getPrice());
			assertNotNull(item.getLastEstateVersion().getModificationDate());
			
			assertEquals("Poznań", item.getLastEstateVersion().getAddress().getCity());
			assertEquals("", item.getLastEstateVersion().getAddress().getDistrict());
			assertEquals("Plewiska Skórzewo Luboń Komorniki Grunwald", item.getLastEstateVersion().getAddress().getStreet());
			assertEquals("wielkopolskie", item.getLastEstateVersion().getAddress().getVoivodine());
			assertNotNull(item.getLastEstateVersion().getAddress().getCreationDate());
			
			
			assertEquals(new Integer(1), item.getLastEstateVersion().getBuilding().getFloorsNumber());
			assertEquals("pustak", item.getLastEstateVersion().getBuilding().getConstructionMaterial());
			assertEquals("dom wielorodzinny", item.getLastEstateVersion().getBuilding().getBuildingType());
			assertNull(item.getLastEstateVersion().getBuilding().getBuildYear());
			
			
			IExtractionSource source = spy.getExtractedSources();
			assertEquals(item71657346Page.outerHtml(), source.getContent());
			assertEquals("http://dom.gratka.pl/deweloperzy/tresc/422-71657346-1-osiedlebliskie.html", source.getOriginalLink());
			assertEquals(ExtractionSourceType.OFFERS_ITEM_SOURCE, source.getType());
			assertTrue(DateUtils.isToday(source.getProcessedTimestamp()));
			
		} catch(Exception e){
			fail("Exception " + e.getLocalizedMessage() + " should not happened");
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testExtraction73269135Item() throws Exception{
		ItemExtractor<Estate, ExtractionSource> spy = PowerMockito.spy(itemExtractor);
		PowerMockito.doReturn(item73269135Page).when(spy, "getDocumentFromLink",  anyString());
		
		try {
			listDataExtractor.parse("www.link2.pl");
			List<Advertisment> list = listDataExtractor.getItems().stream().filter(x -> x.getLink().equals("http://dom.gratka.pl/tresc/397-73269135-wielkopolskie-poznan-literacka.html")).collect(Collectors.toList());
			assertEquals(1, list.size());
			
			Advertisment item = list.get(0); 
			
			spy.parse(item.getLink(), item.getLastEstateVersion());
			
			assertEquals("Mieszkanie", item.getLastEstateVersion().getEstateType());
			assertEquals(65.1f, item.getLastEstateVersion().getArea());
			assertEquals(new Integer(3), item.getLastEstateVersion().getRoomsNumber());
			assertEquals(new Integer(0), item.getLastEstateVersion().getFloor());
			assertEquals("własność", item.getLastEstateVersion().getPropertyForm());
			assertEquals("jednopoziomowe", item.getLastEstateVersion().getFlatLevels());
			assertEquals("nowa", item.getLastEstateVersion().getInstallationState());
			assertEquals("nowe plastikowe", item.getLastEstateVersion().getFlatWindows());
			assertEquals("balkon, piwnica", item.getLastEstateVersion().getAdditionalArea());
			assertTrue(item.getLastEstateVersion().getFlatDescription().length() > 0);
			assertEquals("z oknem, prysznic", item.getLastEstateVersion().getBathroom());
			assertEquals("bardzo dobry", item.getLastEstateVersion().getBathroomCondition());
			assertEquals("garaż, miejsce parkingowe", item.getLastEstateVersion().getCarPlace());
			assertEquals("internet, TV kablowa, patio, ogrzewanie miejskie, recepcja, domofon, ochrona, teren ogrodzony, podjazd dla niepełnosprawnych", item.getLastEstateVersion().getFlatAdditions());
			assertEquals("bardzo dobry", item.getLastEstateVersion().getFlatCondition());
			assertEquals("ciche", item.getLastEstateVersion().getFlatNoiseLevel());
			assertEquals("z oknem, w aneksie, z wyposażeniem, w zabudowie", item.getLastEstateVersion().getKitchen());
			assertNull(item.getLastEstateVersion().getNorthOrientation());
			assertNull(item.getLastEstateVersion().getPipesCondition());
			assertEquals(new BigDecimal(499000), item.getLastEstateVersion().getPrice());
			assertNotNull(item.getLastEstateVersion().getModificationDate());
			
			assertEquals("Poznań", item.getLastEstateVersion().getAddress().getCity());
			assertTrue(item.getLastEstateVersion().getAddress().getDistrict().length() == 0);
			assertEquals("ul. Literacka", item.getLastEstateVersion().getAddress().getStreet());
			assertEquals("wielkopolskie", item.getLastEstateVersion().getAddress().getVoivodine());
			assertNotNull(item.getLastEstateVersion().getAddress().getCreationDate());
			
			
			assertEquals(new Integer(4), item.getLastEstateVersion().getBuilding().getFloorsNumber());
			assertEquals("silikat", item.getLastEstateVersion().getBuilding().getConstructionMaterial());
			assertEquals("blok", item.getLastEstateVersion().getBuilding().getBuildingType());
			assertEquals("2007", new SimpleDateFormat("YYYY").format(item.getLastEstateVersion().getBuilding().getBuildYear()));
			
			
			IExtractionSource source = spy.getExtractedSources();
			assertEquals(item73269135Page.outerHtml(), source.getContent());
			assertEquals("http://dom.gratka.pl/tresc/397-73269135-wielkopolskie-poznan-literacka.html", source.getOriginalLink());
			assertEquals(ExtractionSourceType.OFFERS_ITEM_SOURCE, source.getType());
			assertTrue(DateUtils.isToday(source.getProcessedTimestamp()));
			
		} catch(Exception e){
			fail("Exception " + e.getLocalizedMessage() + " should not happened");
			e.printStackTrace();
		}	
	}
}


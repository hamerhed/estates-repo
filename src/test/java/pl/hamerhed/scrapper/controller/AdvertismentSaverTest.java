package pl.hamerhed.scrapper.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javafx.beans.binding.When;
import junit.framework.TestCase;
import pl.hamerhed.AdvertismentRepo;
import pl.hamerhed.TestConfig;
import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.scrapper.persistance.AdvertismentRepository;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
//@org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest(showSql=true)
@SpringBootTest(classes=TestConfig.class)
@ActiveProfiles(profiles="test")

public class AdvertismentSaverTest extends TestCase {
	
	@Autowired
	@InjectMocks
	private pl.hamerhed.scrapper.controller.AdvertismentSaver advertismentSaver;
	
	@Mock
	private AdvertismentRepository advertismentRepository;
	
	private List<Advertisment> items;

	private List<Advertisment> versionItems;
	
	
	@Before
	public void setUp() throws Exception {
		items = AdvertismentRepo.createAdvertisments();
	
		versionItems = new ArrayList<>();
		versionItems.add(items.get(2));
		versionItems.add(items.get(3));
		
		MockitoAnnotations.initMocks(this);	
		
	}
	
	@Test
	public void testEmptyCollection(){
		Collection<Advertisment> col = null;
		assertThat(null, is(col));
	
		try {
			assertNotNull(advertismentSaver.processVersions(col));
		} catch (Exception e) {
			fail("Should not happened");
		}
	}
	
	@Test
	public void testEmptyDatabase(){
		items.remove(items.size()-1);
		items.remove(items.size()-1);
		
		Mockito.when(advertismentRepository.findbyOriginalAdvertismentIdAndLink(items.get(0).getAdvertismentId(), items.get(0).getLink())).thenReturn(Optional.ofNullable(null));
		Mockito.when(advertismentRepository.findbyOriginalAdvertismentIdAndLink(items.get(1).getAdvertismentId(), items.get(1).getLink())).thenReturn(Optional.ofNullable(null));
				
		try {
			Collection<Advertisment> filtered = advertismentSaver.processVersions(items);
			assertEquals(2, filtered.size());
			
			filtered.stream().forEach(s -> assertThat(1, is(s.getEstateVersions().size())));
			
		} catch (Exception e) {
			fail("exception should happened");
		}
	}
	
	@Test
	public void testUpdatesOfAdvertisment() throws CloneNotSupportedException{
/*		assertThat(2, is(versionItems.size()));
		for(int i = 0; i < items.size(); i++) {
			System.out.println("items["+i+"]"  + items.get(i).getFirstEstateVersion().getId());
		}
		
		for(int i = 0; i < versionItems.size(); i++) {
			System.out.println("versionItems["+i+"]"  + versionItems.get(i).getFirstEstateVersion().getId());
		}*/
		
		versionItems.remove(versionItems.size()-1);
		assertThat(1, is(versionItems.size()));
		
		Advertisment mockResponse = (Advertisment) org.apache.commons.lang.SerializationUtils.clone(items.get(0));
		
		assertEquals(items.get(0).getId(), mockResponse.getId());
		assertEquals(items.get(0).getFirstEstateVersion().getId(), mockResponse.getFirstEstateVersion().getId());
		
		//when
		Mockito.when(advertismentRepository.findbyOriginalAdvertismentIdAndLink(versionItems.get(0).getAdvertismentId(), versionItems.get(0).getLink())).thenReturn(Optional.ofNullable(mockResponse));
		
		assertNotEquals(versionItems.get(0).getFirstEstateVersion().getId().toString(), items.get(0).getFirstEstateVersion().getId().toString());
		try {
			//then
			Collection<Advertisment> filtered = advertismentSaver.processVersions(versionItems);
			
			assertNotNull(filtered);
			assertEquals(1, filtered.size());

			filtered.stream().forEach(s -> {assertThat(2, is(s.getEstateVersions().size()));}); 
		
			filtered.stream().forEach(s -> assertEquals(versionItems.get(0).getLastEstateVersion().getId().toString(), s.getLastEstateVersion().getId().toString()));

			filtered.stream().forEach(s -> assertEquals(items.get(0).getLastEstateVersion().getId().toString(), s.getFirstEstateVersion().getId().toString()));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("exception should not happened");
		}
	}
	
	@Test
	public void testUpdateOf2Advertisments(){
		Advertisment mockAd1 = (Advertisment) org.apache.commons.lang.SerializationUtils.clone(items.get(0));
		Advertisment mockAd2 = (Advertisment) org.apache.commons.lang.SerializationUtils.clone(items.get(1));
		
		Mockito.when(advertismentRepository.findbyOriginalAdvertismentIdAndLink(versionItems.get(0).getAdvertismentId(), versionItems.get(0).getLink())).thenReturn(Optional.ofNullable(mockAd1));
		Mockito.when(advertismentRepository.findbyOriginalAdvertismentIdAndLink(versionItems.get(1).getAdvertismentId(), versionItems.get(1).getLink())).thenReturn(Optional.ofNullable(mockAd2));
				
		try {
			Collection<Advertisment> filtered = advertismentSaver.processVersions(versionItems);
			assertNotNull(filtered);
			assertEquals(2, filtered.size());
			
			assertTrue(filtered.contains(mockAd1));
			assertTrue(filtered.contains(mockAd2));
			
			filtered.stream().forEach(s -> {assertThat(2, is(s.getEstateVersions().size()));}); 
			
			filtered.stream().filter(s->s.equals(mockAd1)).forEach(e -> assertEquals(items.get(0).getFirstEstateVersion(),  e.getFirstEstateVersion()));
			filtered.stream().filter(s->s.equals(mockAd1)).forEach(e -> assertEquals(versionItems.get(0).getLastEstateVersion(),  e.getLastEstateVersion()));
			
			filtered.stream().filter(s->s.equals(mockAd2)).forEach(e -> assertEquals(items.get(1).getFirstEstateVersion(),  e.getFirstEstateVersion()));
			filtered.stream().filter(s->s.equals(mockAd2)).forEach(e -> assertEquals(versionItems.get(1).getLastEstateVersion(),  e.getLastEstateVersion()));
			
			} catch (Exception e) {
			e.printStackTrace();
			fail("exception should not happened");
		}
	}
	
	@Test
	public void testSaveNullSet(){
		try {
			assertFalse(advertismentSaver.save(Optional.ofNullable(null)).isPresent());
		} catch (Exception e){
			fail("exception should not happen. Error msg: " + e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testSaveEmptyCol() throws Exception{
		List<Advertisment> emptyList = Collections.<Advertisment>emptyList();
		when(advertismentRepository.save(emptyList)).thenReturn(emptyList);
		
		Optional<Iterable<Advertisment>> result = advertismentSaver.save(Optional.ofNullable(emptyList));
		assertTrue(result.isPresent());
		assertFalse(result.get().iterator().hasNext());
	}
	
	@Test
	public void testSaveOneElement() throws Exception{

		List<Advertisment> list = new ArrayList<>();
		list.add(items.get(0));
		
		when(advertismentRepository.save(list)).thenReturn(list);
		
		Optional<Iterable<Advertisment>> result = advertismentSaver.save(Optional.ofNullable(list));
		assertEquals(items.get(0).getId(), result.get().iterator().next().getId());
	}
	
	@Test
	public void testFilterTheSameEstate(){
		//System.out.println("start same estates");
		Advertisment mockAd1 = (Advertisment) org.apache.commons.lang.SerializationUtils.clone(items.get(0));
		assertEquals(mockAd1.getId(), items.get(0).getId());
		assertEquals(mockAd1.getFirstEstateVersion().getModificationDate(), items.get(0).getFirstEstateVersion().getModificationDate());
		//System.out.println("mockAd1 data " + mockAd1.getFirstEstateVersion().getModificationDate());
		
		mockAd1.setId(UUID.randomUUID());
		mockAd1.getFirstEstateVersion().setId(UUID.randomUUID());
		assertNotEquals(mockAd1.getId(), items.get(0).getId());
		
		when(advertismentRepository.findbyOriginalAdvertismentIdAndLink(items.get(0).getAdvertismentId(), items.get(0).getLink())).thenReturn(Optional.ofNullable(items.get(0)));
		
		List<Advertisment> list = new ArrayList<>();
		list.add(mockAd1);
		try {
			Collection<Advertisment> result = advertismentSaver.processVersions(list);
			assertEquals(0, result.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception should not happened: " + e.getLocalizedMessage());
		}
		//System.out.println("end same estates");
	}
	
	@Test
	public void testFilterModifiedTodayEstate(){
		//System.out.println("start same estates");
		Advertisment mockAd1 = (Advertisment) org.apache.commons.lang.SerializationUtils.clone(items.get(0));
		assertEquals(mockAd1.getId(), items.get(0).getId());
		assertEquals(mockAd1.getFirstEstateVersion().getModificationDate(), items.get(0).getFirstEstateVersion().getModificationDate());
		//System.out.println("mockAd1 data " + mockAd1.getFirstEstateVersion().getModificationDate());
		
		mockAd1.setId(UUID.randomUUID());
		mockAd1.getFirstEstateVersion().setId(UUID.randomUUID());
		mockAd1.getFirstEstateVersion().setModificationDate(new Date());
		assertNotEquals(mockAd1.getId(), items.get(0).getId());
		
		when(advertismentRepository.findbyOriginalAdvertismentIdAndLink(items.get(0).getAdvertismentId(), items.get(0).getLink())).thenReturn(Optional.ofNullable(items.get(0)));
		
		List<Advertisment> list = new ArrayList<>();
		list.add(mockAd1);
		try {
			Collection<Advertisment> result = advertismentSaver.processVersions(list);
			assertEquals(1, result.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception should not happened: " + e.getLocalizedMessage());
		}
		//System.out.println("end same estates");
	}
	
	@Test
	public void testFilterModifiedYesterdayEstate(){
		//System.out.println("start same estates");
		Advertisment mockAd1 = (Advertisment) org.apache.commons.lang.SerializationUtils.clone(items.get(0));
		assertEquals(mockAd1.getId(), items.get(0).getId());
		assertEquals(mockAd1.getFirstEstateVersion().getModificationDate(), items.get(0).getFirstEstateVersion().getModificationDate());
		//System.out.println("mockAd1 data " + mockAd1.getFirstEstateVersion().getModificationDate());
		
		
		mockAd1.setId(UUID.randomUUID());
		mockAd1.getFirstEstateVersion().setId(UUID.randomUUID());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		mockAd1.getFirstEstateVersion().setModificationDate(cal.getTime());
		assertNotEquals(mockAd1.getId(), items.get(0).getId());
		
		when(advertismentRepository.findbyOriginalAdvertismentIdAndLink(items.get(0).getAdvertismentId(), items.get(0).getLink())).thenReturn(Optional.ofNullable(items.get(0)));
		
		List<Advertisment> list = new ArrayList<>();
		list.add(mockAd1);
		try {
			Collection<Advertisment> result = advertismentSaver.processVersions(list);
			assertEquals(0, result.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception should not happened: " + e.getLocalizedMessage());
		}
		//System.out.println("end same estates");
	}
	
}



package pl.hamerhed.scrapper.persistance;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import pl.hamerhed.AdvertismentRepo;
import pl.hamerhed.domain.Address;
import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.domain.Building;
import pl.hamerhed.domain.Estate;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class AdvertismentRepoTest extends pl.hamerhed.DataJpaTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private AdvertismentRepository advertismentRepository;

	 
	private List<Advertisment> items;

	@Before
	public void setUp() throws Exception {
		items = AdvertismentRepo.createAdvertisments();
	}

	@Test
	@Rollback
	public void testSaveEmptyCollection(){
		Iterable<Advertisment> result = advertismentRepository.save(new ArrayList<Advertisment>());
		assertNotNull(result);
		assertFalse(result.iterator().hasNext());
		
	}
	
	@Test
	@Rollback
	public void saveTest() {
		Advertisment addvertisment = items.get(0);
		assertNotNull(addvertisment.getId());
		
	    advertismentRepository.save(addvertisment);
	    	  
	    assertThat(1L, is(advertismentRepository.count()));
	    assertThat(1, is(advertismentRepository.findOne(addvertisment.getId()).getEstateVersions().size()));
	    
	    assertNotNull(advertismentRepository.findOne(addvertisment.getId()));
	    assertEquals(addvertisment, advertismentRepository.findOne(addvertisment.getId()));
	    assertEquals(addvertisment.getAdvertismentId(), advertismentRepository.findOne(addvertisment.getId()).getAdvertismentId());
	    
	    Advertisment saved = advertismentRepository.findOne(addvertisment.getId());
	    
	    assertThat(saved.getFirstEstateVersion().getAdvertisment().getId(), is(saved.getId()));
	    assertThat(saved.getLastEstateVersion().getAdvertisment().getId(), is(saved.getId()));
	}
	
	@Test
	@Rollback
	public void saveTest2Estates() {
		Advertisment advertisment = items.get(0);
		
		items.remove(3);
		items.remove(2);
		advertismentRepository.save(items);
	    
	    assertThat(2L, is(advertismentRepository.count()));
	    assertEquals(advertisment, advertismentRepository.findOne(advertisment.getId()));
	    assertEquals(items.get(1), advertismentRepository.findOne(items.get(1).getId()));
	    assertNotEquals(advertismentRepository.findOne(items.get(0).getId()), advertismentRepository.findOne(items.get(1).getId()));
   }
	
	@Test
	@Rollback
	public void getByAdIdAndLink(){
		items.remove(items.size() - 1);
		items.remove(items.size() - 1);
		
		advertismentRepository.save(items);
		Optional<Advertisment> saved1 = advertismentRepository.findbyOriginalAdvertismentIdAndLink(items.get(0).getAdvertismentId(), items.get(0).getLink());
		Optional<Advertisment> saved2 = advertismentRepository.findbyOriginalAdvertismentIdAndLink(items.get(1).getAdvertismentId(), items.get(1).getLink());
		
		try {
			assertNotNull(saved1.get());
			assertEquals(items.get(0).getId(), saved1.get().getId());
		} catch (NoSuchElementException e){
			fail("object should not be null");
		}
		
		try {
			assertNotNull(saved2.get());
			assertEquals(items.get(1).getId(), saved2.get().getId());
		} catch (NoSuchElementException e){
			fail("object should not be null");
		}
		
		
	}
	
	@Test
	@Rollback
	public void getByAdIdAndLinkNotFound(){
		items.remove(items.size() - 1);
		items.remove(items.size() - 1);
		
		advertismentRepository.save(items.get(0));
		try {
			advertismentRepository.findbyOriginalAdvertismentIdAndLink(items.get(1).getAdvertismentId(), items.get(1).getLink()).get();
			fail("method should return null");
		} catch(NoSuchElementException e){}
	}

}

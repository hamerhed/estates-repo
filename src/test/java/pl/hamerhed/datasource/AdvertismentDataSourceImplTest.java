package pl.hamerhed.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import pl.hamerhed.TestConfig;
import pl.hamerhed.datasource.AdvertismentsDataSource;
import pl.hamerhed.domain.AddressLink;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
@ActiveProfiles(profiles="test")
//TODO przepisac klase testow na nowy typ danych
public class AdvertismentDataSourceImplTest {
	
	@Autowired
	private AdvertismentsDataSource<AddressLink> stringAdvertismentDataSource;
	
	@Before
	public void setUp(){
		
	}
	
	@Test
	public void testEmptyIterator(){
		Whitebox.setInternalState(stringAdvertismentDataSource, "internalData", new ArrayList<>());
		assertNotNull(stringAdvertismentDataSource.iterator());
		assertFalse(stringAdvertismentDataSource.iterator().hasNext());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testUnmodifiableIterator(){
		List<AddressLink> list = Arrays.asList(new AddressLink("www.link1.pl", null, new String[]{}),
												new AddressLink("www.link2.pl", null, new String[]{}),
												new AddressLink("www.link3.pl", null, new String[]{}));
		//uwaga sprawdzane na etapie kompilacji nie runtime, wiec takie podstawienia przechodza!!!
		//jak to obejsc bo testy nie maja sensu? Pilnowac podstawienia danych?
		Whitebox.setInternalState(stringAdvertismentDataSource, "internalData", list);
		
		stringAdvertismentDataSource.iterator().remove();
	}
	
	@Test
	public void testNotEmptyIterator(){
		List<AddressLink> list = Arrays.asList(new AddressLink("www.link1.pl", null, new String[]{}),
												new AddressLink("www.link2.pl", null, new String[]{}),
												new AddressLink("www.link3.pl", null, new String[]{}));
		Whitebox.setInternalState(stringAdvertismentDataSource, "internalData", list);
		
		AdvertismentsDataSource<AddressLink> spy = spy(stringAdvertismentDataSource);
				
		assertNotNull(spy.iterator());
		Iterator<AddressLink> iterator = spy.iterator();
		int count = 0;
		while(iterator.hasNext()){
			assertTrue(list.contains(iterator.next()));
			count++;
		}
		assertEquals(list.size(), count);
	}
	
	@Test
	public void testEmptyCollection(){
		Whitebox.setInternalState(stringAdvertismentDataSource, "internalData", new ArrayList<>());
		assertNotNull(stringAdvertismentDataSource.getSources());
		assertEquals(0, stringAdvertismentDataSource.getSources().size());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testNonEmptyCollection(){
		List<AddressLink> list = Arrays.asList(new AddressLink("www.gratka.pl/1/2/3", null, new String[]{}),
												new AddressLink("www.gratka.pl/5/6/7", null, new String[]{}),
												new AddressLink("http://www.gratka.pl/789", null, new String[]{}));
		Whitebox.setInternalState(stringAdvertismentDataSource, "internalData", list);
		
		assertNotNull(stringAdvertismentDataSource.getSources());
		assertEquals(list.size(), stringAdvertismentDataSource.getSources().size());
		
		for (AddressLink item : list) {
			assertTrue(stringAdvertismentDataSource.getSources().contains(item));
		}
		
		//testing immutability	
		stringAdvertismentDataSource.getSources().add(null);
	}
}


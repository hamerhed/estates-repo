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

@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
@ActiveProfiles(profiles="test")
public class AdvertismentDataSourceImplTest {
	
	@Autowired
	private AdvertismentsDataSource<String> stringAdvertismentDataSource;
	
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
		List<String> list = Arrays.asList("www.link1.pl", "www.link2.pl", "www.link3.pl");
		Whitebox.setInternalState(stringAdvertismentDataSource, "internalData", list);
		
		stringAdvertismentDataSource.iterator().remove();
	}
	
	@Test
	public void testNotEmptyIterator(){
		List<String> list = Arrays.asList("www.link1.pl", "www.link2.pl", "www.link3.pl");
		Whitebox.setInternalState(stringAdvertismentDataSource, "internalData", list);
		
		AdvertismentsDataSource<String> spy = spy(stringAdvertismentDataSource);
				
		assertNotNull(spy.iterator());
		Iterator<String> iterator = spy.iterator();
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
		List<String> list = Arrays.asList("www.gratka.pl/1/2/3", "www.gratka.pl/5/6/7", "http://www.gratka.pl/789");
		Whitebox.setInternalState(stringAdvertismentDataSource, "internalData", list);
		
		assertNotNull(stringAdvertismentDataSource.getSources());
		assertEquals(list.size(), stringAdvertismentDataSource.getSources().size());
		
		for (String item : list) {
			assertTrue(stringAdvertismentDataSource.getSources().contains(item));
		}
		
		//testing immutability	
		stringAdvertismentDataSource.getSources().add(null);
	}
}


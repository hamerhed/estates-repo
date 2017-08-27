package pl.hamerhed.scrapper.persistance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import pl.hamerhed.DataJpaTest;
import pl.hamerhed.domain.ExtractionSource;
import pl.hamerhed.domain.ExtractionSourceType;

public class ExtractionSourceRepositoryTest extends DataJpaTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private ExtractionSourceRepository extractionSourceRepository;

	private List<ExtractionSource> items;
	
	@Test
	public void testSaveEmptyCollection(){
		Iterable<ExtractionSource> result = extractionSourceRepository.save(new ArrayList<>());
		assertNotNull(result);
		assertFalse(result.iterator().hasNext());
	}
	
	@Test
	public void testSaveCollection(){
		ExtractionSource s1 = new ExtractionSource();
		s1.setContent("test");
		s1.setOriginalLink("www.link.pl");
		s1.setProcessedTimestamp(new Date());
		s1.setType(ExtractionSourceType.OFFERS_ITEMS_LIST_SOURCE);
		
		ExtractionSource s2 = new ExtractionSource();
		s2.setContent("test");
		s2.setOriginalLink("www.item.pl");
		s2.setProcessedTimestamp(new Date());
		s2.setType(ExtractionSourceType.OFFERS_ITEM_SOURCE);
		
		
		Iterable<ExtractionSource> result = extractionSourceRepository.save(Arrays.asList(s1, s2));
		List<ExtractionSource> list = StreamSupport.stream(result.spliterator(), false).collect(Collectors.toList());
		assertEquals(2, list.size());
		
		assertEquals(1, list.stream().filter(x -> x.getType() == ExtractionSourceType.OFFERS_ITEM_SOURCE).count());
		assertEquals(1, list.stream().filter(x -> x.getType() == ExtractionSourceType.OFFERS_ITEMS_LIST_SOURCE).count());
		
		assertEquals(1, list.stream().filter(x -> x.getOriginalLink().equals("www.link.pl")).count());
		assertEquals(1, list.stream().filter(x -> x.getOriginalLink().equals("www.item.pl")).count());
		
		Iterable<ExtractionSource> readed = extractionSourceRepository.findAll();
		List<ExtractionSource> readedList = StreamSupport.stream(readed.spliterator(), false).collect(Collectors.toList());
		
		assertEquals(2, readedList.size());
		
		assertEquals(1, readedList.stream().filter(x -> x.getType() == ExtractionSourceType.OFFERS_ITEM_SOURCE).count());
		assertEquals(1, readedList.stream().filter(x -> x.getType() == ExtractionSourceType.OFFERS_ITEMS_LIST_SOURCE).count());
		
		assertEquals(2, readedList.stream().filter(x -> x.getContent().equals("test")).count());
	}
}

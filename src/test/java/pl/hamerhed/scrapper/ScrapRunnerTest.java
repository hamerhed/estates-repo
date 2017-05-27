package pl.hamerhed.scrapper;

import org.assertj.core.api.Fail;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import junit.framework.TestCase;
import pl.hamerhed.TestConfig;
import pl.hamerhed.datasource.AdvertismentsDataSource;
import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.scrapper.ScrapRunner;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
@ActiveProfiles(profiles="test")

public class ScrapRunnerTest {
	@Autowired
	@InjectMocks
	private ScrapRunner scrapRunner;
	
	@Mock
	private AdvertismentsDataSource<String> dataSource;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Test
	public void testGetEmptyLinksFromRepo(){
		ScrapRunner spy = PowerMockito.spy(scrapRunner);
		when(dataSource.iterator()).thenReturn(new ArrayList<String>().iterator());
		
		try {
			spy.parse();
			assertEquals(0, spy.getItems().size());
			verify(dataSource, times(1)).iterator();
		} catch (IOException e) {
			fail("should not happened");
			e.printStackTrace();
		}
	}
	
	
}

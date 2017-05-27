package pl.hamerhed;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest(showSql=true)
@SpringBootTest(classes=TestConfig.class)
@ActiveProfiles(profiles="test")
@Ignore
public class DataJpaTest extends TestCase {
	public DataJpaTest(){
		super();
	}

}

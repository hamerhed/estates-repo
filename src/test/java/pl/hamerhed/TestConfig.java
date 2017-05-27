package pl.hamerhed;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import pl.hamerhed.scrapper.persistance.AdvertismentRepository;

@SpringBootApplication //to dziala dla klasy testowej ale jest bez sensu
@TestConfiguration
@EntityScan(basePackages="pl.hamerhed")
@Profile({"test"})
public class TestConfig {
	
	/*@Bean
	@Primary
	public AdvertismentRepository advertismentRepository(){
		System.out.println("pobieram mojego mocka");
		return Mockito.mock(AdvertismentRepository.class);
	}*/
}


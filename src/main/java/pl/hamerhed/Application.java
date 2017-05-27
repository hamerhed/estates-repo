package pl.hamerhed;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.scrapper.ScrapRunner;


@SpringBootApplication //equivalent to  @Configuration, @EnableAutoConfiguration and @ComponentScan
@EnableScheduling
@Profile("!test")
public class Application implements CommandLineRunner {

	@Autowired
	private Environment env;
	
	@Autowired
	private pl.hamerhed.scrapper.controller.AdvertismentSaver advertismentSaver;
	
	@Autowired
	private ScrapRunner scrapRunner;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	
	private void printClasspath(){
		System.out.println("print classpath");
		
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
        	System.out.println(url.getFile());
        }
	}
	
	
	@Override
	public void run(String... args) throws Exception {
		run();
	}
	
	//wstepnie ustawione na 5 minut in miliseconds
	@Scheduled(fixedRate=600000)
	private void run() throws Exception {

		scrapRunner.parse();
		
		Collection<Advertisment> items = scrapRunner.getItems();
		System.out.println("mam tyle nieruchomosci " + items.size());
		
		Collection<Advertisment> processedVersions = advertismentSaver.processVersions(items);
		System.out.println("po przetworzeniu " + processedVersions.size());
		Optional<Iterable<Advertisment>> savedItems = advertismentSaver.save(Optional.ofNullable(processedVersions));
		System.out.println("po zapisie ");
		if(processedVersions.size() == countSavedItems(savedItems.get())){
			System.out.println("Zapisano do bazy " + processedVersions.size() + "elementów");
		}
		//saveTestData();
	}
	
	private <T> int countSavedItems(Iterable<T> items){
		if(items == null ) return 0;
		int count = 0;
		Iterator<T> iter = items.iterator();
		while(iter.hasNext()){
			iter.next();
			count++;
		}
		return count;
	}
}

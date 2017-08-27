package pl.hamerhed;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.domain.ExtractionSource;
import pl.hamerhed.domain.IExtractionSource;
import pl.hamerhed.scrapper.ScrapRunner;
import pl.hamerhed.scrapper.persistance.ExtractionSourceRepository;


@SpringBootApplication //equivalent to  @Configuration, @EnableAutoConfiguration and @ComponentScan
@EnableScheduling
@Profile("!test")
public class Application implements CommandLineRunner {

	@Autowired
	private Environment env;
	
	@Autowired
	private pl.hamerhed.scrapper.controller.AdvertismentSaver advertismentSaver;
	
	@Autowired
	private ExtractionSourceRepository extractionSourceRepository;
		
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
	//@Scheduled(cron="30 22 * * * *")
	private void run() throws Exception {
		System.out.println("moja aplikacja dziala");
		boolean flag = false;
		if(flag){
			return;
		}
		//StreamSupport.stream(extractionSourceRepository.findAll().spliterator(), false).forEach(x -> {System.out.println("wypisz content"); System.out.println(x.getContent().substring(0 ,100));});
		scrapRunner.parse();
		
		Collection<Advertisment> items = scrapRunner.getItems();
		System.out.println("mam tyle nieruchomosci " + items.size());
		
		Collection<Advertisment> processedVersions = advertismentSaver.processVersions(items);
		System.out.println("po przetworzeniu " + processedVersions.size());
		
		Collection<ExtractionSource> extractionSources = scrapRunner.getExtractionSources();
		
		extractionSourceRepository.save(extractionSources);
		
		Optional<Iterable<Advertisment>> savedItems = advertismentSaver.save(Optional.ofNullable(processedVersions));
		System.out.println("po zapisie ");
		System.out.println("wyekstrachowano " + processedVersions.size() + " Zapisano do bazy " + countSavedItems(savedItems.get()) + "elementów");
		
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

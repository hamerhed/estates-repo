package pl.hamerhed.scrapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import pl.hamerhed.domain.ExtractionSource;
import pl.hamerhed.domain.IExtractionSource;

public abstract class AbstractListDataExtractor<T> extends AbstractExtractor<ExtractionSource> implements ListDataExtractor<T, ExtractionSource> {
	private static final Logger clog = Logger.getLogger(AbstractListDataExtractor.class);
	
	protected AbstractListDataExtractor() {
		
	}
	
	@Override
	public abstract void parse(String link) throws Exception;

	@Override
	public abstract Integer getPagesNumber();

	@Override
	public abstract Collection<T> getItems();
	
	/*protected Date convertStringToDate(String strDate) {
		Calendar cal = Calendar.getInstance();
		//clog.debug("sss " +strDate);
		String val = strDate.trim();
		switch(val){
			case "dzisiaj":
				break;
			case "wczoraj":
				cal.add(Calendar.DAY_OF_YEAR, -1);
				break;
			case "więcej niż miesiąc temu":
				cal.add(Calendar.MONTH, -1);
				break;
			case "w ciągu ostatnich dwóch tygodni":
				cal.add(Calendar.DAY_OF_YEAR, -14);
				break;
			case "w tym tygodniu":
				cal.add(Calendar.DAY_OF_YEAR, -7);
				break;
			case "w tym miesiącu":
				cal.set(Calendar.DAY_OF_MONTH, 1);
			case "przedwczoraj":
				cal.add(Calendar.DAY_OF_YEAR, -2);
				break;
			default:
				throw new IllegalArgumentException("nie mam takiej daty: " + strDate);
				
		}
	
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}*/
	
	/*public class LocalizationExtractionResult {
		private List<IRegion> locations = new ArrayList<>();
		private List<String> problems = new ArrayList<>();
		
		public void addLocation(IRegion region){
			locations.add(region);
		}
		
		public void addProblem(String problem){
			problems.add(problem);
		}
		
		public List<IRegion> getLocations(){
			return locations;
		}
		
		public List<String> getProblems(){
			return problems;
		}
		
	}*/
}

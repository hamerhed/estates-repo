package pl.hamerhed.scrapper;

/**
 * @author hamerhed
 * Interface used to parsing offer item
 * @param <T> type returned as original parsed object mainly for nosql purposes
 * @param <R> type of objects being returned as original content of processed input
 */
public interface ItemExtractor<T, R> extends IExtractor<R>{
	public void parse(String url, T item) throws Exception;
	
}

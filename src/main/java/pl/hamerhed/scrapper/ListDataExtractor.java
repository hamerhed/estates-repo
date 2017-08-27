package pl.hamerhed.scrapper;

import java.util.Collection;

/**
 * @author hamerhed
 *
 * @param <T> Type of objects which will be processed
 */
public interface ListDataExtractor<T, R> extends IExtractor<R>{
	public void parse(String link) throws Exception;
	public Integer getPagesNumber();
	public Collection<T> getItems();
}

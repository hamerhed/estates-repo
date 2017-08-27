package pl.hamerhed.scrapper;

/**
 * @author hamerhed
 * Base interface for extractors
 * @param <R> type of object containing parsed content information
 */
public interface IExtractor<R> {
	public R getExtractedSources();
}

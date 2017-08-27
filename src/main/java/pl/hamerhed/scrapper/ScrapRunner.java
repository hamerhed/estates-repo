package pl.hamerhed.scrapper;

import java.util.Collection;

import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.domain.ExtractionSource;
import pl.hamerhed.domain.IExtractionSource;

public interface ScrapRunner {
	public void parse() throws Exception;
	public Collection<Advertisment> getItems();
	public Collection<ExtractionSource> getExtractionSources();
}

package pl.hamerhed.scrapper;

import java.io.IOException;
import java.util.Collection;

import pl.hamerhed.domain.Advertisment;

public interface ScrapRunner {
	public void parse() throws IOException;
	public Collection<Advertisment> getItems();
}

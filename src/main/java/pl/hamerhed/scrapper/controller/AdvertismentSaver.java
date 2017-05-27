package pl.hamerhed.scrapper.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pl.hamerhed.domain.Advertisment;

@Service
public interface AdvertismentSaver {
	public Collection<Advertisment> processVersions(Collection<Advertisment> advertisments) throws Exception;
	
	public Optional<Iterable<Advertisment>> save(Optional<Collection<Advertisment>> itemsToSave) throws Exception;
}

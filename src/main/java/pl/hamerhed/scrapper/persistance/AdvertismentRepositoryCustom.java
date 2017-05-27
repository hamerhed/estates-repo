package pl.hamerhed.scrapper.persistance;

import java.util.Optional;

import javax.persistence.NonUniqueResultException;

import pl.hamerhed.domain.Advertisment;

interface AdvertismentRepositoryCustom {
	public Optional<Advertisment> findbyOriginalAdvertismentIdAndLink(String id, String link) throws NonUniqueResultException;
	
}

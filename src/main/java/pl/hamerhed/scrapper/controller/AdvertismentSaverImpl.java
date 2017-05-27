package pl.hamerhed.scrapper.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.domain.Estate;
import pl.hamerhed.scrapper.persistance.AdvertismentRepository;

@Service
public class AdvertismentSaverImpl implements AdvertismentSaver {

	private AdvertismentRepository advertismentRepo;
	
	protected AdvertismentSaverImpl() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	public AdvertismentSaverImpl(AdvertismentRepository advertismentRepository) {
		this.advertismentRepo = advertismentRepository;
	}
	
	@Override
	public Collection<Advertisment> processVersions(Collection<Advertisment> estateToSave) throws Exception {
		if(estateToSave == null || estateToSave.size() == 0) return Collections.emptyList();
		
		List<Advertisment> result = new ArrayList<>();
		for(Iterator<Advertisment> iter = estateToSave.iterator(); iter.hasNext(); ){
			Advertisment ad = iter.next();
			Optional<Advertisment> saved = advertismentRepo.findbyOriginalAdvertismentIdAndLink(ad.getAdvertismentId(), ad.getLink());
			if(saved.isPresent()){
				Advertisment savedAd = saved.get();
				//System.out.println("to dostalem czy z mocka " + savedAd.getAdvertismentId() + " " + savedAd.getLink());
				if(savedAd.isNewVersion(ad)){
					System.out.println("dodaj to to istniejacego");
					savedAd.addEstateVersion(ad.getFirstEstateVersion());
					result.add(savedAd);
				}
			} else {
				System.out.println("dodaj jako nowy item");
				result.add(ad);
			}
		}
		return result;
	}

	@Override
	public Optional<Iterable<Advertisment>> save(Optional<Collection<Advertisment>> itemsToSave) throws Exception {
		Iterable<Advertisment> result = null;
		if(itemsToSave.isPresent()){
			Collection<Advertisment> items = itemsToSave.get();
			result = advertismentRepo.save(items);
		}
		return Optional.ofNullable(result);
		
	}


	
	
	
}

package pl.hamerhed.datasource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.springframework.stereotype.Service;

@Service
class StringAdvertismentDataSourceImpl implements AdvertismentsDataSource<String> {
	private Collection<String> internalData;

	public StringAdvertismentDataSourceImpl() {
			internalData = Arrays.asList("http://dom.gratka.pl/mieszkania-sprzedam/lista/wielkopolskie,poznan,rataje,60,80,3,6,3,dz,mo,md,lpo,ld,pd.html");
	}
	
	
	@Override
	public Iterator<String> iterator() {
		return Collections.unmodifiableCollection(internalData).iterator();
	}

	@Override
	public Collection<String> getSources() {
		return Collections.unmodifiableCollection(internalData);
	}
	
}

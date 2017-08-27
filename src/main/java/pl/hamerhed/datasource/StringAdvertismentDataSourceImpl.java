package pl.hamerhed.datasource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.springframework.stereotype.Service;

import pl.hamerhed.domain.AddressLink;

@Service
class StringAdvertismentDataSourceImpl implements AdvertismentsDataSource<AddressLink> {
	private Collection<AddressLink> internalData;

	public StringAdvertismentDataSourceImpl() {
			internalData = Arrays.asList(new AddressLink("http://dom.gratka.pl/mieszkania-sprzedam/lista/wielkopolskie,poznan,rataje,40,:pageIndex:,60,80,3,6,3,dz,li,s,mo,md,lpo,ld,pd.html", null, new String[]{":pageIndex:"}));
	}
	
	
	@Override
	public Iterator<AddressLink> iterator() {
		return Collections.unmodifiableCollection(internalData).iterator();
	}

	@Override
	public Collection<AddressLink> getSources() {
		return Collections.unmodifiableCollection(internalData);
	}
	
}

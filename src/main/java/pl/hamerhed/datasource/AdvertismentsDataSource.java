package pl.hamerhed.datasource;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.stereotype.Service;

@Service
public interface AdvertismentsDataSource<T> {
	public Iterator<T> iterator();
	public Collection<T> getSources();
	
}

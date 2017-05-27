package pl.hamerhed.scrapper.persistance;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pl.hamerhed.domain.Advertisment;

public class AdvertismentRepositoryImpl implements AdvertismentRepositoryCustom {

	protected AdvertismentRepositoryImpl() {
	
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Optional<Advertisment> findbyOriginalAdvertismentIdAndLink(String id, String link) throws NonUniqueResultException {
		String sql = "select c from Advertisment c where c.advertismentId = :ad_id and c.link = :link";
		Query query = em.createQuery(sql);
		query.setParameter("ad_id", id);
		query.setParameter("link", link);
		
		Advertisment result = null;
		try {
			result = (Advertisment) query.getSingleResult();
		} catch (NoResultException e) {}
		
		return Optional.ofNullable(result);
	}

}

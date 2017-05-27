package pl.hamerhed.scrapper.persistance;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.domain.Estate;

@Repository
public interface AdvertismentRepository extends CrudRepository<Advertisment, UUID>, AdvertismentRepositoryCustom {

}

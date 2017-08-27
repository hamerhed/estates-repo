package pl.hamerhed.scrapper.persistance;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.hamerhed.domain.ExtractionSource;

@Repository
public interface ExtractionSourceRepository extends CrudRepository<ExtractionSource, UUID> {

}

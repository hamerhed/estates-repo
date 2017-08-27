package pl.hamerhed.domain;

import java.util.Date;

public interface IPersistable<ID> {
	//TODO wprowadzic type Id bo nie wiadomo co bedzie ostatecznie stosowane
	//przeniesc do interfejsu nadrzednego
	ID getId();
	void setId(ID id);

	Date getCreationDate();
	
	
}

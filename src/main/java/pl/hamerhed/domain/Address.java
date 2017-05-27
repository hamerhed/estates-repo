package pl.hamerhed.domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.GeneratorType;

@Entity
//@SequenceGenerator(name="ADDRESS_SEQ", sequenceName="ADDRESS_SEQ", allocationSize=1, initialValue=1)
public class Address extends AbstractBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	//address
	@Column(nullable=false)
	private String city;
	@Column(nullable=false)
	private String district;
	@Column(nullable=false)
	private String street;
	@Column(nullable=false)
	private String voivodine;
	
	public Address(){
		super();
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getVoivodine() {
		return voivodine;
	}

	public void setVoivodine(String voivodine) {
		this.voivodine = voivodine;
	}

	/*@Override
	public int hashCode() {
		return getCity().hashCode() + getDistrict().hashCode()
			   + getStreet().hashCode() + getVoivodine().hashCode();
	}
 
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;

		if (!(obj instanceof Address)) {
			return false;
		}
		Address other = (Address) obj;
		return getCity().equals(other.getCity()) && getDistrict().equals(other.getDistrict())
				&& getStreet().equals(other.getStreet()) && getVoivodine().equals(other.getVoivodine());
	}*/
}

package pl.hamerhed.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.property.access.spi.GetterMethodImpl;

@Entity
public class Advertisment extends AbstractBaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//advertisment information
	@Column(unique=true, nullable=false, name="advertisment_id")
	private String advertismentId;
	
	@Column(nullable=false, name="market_type")
	private String marketType;
	
	@Column(length=1024, nullable=false)
	private String link;
	
	@Column(nullable=false, name="addition_date")
	private Date additionDate;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="advertisment", cascade=CascadeType.ALL)
	@OrderBy("modification_date ASC")
	private List<Estate> estateVersions = new ArrayList<Estate>();
	
	public Advertisment(){}
	
	public String getAdvertismentId() {
		return advertismentId;
	}


	public void setAdvertismentId(String advertismentId) {
		this.advertismentId = advertismentId;
	}


	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public String getMarketType() {
		return marketType;
	}

	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}

	public Date getAdditionDate() {
		return additionDate;
	}

	public void setAdditionDate(Date additionDate) {
		this.additionDate = additionDate;
	}

	public List<Estate> getEstateVersions() {
		return estateVersions;
	}

	private void setEstateVersions(List<Estate> estateVersions) {
		this.estateVersions = estateVersions;
	}
	
	public void addEstateVersion(Estate estate){
		this.estateVersions.add(estate);
		estate.setAdvertisment(this);
	}
	
	public Estate getFirstEstateVersion(){
		if(estateVersions == null || estateVersions.size() == 0) return null;
		return estateVersions.get(0);
	}
	
	public Estate getLastEstateVersion(){
		if(estateVersions == null || estateVersions.size() == 0) return null;
		return estateVersions.get(estateVersions.size()-1);
	}
	
	public boolean isNewVersion(Advertisment newAd){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(newAd.getFirstEstateVersion().getModificationDate().getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		
		if(cal.before(today))
				return false;
		
		System.out.println("added element calendar=" + cal.getTime());
		this.getEstateVersions().stream().forEach(s-> {System.out.println(" to item w db " + s.getModificationDate() + " is eq " + (s.getModificationDate().compareTo(cal.getTime()) == 0));});
		long todayVersions = this.getEstateVersions().stream().filter(s-> s.getModificationDate().compareTo(cal.getTime()) == 0).count();
		System.out.println(" po filtrowaniu mam " + todayVersions);
		if(todayVersions > 0)
			return false;
		
		return true;
	}
}

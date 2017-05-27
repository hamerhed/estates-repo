package pl.hamerhed.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Building extends AbstractBaseEntity {
	
	//building information
	@Column(name="floors_number")
	private Integer floorsNumber;
	@Column(name="building_type")
	private String buildingType;
	@Column(name="construction_material")
	private String constructionMaterial;
	@Column(name="build_year")
	private Date buildYear;

	public Building(){}
	
	public Integer getFloorsNumber() {
		return floorsNumber;
	}

	public void setFloorsNumber(Integer floorsNumber) {
		this.floorsNumber = floorsNumber;
	}
	
	public String getConstructionMaterial() {
		return constructionMaterial;
	}

	public void setConstructionMaterial(String constructionMaterial) {
		this.constructionMaterial = constructionMaterial;
	}

	public Date getBuildYear() {
		return buildYear;
	}

	public void setBuildYear(String buildYear) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy");
		Date date = df.parse(buildYear);
		this.buildYear = date;
	}
	
	public void setBuildYear(Date buildYear) {
		this.buildYear = buildYear;
	}

	public String getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}
}

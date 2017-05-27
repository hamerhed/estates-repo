package pl.hamerhed.domain;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "estate")
public class Estate extends AbstractBaseEntity {
	
		@OneToOne(cascade=CascadeType.ALL, optional=false, orphanRemoval=true, fetch=FetchType.EAGER)
		@JoinColumn(name="address_id")
		private Address address;
							
		//flat information
		@Column(nullable=false, name="modification_date")
		private Date modificationDate;
		@Column(nullable=false)
		private BigDecimal price;
		
		private String propertyForm;
		private String estateType;
		private Integer floor;
		private Float area;
		private Integer roomsNumber;
		private String flatLevels;
		@Column(length=1024)
		private String flatCondition;
		private String flatNoiseLevel;
		private String pipesCondition;
		private String flatWindows;
		private String carPlace;
		private String flatAdditions;
		@Column(length=10000)
		private String flatDescription;
		private String kitchen;
		private String bathroom;
		private String bathroomCondition;
		private String additionalArea;
		private String installationState;
		private String northOrientation;
		
		@OneToOne(cascade=CascadeType.ALL, optional=false, orphanRemoval=true)
		@JoinColumn(name="building_id")
		private Building building;
		
		@ManyToOne
		@JoinColumn(name="advertisment_id")
	    private Advertisment advertisment;
		
		public Estate(){}

		public Integer getFloor() {
			return floor;
		}

		public void setFloor(Integer floor) {
			this.floor = floor;
		}

		public Float getArea() {
			return area;
		}

		public void setArea(Float area) {
			this.area = area;
		}

		public Integer getRoomsNumber() {
			return roomsNumber;
		}

		public void setRoomsNumber(Integer roomsNumber) {
			this.roomsNumber = roomsNumber;
		}

		public String getFlatLevels() {
			return flatLevels;
		}

		public void setFlatLevels(String flatLevels) {
			this.flatLevels = flatLevels;
		}

		public String getFlatCondition() {
			return flatCondition;
		}

		public void setFlatCondition(String flatCondition) {
			this.flatCondition = flatCondition;
		}

		public String getFlatNoiseLevel() {
			return flatNoiseLevel;
		}

		public void setFlatNoiseLevel(String flatNoiseLevel) {
			this.flatNoiseLevel = flatNoiseLevel;
		}

		public String getPipesCondition() {
			return pipesCondition;
		}

		public void setPipesCondition(String pipesCondition) {
			this.pipesCondition = pipesCondition;
		}

		public String getFlatWindows() {
			return flatWindows;
		}

		public void setFlatWindows(String flatWindows) {
			this.flatWindows = flatWindows;
		}

		public String getCarPlace() {
			return carPlace;
		}

		public void setCarPlace(String carPlace) {
			this.carPlace = carPlace;
		}

		public String getFlatAdditions() {
			return flatAdditions;
		}

		public void setFlatAdditions(String flatAdditions) {
			this.flatAdditions = flatAdditions;
		}

		public String getFlatDescription() {
			return flatDescription;
		}

		public void setFlatDescription(String flatDescription) {
			this.flatDescription = flatDescription;
		}

		public Date getModificationDate() {
			return modificationDate;
		}

		public void setModificationDate(Date modificationDate) {
			this.modificationDate = modificationDate;
		}

		public String getEstateType() {
			return estateType;
		}

		public void setEstateType(String estateType) {
			this.estateType = estateType;
		}

		public String getPropertyForm() {
			return propertyForm;
		}

		public void setPropertyForm(String propertyForm) {
			this.propertyForm = propertyForm;
		}

		public String getKitchen() {
			return kitchen;
		}

		public void setKitchen(String kitchen) {
			this.kitchen = kitchen;
		}

		public String getBathroom() {
			return bathroom;
		}

		public void setBathroom(String bathroom) {
			this.bathroom = bathroom;
		}

		public String getBathroomCondition() {
			return bathroomCondition;
		}

		public void setBathroomCondition(String bathroomCondition) {
			this.bathroomCondition = bathroomCondition;
		}

		public String getAdditionalArea() {
			return additionalArea;
		}

		public void setAdditionalArea(String additionalArea) {
			this.additionalArea = additionalArea;
		}

		public String getInstallationState() {
			return installationState;
		}

		public void setInstallationState(String installationState) {
			this.installationState = installationState;
		}

		public String getNorthOrientation() {
			return northOrientation;
		}

		public void setNorthOrientation(String northOrientation) {
			this.northOrientation = northOrientation;
		}


		public BigDecimal getPrice() {
			return price;
		}


		public void setPrice(BigDecimal price) {
			this.price = price;
		}


		public Address getAddress() {
			return address;
		}


		public void setAddress(Address address) {
			this.address = address;
		}


		public Building getBuilding() {
			return building;
		}

		public void setBuilding(Building building) {
			this.building = building;
		}

		public Advertisment getAdvertisment() {
			return advertisment;
		}

		public void setAdvertisment(Advertisment advertisment) {
			this.advertisment = advertisment;
		}
		
		
		
}


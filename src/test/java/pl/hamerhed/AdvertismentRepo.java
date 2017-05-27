package pl.hamerhed;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.hamerhed.domain.Address;
import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.domain.Building;
import pl.hamerhed.domain.Estate;

public class AdvertismentRepo {
	public static List<Advertisment> createAdvertisments() throws ParseException {
		Address addr1 = new Address();
		addr1.setCity("poznań");
		addr1.setDistrict("rataje");
		addr1.setStreet("os. Polan 3/3");
		addr1.setVoivodine("wielkopolskie");
		
		Building building = new Building();
		building.setBuildingType("blok");
		building.setBuildYear("2001");
		building.setConstructionMaterial("cegła");
				
		Estate est1 = new Estate();
		est1.setAdditionalArea("test");
		est1.setAddress(addr1);
		est1.setArea(65.5f);
		est1.setBathroom("jest");
		est1.setBathroomCondition("dobra");
		est1.setBuilding(building);
		est1.setCarPlace("brak");
		est1.setEstateType("mieszkanie");
		est1.setFlatAdditions("nie ma");
		est1.setFlatCondition("super wąski");
		est1.setFlatDescription("opis mieszkania");
		est1.setFlatLevels("1");
		est1.setFlatNoiseLevel("ciche");
		est1.setFlatWindows("plastikowe");
		est1.setFloor(4);
		est1.setInstallationState("dobry");
		est1.setKitchen("jasna");
		est1.setModificationDate(createDate("2017-04-01"));
		est1.setNorthOrientation("pólnoc");
		est1.setPipesCondition("nowe");
		est1.setPrice(new BigDecimal(320000));
		est1.setPropertyForm("własność");
		est1.setRoomsNumber(3);

		Advertisment ad1 = new Advertisment();
		System.out.println("adver4 est id " + est1.getId());
		ad1.setAdditionDate(createDate("2017-04-01"));
		ad1.setAdvertismentId("1");
		ad1.setLink("www.poznan.pl/link1/");
		ad1.setMarketType("wtórny");
		ad1.addEstateVersion(est1);
		
		//estate2
		Address addr2 = new Address();
		addr2.setCity("poznań");
		addr2.setDistrict("jeżyce");
		addr2.setStreet("ul. słowackiego");
		addr2.setVoivodine("wielkopolskie");
		
		Building building2 = new Building();
		building2.setBuildingType("kamienica");
		building2.setBuildYear("1950");
		building2.setConstructionMaterial("cegła");
				
		Estate est2 = new Estate();
		est2.setAdditionalArea("taras");
		est2.setAddress(addr2);
		est2.setArea(99f);
		est2.setBathroom("jest");
		est2.setBathroomCondition("dobra");
		est2.setBuilding(building2);
		est2.setCarPlace("brak");
		est2.setEstateType("mieszkanie");
		est2.setFlatAdditions("nie ma");
		est2.setFlatCondition("super wąski");
		est2.setFlatDescription("opis mieszkania");
		est2.setFlatLevels("1");
		est2.setFlatNoiseLevel("ciche");
		est2.setFlatWindows("plastikowe");
		est2.setFloor(2);
		est2.setInstallationState("dobry");
		est2.setKitchen("jasna");
		est2.setModificationDate(createDate("2017-04-20"));
		est2.setNorthOrientation("pólnoc");
		est2.setPipesCondition("nowe");
		est2.setPrice(new BigDecimal(450000));
		est2.setPropertyForm("własność");
		est2.setRoomsNumber(4);

		Advertisment ad2 = new Advertisment();
		ad2.setAdditionDate(createDate("2017-04-20"));
		ad2.setAdvertismentId("2");
		ad2.setLink("www.poznan.pl/link2/");
		ad2.setMarketType("wtórny");
		ad2.addEstateVersion(est2);
		
		//estate3 update to estate2
		Address addr3 = new Address();
		addr3.setCity("poznań");
		addr3.setDistrict("jeżyce");
		addr3.setStreet("ul. słowackiego");
		addr3.setVoivodine("wielkopolskie");
				
		Building building3 = new Building();
		building3.setBuildingType("kamienica");
		building3.setBuildYear("1950");
		building3.setConstructionMaterial("cegła");
					
		Estate est3 = new Estate();
		est3.setAdditionalArea("taras");
		est3.setAddress(addr3);
		est3.setArea(99f);
		est3.setBathroom("jest");
		est3.setBathroomCondition("dobra");
		est3.setBuilding(building3);
		est3.setCarPlace("brak");
		est3.setEstateType("mieszkanie");
		est3.setFlatAdditions("nie ma");
		est3.setFlatCondition("super wąski");
		est3.setFlatDescription("opis mieszkania");
		est3.setFlatLevels("1");
		est3.setFlatNoiseLevel("ciche");
		est3.setFlatWindows("plastikowe");
		est3.setFloor(2);
		est3.setInstallationState("dobry");
		est3.setKitchen("jasna");
		//modyfikowany dzisiaj
		est3.setModificationDate(new Date());
		est3.setNorthOrientation("pólnoc");
		est3.setPipesCondition("nowe");
		est3.setPrice(new BigDecimal(400000));
		est3.setPropertyForm("własność");
		est3.setRoomsNumber(4);
				
		Advertisment ad3 = new Advertisment();
		ad3.setAdditionDate(createDate("2017-04-01"));
		ad3.setAdvertismentId("2");
		ad3.setLink("www.poznan.pl/link2/");
		ad3.setMarketType("wtórny");
		ad3.addEstateVersion(est3);
		
		//estate4 version for est1
		Address addr4 = new Address();
		addr4.setCity("poznań");
		addr4.setDistrict("rataje");
		addr4.setStreet("os. Polan 3/3");
		addr4.setVoivodine("wielkopolskie");
		
		Building building4 = new Building();
		building4.setBuildingType("blok");
		building4.setBuildYear("2001");
		building4.setConstructionMaterial("cegła");
				
		Estate est4 = new Estate();
		est4.setAdditionalArea("test");
		est4.setAddress(addr4);
		est4.setArea(65.5f);
		est4.setBathroom("jest");
		est4.setBathroomCondition("dobra");
		est4.setBuilding(building4);
		est4.setCarPlace("brak");
		est4.setEstateType("mieszkanie");
		est4.setFlatAdditions("nie ma");
		est4.setFlatCondition("super wąski");
		est4.setFlatDescription("opis mieszkania");
		est4.setFlatLevels("1");
		est4.setFlatNoiseLevel("ciche");
		est4.setFlatWindows("plastikowe");
		est4.setFloor(4);
		est4.setInstallationState("dobry");
		est4.setKitchen("jasna");
		//modyfikowany dzisiaj
		est4.setModificationDate(new Date());
		//est4.setModificationDate(createDate("2017-04-21"));
		est4.setNorthOrientation("pólnoc");
		est4.setPipesCondition("nowe");
		est4.setPrice(new BigDecimal(300000));
		est4.setPropertyForm("własność");
		est4.setRoomsNumber(3);

		Advertisment ad4 = new Advertisment();
		System.out.println("adver4 est id " + est4.getId());
		ad4.setAdditionDate(createDate("2017-04-01"));
		ad4.setAdvertismentId("1");
		ad4.setLink("www.poznan.pl/link1/");
		ad4.setMarketType("wtórny");
		ad4.addEstateVersion(est4);
		
		List<Advertisment> items = new ArrayList<Advertisment>();
		items.add(ad1);
		items.add(ad2);
		items.add(ad4); //version for est1
		items.add(ad3); //version for est2
		return items;
	}
	
	private static Date createDate(String date) throws ParseException{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.parse(date);
	}
}

package pl.hamerhed.scrapper;

import java.text.ParseException;
import java.util.Iterator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.hamerhed.domain.Building;
import pl.hamerhed.domain.Estate;



public class EstateItemExtractor {
	private static final int BUILDING_TYPE = 0;
	private static final int BUILDING_FLOORS = 1;
	private static final int BUILDING_MATERIAL = 2;
	private static final int BUILDING_YEAR = 3;
	private Document doc;
	
	private Estate flatItem;
	
	public EstateItemExtractor(Document root, Estate flat){
		doc = root;
		flatItem = flat;
	}
	
	public void extract(){
		//System.out.println("flat_item=" + doc);
		
		Elements flatInfo = doc.select("div[class=mieszkanie]").first().select("li");
		//System.out.println("flat-info=" + flatInfo);
		extractFlatInformation(flatInfo);
		
		Elements buildingInfo = doc.select("div[class=budynek]").first().select("li");
		//System.out.println("flat-info=" + buildingInfo);
		Building building = extractBuildingInformation(buildingInfo);
		flatItem.setBuilding(building);
		
		if(doc.select("div[class=garaz]").size() > 0){
			org.jsoup.nodes.Element carparkInfo = doc.select("div[class=garaz]").first().select("p").first();
			flatItem.setCarPlace(carparkInfo.text().trim());	
		}
		
		if(doc.select("div[class=dodatkowezalety]").size() > 0) {
			System.out.println("zalety=" + doc.select("div[class=dodatkowezalety]"));
			org.jsoup.nodes.Element additionalAssetsInfo = doc.select("div[class=dodatkowezalety]").first().select("p").first();
			//	System.out.println("flat-info=" + additionalAssetsInfo);
			flatItem.setFlatAdditions(additionalAssetsInfo.text().trim());
		}
		if(doc.select("div[class=opis]").size() > 0){
			org.jsoup.nodes.Element descriptionInfo = doc.select("div[class=opis]").first().select("p").first();
		//	System.out.println("flat-info=" + descriptionInfo.text());
			flatItem.setFlatDescription(descriptionInfo.text().trim());
		}
	}

	private Building extractBuildingInformation(Elements buildingInfo) {
		int i = 0;
		Building building = new Building();
		Iterator<Element> iter = buildingInfo.listIterator();
		while(iter.hasNext()){
			Element el = iter.next();
			String text = el.select("div[class=wartosc]").first().text().trim();
			
			switch(i){
				case BUILDING_TYPE:
					building.setBuildingType(text);
					break;
				case BUILDING_FLOORS:
					building.setFloorsNumber(Integer.parseInt(text));
					break;
				case BUILDING_MATERIAL:
					building.setConstructionMaterial(text);
					break;
				case BUILDING_YEAR:
				try {
					building.setBuildYear(text);
				} catch (ParseException e) {
					e.printStackTrace();
				}
					break;
			}
			i++;
		}
		return building;
	}

	private void extractFlatInformation(Elements flatInfo) {
		//System.out.println("flatinfo=" + flatInfo);
		Iterator<Element> flatIter = flatInfo.listIterator();
		while(flatIter.hasNext() ){
			Element el = flatIter.next();
			String text = el.select("div[class=wartosc]").first().text().trim();
			String key = el.select("span[class=label]").first().text().trim();	
			System.out.println("text= " + text + " key= " + key);

			switch(key.trim().toLowerCase()){
				case "forma własności":
					flatItem.setPropertyForm(text.trim());
					break;
				case "stan instalacji":
					flatItem.setInstallationState(text.trim());
					break;
				case "powierzchnia":
					text = text.replaceAll("m2", "").trim().replaceAll(",", ".");
					flatItem.setArea(Float.parseFloat(text));
					break;
				case "piętro":
					System.out.println("aaaa text= " + text);
					if("parter".equals(text.trim().toLowerCase())){
						flatItem.setFloor(0);
					} else {
						flatItem.setFloor(Integer.parseInt(text));
					}
					break;
				case "liczba pokoi":
					flatItem.setRoomsNumber(Integer.parseInt(text));
					break;
				case "liczba poziomów":
					flatItem.setFlatLevels(text);
					break;
				case "stan mieszkania":
					flatItem.setFlatCondition(text);
					break;
				case "głośność":
					flatItem.setFlatNoiseLevel(text);
					break;
				case "kuchnia":
					flatItem.setKitchen(text);
					break;
				case "łazienka":
					flatItem.setBathroom(text);
					break;
				case "stan łazienki":
					flatItem.setBathroomCondition(text);
					break;
				case "okna":
					flatItem.setFlatWindows(text);
					break;
				case "powierzchnia dodatkowa":
					flatItem.setAdditionalArea(text);
					break;
				case "usytuowanie wzgl. stron świata":
					flatItem.setNorthOrientation(text.trim());
					break;
				default:
					throw new IllegalStateException("Nie ma takiego parametru " + key);
			}
		}
	}
}

package pl.hamerhed.scrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import pl.hamerhed.domain.Address;
import pl.hamerhed.domain.Advertisment;
import pl.hamerhed.domain.Estate;
import pl.hamerhed.domain.ExtractionSource;
import pl.hamerhed.domain.ExtractionSourceType;
import pl.hamerhed.domain.IExtractionSource;

@Service
public class GratkaplListDataExtractorImpl extends AbstractListDataExtractor<Advertisment> 
										implements ListDataExtractor<Advertisment, ExtractionSource>{
	private static final int VIEWS_POSITION = 3;

	private static final int MODIFICATION_DATE_POSITION = 2;

	private static final int INSERTION_DATE_POSITION = 1;

	private static final int MARKET_POSITION = 0;

	private Document doc;
	
	private List<Advertisment> estates;
	
	private Integer pages;
	
	public GratkaplListDataExtractorImpl(){
		estates = new ArrayList<>();
	}
	
	protected GratkaplListDataExtractorImpl(Document document){
		this.doc = document;
		estates = new ArrayList<>();
	}
	
	public ExtractionSource getExtractedSource(){
		return extractionSources;
	}
	
/*	public void parse(Document document){
		this.doc = document;
		extractPagesNumber();
		
		extractEstates();
	}*/

	protected void init(){
		estates = new ArrayList<>();
		pages = null;
	}
	@Override
	public void parse(String link) throws Exception {
		init();
		this.doc = getDocumentFromLink(link);
		System.out.println(doc.outerHtml());
		this.extractionSources = createExtractionSource(doc.outerHtml(), link, ExtractionSourceType.OFFERS_ITEMS_LIST_SOURCE);
		extractPagesNumber();
		
		extractEstates();
		
	}

	@Override
	public Integer getPagesNumber() {
		return pages;
	}

	@Override
	public Collection<Advertisment> getItems() {
		return estates;
	}
	
/*	@Deprecated
	public Collection<Advertisment> getEstates(){
		return estates;
	}*/
	
	private void extractEstates() {
		if(doc == null) return;
		Elements items = doc.getElementsByAttributeValueMatching("id", Pattern.compile("lista-wiersz-*"));
		for(int i = 0; i < items.size(); i++){
			Element item = items.get(i);
			Advertisment estate = parseAdvertisment(item);
						
			estates.add(estate);
		}
		System.out.println("elements size =" + estates.size());
	}

	private void extractPagesNumber() {
		Elements navPagesSize = doc.getElementsByClass("stronicowanie");
		Element stronicowanie = navPagesSize.first();
		if(stronicowanie != null){
			Elements pages = stronicowanie.select("a[class=strona]");
			for(int i = 0; i < pages.size(); i++){
				this.pages = Integer.valueOf(pages.get(i).text());	
			}
		}
	}
	
	private String parseAdvertismentId(String link) {
		String[] sections = link.split("/");
		String temp = sections[sections.length - 1];
		
		String[] data =temp.split("-");
		return data[1].trim();
	}
	
	private Advertisment parseAdvertisment(Element root){
		//System.out.println("elem " + root);
		System.out.println("id " + getId(root));
		
		Advertisment ad = new Advertisment();
		String link = getLink(root);
		System.out.println("link=" + getLink(root));
		ad.setLink(link);
		String advertismentId = parseAdvertismentId(link);
		ad.setAdvertismentId(advertismentId);
		
		Estate estate = new Estate();
				
		System.out.println("price=" + getPrice(root));
		estate.setPrice(getPrice(root));
		Element info = root.select("div[class=ogloszenieInfo]").first();
		
		String title = info.getElementsByTag("h2").first().text();
		System.out.println("tytul=" + title);
		parseTypeAndAddress(title, estate);
		
		Elements flatData = info.select("p[class=infoDane]");
		
		System.out.println("infodane=" + flatData.text());
		FlatDataExtractor flatEtractor = new FlatDataExtractor(flatData.first());
		flatEtractor.parse();
		System.out.println("po parsie=" + flatEtractor.getFloor() + " " + flatEtractor.getBuildingFloorsNumber()
							+ " rooms=" + flatEtractor.getRoomsNumber() + " " + flatEtractor.getBuildYear()
							+ " area=" + flatEtractor.getArea()
							+ " price=" + flatEtractor.getPricePerMeter());
		
		//System.out.println("footer" + root.select("footer"));
		Element footer = root.select("footer").first();
		//System.out.println("dane= " + footer.select("li").not(".punkty"));
		
		Elements metadata = footer.select("li").not(".punkty");
		System.out.println("metadata="+metadata);
		System.out.println("metadata size=" + metadata.size());
		
		String market = getMarket(metadata);
		String insertDate = getInsertDate(metadata);
		String modificationDate = getModificationDate(metadata);
		String viewsNumber = "0";
		if(metadata.size() > 3){
			viewsNumber = getViewNumber(metadata);
		}
		
		System.out.println("metadane " + market + " " + insertDate + " " + modificationDate + " " + viewsNumber);
		estate.setModificationDate(convertStringToDate(modificationDate));
		
		ad.setMarketType(market);
		ad.setAdditionDate(convertStringToDate(insertDate));
		
		ad.addEstateVersion(estate);
		
		
 		return ad;
	}
	
	private Date convertStringToDate(String strDate) {
		Calendar cal = Calendar.getInstance();
		System.out.println("sss " +strDate);
		String val = strDate.trim();
		switch(val){
			case "dzisiaj":
				break;
			case "wczoraj":
				cal.add(Calendar.DAY_OF_YEAR, -1);
				break;
			case "więcej niż miesiąc temu":
				cal.add(Calendar.MONTH, -1);
				break;
			case "w ciągu ostatnich dwóch tygodni":
				cal.add(Calendar.DAY_OF_YEAR, -14);
				break;
			case "w tym tygodniu":
				cal.add(Calendar.DAY_OF_YEAR, -7);
				break;
			case "w tym miesiącu":
				cal.set(Calendar.DAY_OF_MONTH, 1);
			case "przedwczoraj":
				cal.add(Calendar.DAY_OF_YEAR, -2);
				break;
			default:
				throw new IllegalArgumentException("nie mam takiej daty: " + strDate);
				
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}
	
	private void parseTypeAndAddress(String data, Estate estate){
		String[] tab = data.split(" ");
		int offset = 0;
		String type = tab[0];
		if(tab[0].toLowerCase().equals("nowe")){
			 type = type + " " + tab[1];
			offset = 1;
		}
		
		String city = tab[1+offset];
		
		String[] temp = Arrays.copyOfRange(tab, 2+offset, tab.length);
		String tempStr = String.join(" ", temp);
		
		
		tab = tempStr.split(",");
		String street = tab[tab.length-1];
		String district = String.join(", ", Arrays.copyOfRange(tab, 0, tab.length-1)); 
		
		estate.setEstateType(type.trim());
/*		estate.getAddress().setCity(city.trim());
		estate.getAddress().setVoivodine("wielkopolskie");
		estate.getAddress().setDistrict(district.trim());
		estate.getAddress().setStreet(street.trim());*/
		
		System.out.println("xxxxxxxxxxx to  moj id " + estate.getId());
		
		Address address = new Address();
		address.setCity(city.replaceAll(",", "").trim());
		address.setVoivodine("wielkopolskie");
		address.setDistrict(district.trim());
		address.setStreet(street.trim());
		estate.setAddress(address);
		
		System.out.println("type " + type + " c " + city + " st " + street + " d " + district);
		
		
	}
	
	private String getLink(Element root){
		return root.select("li").first().select("a").first().absUrl("href");
	}
	
	private String getId(Element root){
		String text = root.attr("id");
		String[] tab = text.split("-");
		return tab[tab.length-1];
	}
	
	private BigDecimal getPrice(Element priceContainer){
		String text = priceContainer.select("p[class=price]").text();
		String priceStr = text.replaceAll("\\D+", "");
		return new BigDecimal(priceStr);
		
	}
	
	private String getListValue(Elements list, int index){
		String text = list.get(index).text();
		String[] tab = text.split(":");
		return tab[1].trim();
	}
	
	private String getMarket(Elements list){
		return getListValue(list, MARKET_POSITION);
	}
	
	private String getInsertDate(Elements list){
		return getListValue(list, INSERTION_DATE_POSITION);
	}
	
	private String getModificationDate(Elements list){
		return getListValue(list, MODIFICATION_DATE_POSITION);
	}
	
	private String getViewNumber(Elements list){
		return getListValue(list, VIEWS_POSITION);
	}
	
	public Integer getPageNumber(){
		return pages;
	}
	
	private class FlatDataExtractor {
		private Element root;
		
		private Integer roomsNumber;
		private Integer floor;
		private Integer buildingFloorsNumber;
		private String buildYear;
		private BigDecimal pricePerMeter;
		private Float area;
		
		public FlatDataExtractor(Element item){
			this.root = item;
		}
		
		public void parse(){
			String[] tab = root.text().trim().split(", ");
			roomsNumber = Integer.valueOf(tab[0].replaceAll("\\D+", "").trim());
			
			String floorStr = tab[1].trim();
			if("parter".equals(floorStr.toLowerCase().trim())){
				floor = 0;
			} else {
				String[] floors = floorStr.trim().split("\\s+|\\xA0");
				floor = Integer.valueOf(floors[0].trim());
				buildingFloorsNumber = Integer.valueOf(floors[3].trim());
			}
			int offset = 1;
			if(tab.length == 5){
				if(tab[tab.length-1].contains("dopłata")){
					//do nothing
				} else {
					buildYear = tab[2].replaceAll("\\D+", "").trim();
					System.out.println("build year =" + buildYear);
					offset = 0;
				}
			} else if(tab.length == 6){
				buildYear = tab[2].replaceAll("\\D+", "").trim();
				System.out.println("build year =" + buildYear);
				offset = 0;
			}
			
			pricePerMeter = extractPrice(tab[3-offset].trim());
			area = extractArea(tab[4-offset].trim());
		}

		public BigDecimal extractPrice(String text){
			String val = text.trim().substring(0, text.length()-2);
			val = val.replaceAll("\\D+", "");
			return new BigDecimal(val);
		}
		
		public Float extractArea(String text){
			String[] tab = text.trim().split(" ");
			return Float.valueOf(tab[0].replaceAll(",", "."));
		}
		
		public Integer getRoomsNumber() {
			return roomsNumber;
		}
		
		public Integer getFloor() {
			return floor;
		}

		
		public Integer getBuildingFloorsNumber() {
			return buildingFloorsNumber;
		}

		public String getBuildYear() {
			return buildYear;
		}

		public BigDecimal getPricePerMeter() {
			return pricePerMeter;
		}

		public Float getArea() {
			return area;
		}
		
		
	}
}

package pl.hamerhed.domain;

public class AddressLink {
	private String url;
	private String[] paramNames;
	
	private String last24HoursUrl;
	
	public AddressLink(String url, String last24HoursUrl, String[] paramNames){
			this.url = url;
			this.last24HoursUrl = last24HoursUrl;
			this.paramNames = paramNames;
	}
	
	public String updateLinkParams(String[] paramValues){
		return _updateLinkParams(url, paramValues);
	}
	
	public String updateLast24HoursLinkParams(String[] paramValues){
		return _updateLinkParams(last24HoursUrl, paramValues);
	}
	
	private String _updateLinkParams(String url, String[] paramValues){
		if(paramValues == null || paramValues.length != paramNames.length)
				throw new IllegalArgumentException("Values number need to be the same as params number");
		String myUrl = new String(url);
		for (int i = 0; i < paramValues.length; i++) {
			myUrl = myUrl.replaceAll(paramNames[i], paramValues[i]);
		}
		return myUrl;
	}
	
}

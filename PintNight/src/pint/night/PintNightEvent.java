package pint.night;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PintNightEvent {
	
private String date;
private String beer;
private String abv;
private String description;
private String notes;
private String url;
private Elements pint;

public PintNightEvent(Element pintNight){
	pint = pintNight.getElementsByTag("font");
    date = pint.get(0).text();
	beer = pint.get(2).text();
	abv = pint.get(4).text();
	description = pint.get(6).text();
	notes = pint.get(8).text();
	url = "<a href=\"" + "http://www.google.com/search?btnI=1&as_sitesearch=beeradvocate.com&q=" + beer + "\">" + "More Info" + "<a>";
}

public PintNightEvent(){
    date = "n/a";
	beer = "n/a";
	abv = "n/a";
	description = "n/a";
	notes = "n/a";
	url = "n/a";
}

public String toString(){
	//Occasionally the table on the PintNight website isn't fully populated
	//This results in a series of dashes, up to six
	//removing three, and then two and the replace the last one with newline
	//should take care of any possible number of dashes up to 6
	
	String invalidFieldsRemoved = pint.text().replace("- - - ", "");
	invalidFieldsRemoved = pint.text().replace("- - ", "");
	return invalidFieldsRemoved.replace("-", "\n  ");
	}

public String getMoreInfo(){
	return url;
}

public String getDate(){
	return date;
}

public String getBeer(){
	return beer;
}

public String getABV(){
	return abv;
}

public String getDescription(){
	return description;
}

public String getNotes(){
	return notes;
}
}

package edu.upenn.cit594.datamanagement;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidData;

public class JSONCovidReader extends CovidReader{

	public JSONCovidReader(String covidFile) {
		super(covidFile);
	}

	@Override
	public List<CovidData> getData() {
		covidDataLst.clear();
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) new JSONParser().parse(new FileReader(covidFile));
		} catch (FileNotFoundException e) {
			System.out.println("Sorry, " + covidFile + " not found");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Can't parse the object. " + e.getMessage());
		}
		
		//log the name of the input file each time a file is opened for reading
		Logger l = Logger.getInstance();
		l.log(System.currentTimeMillis() + " " + covidFile);
		
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = jsonArray.iterator();
		while(iterator.hasNext()) {
			//initialize or reset the fields
			String date = null;
			String zipCode = null;
			Double partial = null;
			Double full = null;
			
			JSONObject jo = iterator.next();
			if(jo.containsKey("etl_timestamp")) {
				date = jo.get("etl_timestamp").toString().substring(0, 10);
			}
			if(jo.containsKey("zip_code")) {
					zipCode = jo.get("zip_code").toString();
					int zipCodeLength = zipCode.length();//get zipCode length
					if(zipCodeLength >= 5) {//if length >= 5 digits
						zipCode = jo.get("zip_code").toString().substring(0, 5);//get the first five digits of the zipCode
					}
			}
			
			if(jo.containsKey("partially_vaccinated")) {
				try {//parse string into double
					partial = Double.parseDouble(jo.get("partially_vaccinated").toString());
				}catch(Exception e) {
					partial = null;//invalid value(non-numeric), set to null
				}
			}
			
			if(jo.containsKey("fully_vaccinated")) {
				try {//parse string into double
					full = Double.parseDouble(jo.get("fully_vaccinated").toString());
				}catch(Exception e) {
					full = null;//invalid value(non-numeric), set to null
				}
			}
			
			CovidData covidData = new CovidData(date, zipCode, partial, full);
			covidDataLst.add(covidData);
		}

		return covidDataLst;
	}

}

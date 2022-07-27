package edu.upenn.cit594.processor;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PopulationData;

public class VaccinationsProcessor {
	protected List<CovidData> covidDataLst;
	protected Map<String, String> vacPerCapitaMap;
	protected List<PopulationData> populationDataLst;
	//map for memoization
	private Map<List<String>, Map<String, String>> results = new HashMap<List<String>, Map<String, String>>();
	
	public VaccinationsProcessor(List<CovidData> covidDataLst, List<PopulationData> populationDataLst) {
		this.covidDataLst = covidDataLst;
		this.vacPerCapitaMap = new TreeMap<String, String>();//tree map for ascending order of the zipCode
		this.populationDataLst = populationDataLst;
	}
	
	public Map<String, String> getVacPerCapita(String partialOrFull, String date){
		List<String> key = new ArrayList<String>();
		key.add(partialOrFull);
		key.add(date);
		if(results.containsKey(key)) {
			return results.get(key);
		}else {
			Map<String, String> result= calculate(partialOrFull, date);
			results.put(key, result);
			return result;
		}
	}
	
	private Map<String, String> calculate(String partialOrFull, String date){
		for(CovidData covidData: covidDataLst) {//iterate through covidData list
			if(covidData.getDate().equals(date)) {//match the input date with the same date in the list
				if(covidData.getZipCode() != null) {//if zipcode is null, ignore it
					Double population = getPopulation(covidData.getZipCode(), populationDataLst);//get the population on the zipCode
					Double vaccinated = null;
					if(partialOrFull.equals("partial")) {//get total vaccinated number (partial or full)
						vaccinated = covidData.getPartial();
					}
					if(partialOrFull.equals("full")) {
						vaccinated = covidData.getFull();
					}
					if(vaccinated != null && population != null) {
						if(vaccinated != 0 && population != 0) {
							DecimalFormat df = new DecimalFormat("0.0000");//truncate to four digits, preserve the trailing 0
							df.setRoundingMode(RoundingMode.DOWN);//truncate, not round
							String vacPerCapita = df.format((vaccinated / population));//calculate "per capita"
							vacPerCapitaMap.put(covidData.getZipCode(), vacPerCapita);//put it into the map
						}
					}	
				}
			}
		}
	
		return vacPerCapitaMap;
	}
	
	/*
	 * get the population for a zipCode
	 */
	private Double getPopulation(String zipCode, List<PopulationData> populationLst) {
		Double population = null;
		for(PopulationData populationData: populationLst) {
			if(populationData.getZipCode().equals(zipCode)) {
				population = (double) populationData.getPopulation();
			}
		}	

		return population;
	}
	
}

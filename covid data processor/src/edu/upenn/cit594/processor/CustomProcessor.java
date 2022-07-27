package edu.upenn.cit594.processor;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PopulationData;
import edu.upenn.cit594.util.PropertyData;

public class CustomProcessor {
	protected List<PopulationData> populationDataLst;
	protected List<CovidData> covidDataLst;
	protected List<PropertyData> propertyDataLst;
	//map for memoization
	private Map<String, String> localFactors = new HashMap<String, String>();
	
	public CustomProcessor(List<PopulationData> populationDataLst, List<CovidData> covidDataLst, List<PropertyData> propertyDataLst) {
		this.populationDataLst = populationDataLst;
		this.covidDataLst = covidDataLst;
		this.propertyDataLst = propertyDataLst;
	}
	
	public String localFactor(String zipCode) {
		if(localFactors.containsKey(zipCode)) {//memoization
			return localFactors.get(zipCode);
		}else {
			String result = calculateLocalFactor(zipCode);
			localFactors.put(zipCode, result);
			return result;
		}
	}
	
	private String calculateLocalFactor(String zipCode) {
		String date = getLatestDate();
		Double fullyVaccinated = 0.0;
		for(CovidData covidData: covidDataLst) {
			if(covidData.getDate().equals(date)) {
				if(covidData.getZipCode() != null && covidData.getZipCode().equals(zipCode)) {
					if(covidData.getFull() != null) {
						fullyVaccinated = covidData.getFull();
						break;
					}else {
						System.out.println("no vaccinated data under the given zip code.");
						return null;
					}
					
				}
			}
		}
		int population = getPopulation(zipCode);
		Double vaccinationPerCapita = 0.0;
		if(population == 0) {
			System.out.println("zip code out of range.");
			return null;
		}
		vaccinationPerCapita = fullyVaccinated / getPopulation(zipCode);
		Double livableAreaPerCapita = localLivableAreaPerCapita(zipCode);
		if(livableAreaPerCapita != 0.0) {
			Double result = (vaccinationPerCapita / livableAreaPerCapita) * 10000;
			//truncate the result to integer
			DecimalFormat df = new DecimalFormat("0.00");//truncate to integer, preserve the trailing 0
			df.setRoundingMode(RoundingMode.DOWN);//truncate, not round
			String factor = df.format(result);
			return factor;
		}else {
			System.out.println("Cannot get local livable area per capita.");
			return null;
		}			
	}
	
	/*
	 * helper: get local livable area per capita
	 */
	private Double localLivableAreaPerCapita(String zipCode) {
		int population = getPopulation(zipCode);
		if(population == 0) {
			System.out.println("zip code out of range.");
			return null;
		}
		Double sum = 0.0;
		for(PropertyData propertyData: propertyDataLst) {
			if(propertyData.getZipCode() != null && propertyData.getZipCode().equals(zipCode)) {
				if(propertyData.getLivableArea() != null) {
					sum += propertyData.getLivableArea();
				}	
			}
		}
		if(population != 0) {
			return (sum / population) ;
		}else {
			return 0.0;
		}	
	}
	
	public String standardFactor() {
		Double result = 0.0;
		Double totalVaccinatedPerCapita = totalVaccinatedPerCapita();
		Double livableAreaPerCapita = livableAreaPerCapita();
		if(livableAreaPerCapita == 0.0) {
			return null;
		}else {
			result = (totalVaccinatedPerCapita / livableAreaPerCapita) * 10000;
		}
		
		//truncate the result to integer
		DecimalFormat df = new DecimalFormat("0.00");//truncate to integer, preserve the trailing 0
		df.setRoundingMode(RoundingMode.DOWN);//truncate, not round
		String factor = df.format(result);
		
		return factor;
	}
	
	/*
	 * Total fully-vaccinated per capita
	 * Note: because vaccinated number is cumulative, 
	 * we assume total vaccinated number to be the total fully-vaccinated in all zip code in the LAST DATE in the file,
	 * that is, the most up-to-date vaccinated number
	 */
	private Double totalVaccinatedPerCapita() {
		String date = getLatestDate();
		Double fullyVaccinatedTotal = 0.0;
		int population = 0;
		for(CovidData covidData: covidDataLst) {
			if(covidData.getDate() != null && covidData.getDate().equals(date)) {
				if(covidData.getFull() != null) {
					fullyVaccinatedTotal += covidData.getFull();
					population += getPopulation(covidData.getZipCode());
				}
			}
		}
	
		if(population != 0) {
			return (fullyVaccinatedTotal / population) ;
		}else {
			return 0.0;
		}
	}
	
	/*
	 * all livable area in properties.csv divided by total population
	 */
	private Double livableAreaPerCapita() {
		HashSet<String> zipCodeProcessed = new HashSet<String>();
		int totalPopulation = 0;
		Double sum = 0.0;
		for(PropertyData propertyData: propertyDataLst) {
			if(propertyData.getLivableArea() != null) {
				sum += propertyData.getLivableArea();
				String zipCode = propertyData.getZipCode();
				if(!zipCodeProcessed.contains(zipCode)) {
					totalPopulation += getPopulation(zipCode);
					zipCodeProcessed.add(zipCode);
				}
			}
		}
		if(totalPopulation != 0) {
			return (sum / totalPopulation) ;
		}else {
			return 0.0;
		}
	}
	
	/*
	 * helper: get population under the given zip code
	 */
	private int getPopulation (String zipCode) {
		int population = 0;
		for(PopulationData populationData: populationDataLst) {
			if(populationData.getZipCode().equals(zipCode)) {
				population = populationData.getPopulation();
				break;
			}
		}
		return population;
	}
	
	/*
	 * helper: get the latest date, that is, the last date in the file
	 */
	private String getLatestDate() {
		return covidDataLst.get(covidDataLst.size() - 1).getDate();
	}
}

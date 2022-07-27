package edu.upenn.cit594.processor;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upenn.cit594.util.PopulationData;
import edu.upenn.cit594.util.PropertyData;

public class MarketValuePerCapitaProcessor {
	protected ArrayList<PropertyData> propertyMarketValues;
	protected ArrayList<PopulationData> populationDataLst;
	//map for memoization
	private Map<String, String> results = new HashMap<String, String>();
	
	public MarketValuePerCapitaProcessor(List<PropertyData> propertyDataLst, List<PopulationData> populationDataLst) {
		this.propertyMarketValues = (ArrayList<PropertyData>) propertyDataLst;	
		this.populationDataLst = (ArrayList<PopulationData>) populationDataLst;
	}
	
	public String marketValuePerCapita(String zipCode) {
		if(results.containsKey(zipCode)) {//memoization
			return results.get(zipCode);
		}else {
			String result = calculate(zipCode);
			results.put(zipCode, result);
			return result;
		}
	}
	
	/*
	 * total market value per capita
	 */
	private String calculate(String zipCode) {
		Double totalMarketValue = 0.0;
		int population = 0;
		//get total market value under the given zip code
		for(PropertyData propertyData: propertyMarketValues) {
			String propertyZipCode = propertyData.getZipCode();
			if(propertyZipCode != null) {
				if(propertyZipCode.equals(zipCode)) {
					Double marketValue = propertyData.getMarketValue();
					if(marketValue != null)
						totalMarketValue += marketValue;
				}
			}	
		}
		//get population under the given zip code
		for(PopulationData populationData: populationDataLst) {
			String populationZipCode = populationData.getZipCode();
			if(populationZipCode != null) {
				if(populationZipCode.equals(zipCode)) {
					population = populationData.getPopulation();
				}
			}	
		}
		
		if(totalMarketValue == 0.0 || population == 0) {
			return "0";
		}
		
		Double result = totalMarketValue / population;
		//truncate the result to integer-formatted string
		DecimalFormat df = new DecimalFormat("0");//truncate to integer, preserve the trailing 0
		df.setRoundingMode(RoundingMode.DOWN);//truncate, not round
		String perCapita = df.format(result);
		
		return perCapita;
	}

}

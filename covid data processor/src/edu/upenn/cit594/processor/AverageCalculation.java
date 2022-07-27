package edu.upenn.cit594.processor;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upenn.cit594.util.PropertyData;

public class AverageCalculation {
	protected List<PropertyData> propertyDataLst;

	//maps for memoization
	private Map<String, String> marketValue = new HashMap<String, String>();
	private Map<String, String> livableArea = new HashMap<String, String>();
	
	public AverageCalculation(List<PropertyData> propertyDataLst) {
		this.propertyDataLst = propertyDataLst;
	}
	
	public String average(PropertyProcessor p, String zipCode) {
		Double sum = 0.0;
		for(PropertyData propertyData: propertyDataLst) {
			Double value = p.getValue(propertyData);
			if(value != null)
			sum += value;
		}
		//get total number of homes in the given zipCode
		int totalNum = numOfProperties(zipCode);
		if(totalNum == 0) {//no homes under the given zipCode
			return "0";
		}
		Double result = sum / totalNum;
		//truncate the result to integer
		DecimalFormat df = new DecimalFormat("0");//truncate to integer, preserve the trailing 0
		df.setRoundingMode(RoundingMode.DOWN);//truncate, not round
		String averageValue = df.format(result);
		
		return averageValue;
	}
	
	public String averageMarketValue(String zipCode) {
		if(marketValue.containsKey(zipCode)) {//memoization
			return marketValue.get(zipCode);
		}else {
			String result = average(new MarketDataProcessor(propertyDataLst), zipCode);
			marketValue.put(zipCode, result);
			return result;
		}
	}
	
	public String averageLivableArea(String zipCode) {
		if(livableArea.containsKey(zipCode)) {//memoization
			return livableArea.get(zipCode);
		}else {
			String result = average(new LivableAreaProcessor(propertyDataLst), zipCode);
			livableArea.put(zipCode, result);
			return result;
		}
	}
	
	//helper: num of homes
	public int numOfProperties(String zipCode) {
		int totalNum = 0; 
		for(PropertyData propertyData: propertyDataLst) {
			String propertyZipCode = propertyData.getZipCode();
			if(propertyZipCode != null) {
				if(propertyZipCode.equals(zipCode)) {
					totalNum ++;
				}
			}
			
		}
		return totalNum;		
	}
	
}

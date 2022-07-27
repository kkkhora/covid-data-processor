package edu.upenn.cit594.processor;

import java.util.ArrayList;
import java.util.List;

import edu.upenn.cit594.util.PropertyData;

public class MarketDataProcessor implements PropertyProcessor {

	protected ArrayList<PropertyData> propertyMarketValues;
	
	public MarketDataProcessor(List<PropertyData> propertyDataLst) {
		this.propertyMarketValues = (ArrayList<PropertyData>) propertyDataLst;	
	}
	
	@Override
	public Double getValue(PropertyData propertyData) {
		return propertyData.getMarketValue();
	}
	
}

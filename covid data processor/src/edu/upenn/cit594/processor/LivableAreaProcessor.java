package edu.upenn.cit594.processor;

import java.util.ArrayList;
import java.util.List;

import edu.upenn.cit594.util.PropertyData;

public class LivableAreaProcessor implements PropertyProcessor {
	
protected ArrayList<PropertyData> propertyDataLst;
	
	public LivableAreaProcessor(List<PropertyData> propertyDataLst) {
		this.propertyDataLst = (ArrayList<PropertyData>) propertyDataLst;	
	}
	@Override
	public Double getValue(PropertyData propertyData) {
		return propertyData.getLivableArea();
	}

}

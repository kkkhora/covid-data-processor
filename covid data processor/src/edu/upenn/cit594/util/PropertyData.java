package edu.upenn.cit594.util;

public class PropertyData {
	private Double marketValue;
	private Double livableArea;
	private String zipCode;
	
	//constructor
	public PropertyData(Double marketValue, Double livableArea, String zipCode) {
		this.marketValue = marketValue;
		this.livableArea = livableArea;
		this.zipCode = zipCode;
	}
	
	/*
	 * getters
	 */
	
	public Double getMarketValue() {
		return marketValue;
	}
	
	public Double getLivableArea() {
		return livableArea;
	}
	
	public String getZipCode() {
		return zipCode;
	}
}

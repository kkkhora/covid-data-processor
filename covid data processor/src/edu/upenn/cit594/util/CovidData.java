package edu.upenn.cit594.util;

public class CovidData {
	private String date;
	private String zipCode;
	private Double partial;
	private Double full;
	
	//constructor
	public CovidData(String date, String zipCode, Double partial, Double full) {
		this.date = date;
		this.zipCode = zipCode;
		this.partial = partial;
		this.full = full;
	}
	
	/*
	 * getters
	 */
	public String getDate() {
		return date;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	
	public Double getPartial() {
		return partial;
	}
	
	public Double getFull() {
		return full;
	}
}

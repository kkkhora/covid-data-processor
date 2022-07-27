package edu.upenn.cit594.util;

public class PopulationData {
	private String zipCode;
	private int population;
	
	//constructor
	public PopulationData(String zipCode, int population) {
		this.zipCode = zipCode;
		this.population = population;
	}
	
	//getters
	public String getZipCode() {
		return zipCode;
	}
	
	public int getPopulation() {
		return population;
	}
}

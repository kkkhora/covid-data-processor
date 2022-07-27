package edu.upenn.cit594.processor;

import java.util.List;

import edu.upenn.cit594.util.PopulationData;

public class TotalPopulationProcessor {
	
	protected List<PopulationData> populationDataLst;
	
	public TotalPopulationProcessor(List<PopulationData> populationDataLst) {
		this.populationDataLst = populationDataLst;
	}
	
	public int totalPopulation() {
		int population = 0;
		for(PopulationData populationData: populationDataLst) {
			population += populationData.getPopulation();
		}
		return population;
	}
}

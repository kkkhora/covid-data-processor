package edu.upenn.cit594.datamanagement;

import java.util.ArrayList;
import java.util.List;

import edu.upenn.cit594.util.CovidData;

public abstract class CovidReader {
	protected String covidFile;
	protected List<CovidData> covidDataLst;
	
	//constructor
	public CovidReader(String covidFile) {
		this.covidFile = covidFile;
		this.covidDataLst = new ArrayList<CovidData>();
	}
	
	//abstract method
	public abstract List<CovidData> getData();
}

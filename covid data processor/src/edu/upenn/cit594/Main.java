package edu.upenn.cit594;

import java.util.List;

import edu.upenn.cit594.datamanagement.CSVCovidReader;
import edu.upenn.cit594.datamanagement.CovidReader;
import edu.upenn.cit594.datamanagement.JSONCovidReader;
import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.datamanagement.PropertiesReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.AverageCalculation;
import edu.upenn.cit594.processor.CustomProcessor;
import edu.upenn.cit594.processor.MarketValuePerCapitaProcessor;
import edu.upenn.cit594.processor.TotalPopulationProcessor;
import edu.upenn.cit594.processor.VaccinationsProcessor;
import edu.upenn.cit594.ui.UserInterface;
import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PopulationData;
import edu.upenn.cit594.util.PropertyData;

public class Main {

	public static void main(String[] args) {
		/*
		 * read runtime arguments
		 */
		
		/*
		 * check if the number of arguments is correct
		 */
		if(args.length != 4) {
			System.out.println("Incorrect number of arguments.");
			return;
		}
		
		/*
		 * check covid-file matching
		 */
		if((!args[0].toLowerCase().contains(".json")) && (!args[0].toLowerCase().contains(".csv"))) {
			System.out.println("Covid file does not match.");
			return;
		}
		
		System.out.println("Welcome! Please wait for a moment while we are preparing the data...");
		System.out.println();
		
		//get file name
		String covidFile = args[0]; // The name of the covid data file
		String propertyFile = args[1]; // The name of the property values file
		String populationFile = args[2]; // The name of the population file
		String logFile = args[3]; // The name of the log 
		
		//create and write to the logger
		Logger l = Logger.getInstance();
		l.openFile(logFile);
		l.log(System.currentTimeMillis() + 
			  " " + covidFile + 
			  " " + propertyFile + 
			  " " + populationFile +
			  " " + logFile);
		
		
		//read covid files
		CovidReader covidReader;
		if(covidFile.contains(".json")) {
			covidReader = new JSONCovidReader(covidFile);
			}
		else {
			covidReader = new CSVCovidReader(covidFile);
			}
		
		//read other files
		PropertiesReader propertiesReader = new PropertiesReader(propertyFile);
		PopulationReader populationReader = new PopulationReader(populationFile);
		
		//parse input data, put into lists
		List<CovidData> covidDataLst = covidReader.getData();
		List<PropertyData> propertyDataLst = propertiesReader.getData();
		List<PopulationData> populationDataLst = populationReader.getData();
		
		//get processors
		TotalPopulationProcessor totalPopulationProcessor = new TotalPopulationProcessor(populationDataLst);
		VaccinationsProcessor vaccinationsProcessor = new VaccinationsProcessor(covidDataLst, populationDataLst);
		AverageCalculation averageCalculation = new AverageCalculation(propertyDataLst);
		MarketValuePerCapitaProcessor marketValuePerCapitaProcessor = new MarketValuePerCapitaProcessor(propertyDataLst, populationDataLst);
		CustomProcessor customProcessor = new CustomProcessor(populationDataLst, covidDataLst, propertyDataLst);
		
		//get ui
		UserInterface ui = new UserInterface(totalPopulationProcessor, vaccinationsProcessor, averageCalculation, marketValuePerCapitaProcessor, customProcessor);
		try {
			ui.start();
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		
		}
	}


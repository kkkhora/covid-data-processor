package edu.upenn.cit594.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import edu.upenn.cit594.InvalidInputException;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.AverageCalculation;
import edu.upenn.cit594.processor.CustomProcessor;
import edu.upenn.cit594.processor.MarketValuePerCapitaProcessor;
import edu.upenn.cit594.processor.TotalPopulationProcessor;
import edu.upenn.cit594.processor.VaccinationsProcessor;

public class UserInterface {
	
	protected TotalPopulationProcessor totalPopulationProcessor;
	protected VaccinationsProcessor vaccinationsProcessor;
	protected AverageCalculation averageCalculation;
	protected MarketValuePerCapitaProcessor marketValuePerCapitaProcessor;
	protected CustomProcessor customProcessor;
	//map for memoization
	private Map<String, Object> results = new HashMap<String, Object>();
	
	public UserInterface(TotalPopulationProcessor totalPopulationProcessor, VaccinationsProcessor vaccinationsProcessor, AverageCalculation averageCalculation, MarketValuePerCapitaProcessor marketValuePerCapitaProcessor, CustomProcessor customProcessor) {
		this.totalPopulationProcessor = totalPopulationProcessor;
		this.vaccinationsProcessor = vaccinationsProcessor;
		this.averageCalculation = averageCalculation;
		this.marketValuePerCapitaProcessor = marketValuePerCapitaProcessor;
		this.customProcessor = customProcessor;
	}
	
	public void start() throws InvalidInputException {
		String input = null;
		Scanner s = new Scanner(System.in);
		
		while(true) {
			printWelcomeMsg();
			//read user input
			input = s.nextLine();
			
			//log the response from a user any time the user is asked for input
			Logger l = Logger.getInstance();
			l.log(System.currentTimeMillis() + " " + input);
			
			//0
			if(input.equals("0")) {
				s.close();
				l.closeFile();
				System.out.println("user input 0, exit.");
				break;
			}

			choice(input, s);
		}
	}
	
	private void printWelcomeMsg() {
		System.out.println("Please enter a number from 0 to 6.");
		System.out.println( "0: exit the program." + "\n" +
							"1: total population for all ZIP codes." + "\n" +
							"2: total partial or full vaccinations per capita." + "\n" +
							"3: average market value." + "\n" +
							"4: average total livable area." + "\n" +
							"5: total market value per capita." + "\n" +
							"6: factor reflecting the willingness to get vaccination.");
		//prompt line
		System.out.print("> ");
		System.out.flush();
	}
	
	private void choice(String input, Scanner s) throws InvalidInputException {
		//1
		if(input.equals("1")) {
			choiceOne();
		}
		//2
		else if(input.equals("2")) {
			choiceTwo(input, s);				
		}
		//3
		else if(input.equals("3")) {
			choiceThreeToSix(input, s);
		}
		//4
		else if(input.equals("4")) {
			choiceThreeToSix(input, s);
		}
		//5
		else if(input.equals("5")) {
			choiceThreeToSix(input, s);
		}
		//6
		else if(input.equals("6")) {
			choiceThreeToSix(input, s);
		}
		//other input
		else {
			InvalidInputException exception = new InvalidInputException("Invalid input: must be an integer between 0 - 6.");
			throw exception;
		}
	}
	
	private void choiceOne() {
		System.out.println();
		System.out.println("BEGIN OUTPUT");
		if(results.containsKey("1")) {//memoization
			System.out.println(results.get("1"));	
		}else {
			int population = totalPopulationProcessor.totalPopulation();
			System.out.println(population);
			results.put("1", population);
		}
		System.out.println("END OUTPUT");
		System.out.println();
	}
	
	private void choiceTwo(String input, Scanner s) throws InvalidInputException {
		//prompt user for partial or full
		System.out.println("Please enter a type [partial] or [full]:" );
		System.out.print("> ");
		System.out.flush();
		
		input = s.nextLine();
		//log the response from a user any time the user is asked for input
		Logger l = Logger.getInstance();
		l.log(System.currentTimeMillis() + " " + input);
		
		String partialOrFull = null;
		if(input.toLowerCase().equals("partial")) {
			partialOrFull = "partial";
		}
		else if(input.toLowerCase().equals("full")) {
			partialOrFull = "full";
		}else {
			s.close();
			l.closeFile();
			InvalidInputException exception = new InvalidInputException("invalid input: must be either [partial] or [full]");
			throw exception;
		}
		
		//prompt user for a date
		System.out.println("Please enter a date with the format YYYY-MM-DD: ");
		System.out.print("> ");
		System.out.flush();
		
		input = s.nextLine();
		//log the response from a user any time the user is asked for input
		l.log(System.currentTimeMillis() + " " + input);
		
		String date;
		if(Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", input)) {
			date = input;
			Map<String, String> vacPerCapitaMap = vaccinationsProcessor.getVacPerCapita(partialOrFull, date);
			if(vacPerCapitaMap.isEmpty()) {//date out of range
				System.out.println();
				System.out.println("BEGIN OUTPUT");
				System.out.println("0");
				System.out.println("END OUTPUT");
				System.out.println();
			}else {
				System.out.println();
				System.out.println("BEGIN OUTPUT");
				for(Map.Entry<String, String> entry: vacPerCapitaMap.entrySet()) {
					System.out.println(entry.getKey() + " " + entry.getValue());
				}
				System.out.println("END OUTPUT");
				System.out.println();
			}
		}else {
			l.closeFile();
			s.close();
			InvalidInputException exception = new InvalidInputException("Invalid date input.");
			throw exception;
		}
	}
	
	private void choiceThreeToSix (String input, Scanner s) throws InvalidInputException {
		String inputNum = input;
		//prompt user for zipCode
		System.out.println("Please enter a five-digit zip code: " );
		System.out.print("> ");
		System.out.flush();
		
		input = s.nextLine();
		//log the response from a user any time the user is asked for input
		Logger l = Logger.getInstance();
		l.log(System.currentTimeMillis() + " " + input);
		
		String zipCode;
		if(Pattern.matches("[0-9]{5}", input)) {//check valid zip code
			zipCode = input;
			System.out.println();
			System.out.println("BEGIN OUTPUT");
			if(inputNum.equals("3")) {
				System.out.println(averageCalculation.averageMarketValue(zipCode));
			}
			if(inputNum.equals("4")) {
				System.out.println(averageCalculation.averageLivableArea(zipCode));
			}
			if(inputNum.equals("5")) {
				System.out.println(marketValuePerCapitaProcessor.marketValuePerCapita(zipCode));
			}
			if(inputNum.equals("6")) {
				System.out.println("local factor is: " + customProcessor.localFactor(zipCode));
				if(results.containsKey("6")) {//memoization
					System.out.println("standard factor is: " + results.get("6"));	
				}else {
					String factor = customProcessor.standardFactor();
					System.out.println("standard factor is: " + factor);
					results.put("6", factor);
				}
			}
			System.out.println("END OUTPUT");
			System.out.println();	
		}else {
			s.close();
			l.closeFile();
			InvalidInputException exception = new InvalidInputException("invalid zip code input.");
			throw exception;
		}
	}
}

package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidData;

public class CSVCovidReader extends CovidReader{

	public CSVCovidReader(String covidFile) {
		super(covidFile);
	}

	@Override
	public List<CovidData> getData() {
		covidDataLst.clear();
		String date;
		String zipCode;
		Double partial;
		Double full;
		
		String line = null;
		//create file
		File file = new File(covidFile);
		//define file reader
		FileReader fileReader = null;
		//define buffered reader
		BufferedReader br = null;

		try {
			fileReader = new FileReader(file);
			br = new BufferedReader(fileReader);
		} catch (FileNotFoundException e) {
			System.out.println("Sorry, " + file.getName() + " not found");
		}
		
		//log the name of the input file each time a file is opened for reading
		Logger l = Logger.getInstance();
		l.log(System.currentTimeMillis() + " " + covidFile);
								
		try {
			line = br.readLine();
		} catch (IOException e1) {
			System.out.println("readLine() problem.");
			e1.printStackTrace();
			l.closeFile();
			}						
		
		if(line == null) {
			System.out.println("header of the covidFile is null");
		}
//			System.out.println(line);
		
		//parse the header and get index
		int dateIndex = -1;
		int zipCodeIndex = -1;
		int partialIndex = -1;
		int fullIndex = -1;
//		System.out.println(line);
		List<Integer> index = new ArrayList<Integer>();
		index = getIndex(line, dateIndex, zipCodeIndex, partialIndex, fullIndex);
		dateIndex = index.get(0);
		zipCodeIndex = index.get(1);
		partialIndex = index.get(2);
		fullIndex = index.get(3);
//		System.out.println("dateIndex: " + dateIndex);
		
		try {
			while((line = br.readLine()) != null) {
				date = null;
				zipCode = null;
				partial = null;
				full = null;
				
				//parse each line
				line = line.replaceAll("\"", "");
//				System.out.println(line);
				String[] covidRow = line.split(",");
				int covidRowLength = covidRow.length;
				if(dateIndex > covidRowLength - 1) {
					date = null;
				}else {
					date = parseDate(covidRow[dateIndex]);
				}
				if(zipCodeIndex > covidRowLength - 1) {
					zipCode = null;
				}else {
					zipCode = parseZipCode(covidRow[zipCodeIndex]) ;
				}
				if(partialIndex > covidRowLength - 1) {
					partial = null;
				}else {
					partial = parseDouble(covidRow[partialIndex]);
				}
				if(fullIndex > covidRowLength - 1) {
					full = null;
				}else {
					full = parseDouble(covidRow[fullIndex]);
				}
								
//				System.out.println("date: " + date);
//				System.out.println("zipCode: " + zipCode);
//				System.out.println("partial: " + partial);
//				System.out.println("full: " + full);
				
				CovidData covidData = new CovidData(date, zipCode, partial, full);
				covidDataLst.add(covidData);
			}
		} catch (IOException e) {
			System.out.println("read data problem.");
			e.printStackTrace();
			l.closeFile();
		}
		
		try {
			fileReader.close();
			br.close();
		} catch (IOException e) {
			System.out.println("can't close the Reader.");
			e.printStackTrace();
			l.closeFile();
		}

		return covidDataLst;
	}
	
	//helper methods
	private List<Integer> getIndex(String line, int dateIndex, int zipCodeIndex, int partialIndex, int fullIndex) {
		//parse the header
		line = line.replaceAll("\"", "");
//		System.out.println(line);
		String[]titles = line.split(",");
				
		//get index of the title: market_value, total_livable_area, zipCodeIndex
		for(int i = 0; i < titles.length; i++) {
			if(titles[i].contains("etl_timestamp")) {
				dateIndex = i;
			}else if(titles[i].equals("zip_code")) {
				zipCodeIndex = i;
			}else if(titles[i].equals("partially_vaccinated")) {
				partialIndex = i;
			}else if(titles[i].equals("fully_vaccinated")) {
				fullIndex = i;
			}
		}
				
		//error message
		if(dateIndex == -1) {
			System.out.println("dateIndex not found.");
		}
		if(zipCodeIndex == -1) {
			System.out.println("zipCodeIndex not found.");
		}
		if(partialIndex == -1) {
			System.out.println("partialIndex not found.");
		}
		if(fullIndex == -1) {
			System.out.println("fullIndex not found.");
		}
//		System.out.println("dateIndex: " + dateIndex + '\n' + 
//							"zipCodeIndex: " + zipCodeIndex + '\n' +
//							"partialIndex: " + partialIndex + '\n' +
//							"fullIndex: " + fullIndex + '\n');
		List<Integer> index = new ArrayList<Integer>();
		index.add(dateIndex);
		index.add(zipCodeIndex);
		index.add(partialIndex);
		index.add(fullIndex);
		
		return index;
	}
	
	private String parseDate(String date) {
		String formattedDate = null;
		if(date != null) {//date of the format: YYYY-MM-DD
			formattedDate = date.substring(6, 10) + "-" + date.substring(3, 5) + "-" + date.substring(0, 2);
		}		
		return formattedDate;
	}
	
	private String parseZipCode(String zipCode) {
		if(zipCode != null) {
			int zipCodeLength = zipCode.length();//get zipCode length
			if(zipCodeLength < 5) {//if length is less than 5 digits
				zipCode = null;//invalid zipCode, set to null
			}else {
				zipCode = zipCode.substring(0, 5);//get the first five digits of the zipCode
			}
		}
		return zipCode;
	}
	
	private Double parseDouble(String value) {
		Double parsedValue = null;
		try {//parse string into double
			parsedValue = Double.parseDouble(value);
			}catch(Exception e) {
				parsedValue = null;//invalid value(non-numeric), set to null
			}
		return parsedValue;
	}
}

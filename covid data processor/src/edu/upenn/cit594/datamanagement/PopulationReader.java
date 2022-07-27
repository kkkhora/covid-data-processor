package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.PopulationData;

public class PopulationReader {
	protected String populationFile;
	protected List<PopulationData> populationDataLst;
	
	//constructor
	public PopulationReader(String populationFile) {
		this.populationFile = populationFile;
		this.populationDataLst = new ArrayList<PopulationData>();
	}
	
	//method: read file and parse 
	public List<PopulationData> getData(){
		populationDataLst.clear();
		String zipCode;
		int population;
		String line = null;
		//create file
		File file = new File("population.txt");
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
		
		//log
		Logger l = Logger.getInstance();
		l.log(System.currentTimeMillis() + " " + populationFile);
		
		try {
			while((line = br.readLine()) != null) {
				//parse each line
				String[] populationArr = line.split(" ");
				zipCode = populationArr[0];
				population = Integer.parseInt(populationArr[1]);
				PopulationData populationData = new PopulationData(zipCode, population);
				populationDataLst.add(populationData);
			}
		} catch (IOException e) {
			System.out.println("readLine() problem.");
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
		
		return populationDataLst;
	}
}

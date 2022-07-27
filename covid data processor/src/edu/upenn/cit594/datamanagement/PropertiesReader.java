package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.PropertyData;


public class PropertiesReader {
	protected String propertyFile;
	protected List<PropertyData> propertyDataLst;
	
	public PropertiesReader(String propertyFile) {
		//constructor
			this.propertyFile = propertyFile;
			this.propertyDataLst = new ArrayList<PropertyData>();		
	}
	
	// method: read file and parse
	public  List<PropertyData> getData(){
		propertyDataLst.clear();
		String line = null;
		//create file
		File file = new File(propertyFile);
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
		l.log(System.currentTimeMillis() + " " + propertyFile);
								
		try {
			line = br.readLine();
		} catch (IOException e1) {
			System.out.println("readLine() problem.");
			e1.printStackTrace();
			l.closeFile();
		}						
		
		if(line == null) {
			System.out.println("header of the propertyFile is null");
		}
//			System.out.println(line);
		
		//parse the header
		int marketValueIndex = -1;
		int totalLivableAreaIndex = -1;
		int zipCodeIndex = -1;
		
		List<Integer> index = new ArrayList<Integer>();
		index = getIndex(line, marketValueIndex, totalLivableAreaIndex, zipCodeIndex);
		marketValueIndex = index.get(0);
		totalLivableAreaIndex = index.get(1);
		zipCodeIndex = index.get(2);
		
		//read chars
		int r;
		//state machine, set to default
		String state = "default";
		StringBuilder str = new StringBuilder();
		ArrayList<String> property = new ArrayList<String>();
				
		try {
			while((r = br.read()) != -1) {//until reaching EOF
				char c = (char) r;
				if(c != ',' && c != '"' && c != '\n' && c != '\r' ) {//if the char isn't comma, quotation mark, new line/return character
					str.append(c);//append to the stringbuilder
				}else {
					if(c == ',') {//if the char is comma
						if(state.equals("default")) {//check if it's within the quotation
							property.add(str.toString());//if it's not, add the item to the list
							str.setLength(0);//reset the stringbuilder
						}
						
						if(state.equals("foundOpenQuotation")) {//if it's within the quotation
							str.append(c);//ignore the comma and append it to the stringbuilder
						}
					}
					
					if(c == '"') {//if the char is a quotation mark
						r = br.read();//check the next char right after this quotation mark
						if(r == -1) {//if it reaches EOF
							property.add(str.toString());//add this last item to the list
							str.setLength(0);//reset the stringbuilder
							break;//break the loop
						}else if((char)r == ',') {//if the char right after the quotation mark is a comma, it means this quotation mark is the ending quotation mark
							state = "default";//reset the state to default
							property.add(str.toString());//add this item (with comma inside) into the list
							str.setLength(0);//reset the stringbuilder
						}else {//if the char right after the quotation mark is not a comma, it means this quotation mark is the open quotation mark
							str.append((char)r);//append it to the stringbuilder
							state = "foundOpenQuotation";//set the state machine into "foundOpenQuotation", which will ignore the comma within the quotation
						}
					}
					
					if(c == '\n' || c == '\r') {//if the line ends
						if(property.size() != 0) {
							property.add(str.toString());//add the last item into the list
							str.setLength(0);//reset the string builder
							//get the data of interest
							PropertyData propertyData = getPropertyData(property, marketValueIndex, totalLivableAreaIndex, zipCodeIndex);
							propertyDataLst.add(propertyData);
							property.clear();//reset the list
						}
					}
				}
				
			}
		} catch (IOException e) {
			System.out.println("read() problem.");
			e.printStackTrace();
			l.closeFile();
		}
		
//		//get the last line of data, before it reaches EOF
//		PropertyData propertyData = getPropertyData(property, marketValueIndex, totalLivableAreaIndex, zipCodeIndex);
//		propertyDataLst.add(propertyData);
//		System.out.println(propertyDataLst.get(propertyDataLst.size() - 1).getMarketValue());
		
		try {
			fileReader.close();
			br.close();
		} catch (IOException e) {
			System.out.println("can't close the Reader.");
			e.printStackTrace();
			l.closeFile();
		}		
		
		return propertyDataLst;
	}
		
	//helper methods
	private List<Integer> getIndex(String line, int marketValueIndex, int totalLivableAreaIndex, int zipCodeIndex) {
		String[]titles = line.split(",");
		//get index of the title: market_value, total_livable_area, zipCodeIndex
		for(int i = 0; i < titles.length; i++) {
			if(titles[i].equals("market_value")) {
				marketValueIndex = i;
			}else if(titles[i].equals("total_livable_area")) {
				totalLivableAreaIndex = i;
			}else if(titles[i].equals("zip_code")) {
				zipCodeIndex = i;
				}
			}
				
		//error message
		if(marketValueIndex == -1) {
			System.out.println("marketValueIndex not found.");
			}
		if(totalLivableAreaIndex == -1) {
			System.out.println("totalLivableAreaIndex not found.");
			}
		if(zipCodeIndex == -1) {
			System.out.println("zipCodeIndex not found.");
			}
				
//		System.out.println("marketValueIndex: " + marketValueIndex + '\n' + 
//							"totalLivableAreaIndex: " + totalLivableAreaIndex + '\n' +
//							"zipCodeIndex: " + zipCodeIndex + '\n');
		
		List<Integer> index = new ArrayList<Integer>();
		index.add(marketValueIndex);
		index.add(totalLivableAreaIndex);
		index.add(zipCodeIndex);
		
		return index;
	}
	
	private PropertyData getPropertyData(ArrayList<String> property, int marketValueIndex, int totalLivableAreaIndex, int zipCodeIndex) {
		
//		System.out.println( "market value: " + property.get(marketValueIndex) + "\n" +
//				"total livable area: " + property.get(totalLivableAreaIndex) + "\n" +
//				"zipCode: " + property.get(zipCodeIndex) + "\n");
		
		Double marketValue;
		Double totalLivableArea;
		String zipCode;
		
		try {//parse string into double
		marketValue = Double.parseDouble(property.get(marketValueIndex));
		}catch(Exception e) {
			marketValue = null;//invalid value(non-numeric), set to null
		}
		
		try {//parse string into double
			totalLivableArea = Double.parseDouble(property.get(totalLivableAreaIndex));
			}catch(Exception e) {
				totalLivableArea = null;//invalid value(non-numeric), set to null
			}
		
		int zipCodeLength = property.get(zipCodeIndex).length();//get zipCode length
		if(zipCodeLength < 5) {//if length is less than 5 digits
			zipCode = null;//invalid zipCode, set to null
		}else {
			zipCode = property.get(zipCodeIndex).substring(0, 5);//get the first five digits of the zipCode
		}

		PropertyData propertyData = new PropertyData(marketValue, totalLivableArea, zipCode);
		
		return propertyData;
	}
}

package edu.upenn.cit594.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {

	FileWriter fileWriter = null;
	PrintWriter printWriter = null;
	
	//private constructor
	private Logger() {
		}
		
	//singleton instance
	private static Logger instance = new Logger();
	//singleton accessor method
	public static Logger getInstance() {
		return instance;
	}
	
    //non-static methods
	//open the log file
	public void openFile(String fileName) {
		File file = new File(fileName);
		try{
			fileWriter = new FileWriter(file, true);//write at the end of the file, not overwrite previous data
			printWriter = new PrintWriter(fileWriter);
		}
		catch (IOException e) {
			System.out.println("cannot create/open the log file " + file);
	}
	}
	
	//log messages
	public void log(String msg) {
		printWriter.println(msg);
		printWriter.flush();
	}
	
	//close the log file
	public void closeFile() {
			try {
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("can't close the file" + e.getMessage());
			}			
			printWriter.close();			
		}
	}

package edu.upenn.cit594;

@SuppressWarnings("serial")
public class InvalidInputException extends Exception {
	public InvalidInputException(String str){
		super(str);
	}
}

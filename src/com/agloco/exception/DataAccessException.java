package com.agloco.exception;

/**
 * 
 * @author terry_zhao
 * @date Apr 24, 2007
 */
public class DataAccessException extends RuntimeException {

	public DataAccessException(){
		super();
	}
	
	public DataAccessException(String message){
		super(message);
	}
	
	public DataAccessException(String message, Throwable cause){
		super(message,cause);
	}
	
	public DataAccessException(Throwable cause){
		super(cause);
	}
}

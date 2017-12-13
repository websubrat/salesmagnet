package com.db.awmd.challenge.exception;

public class InsufficientBalanceException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8458514013911106371L;

	public InsufficientBalanceException(String message) {
		super(message);
	}

}

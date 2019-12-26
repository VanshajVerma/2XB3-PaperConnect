package com.paperconnect.exception;

import java.io.Serializable;

public class InvalidIdException extends Exception implements Serializable {

	private String id;
	private String message;

	public InvalidIdException() {
	}
	
	public InvalidIdException(String id) {
		this.id = id;
	}
	
	public InvalidIdException(String id, String message) {
		this.id = id;
		this.message = message;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getMessage() {
		return this.message;
	}
}

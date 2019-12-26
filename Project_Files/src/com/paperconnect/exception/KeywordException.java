package com.paperconnect.exception;

import java.io.Serializable;

public class KeywordException extends Exception implements Serializable {

	private String keyword;
	private String message;

	public KeywordException() {
	}

	public KeywordException(String keyword) {
		this.keyword = keyword;
	}

	public KeywordException(String keyword, String message) {
		this.keyword = keyword;
		this.message = message;
	}
	
	public String getKeyword() {
		return this.keyword;
	}

	public String getMessage() {
		return this.message;
	}
}

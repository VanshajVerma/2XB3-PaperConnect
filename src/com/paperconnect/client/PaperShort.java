package com.paperconnect.client;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;

public class PaperShort implements Serializable {

	private ArrayList<String> fields;

	public PaperShort() {
		this.fields = new ArrayList<String>();
	}

	public PaperShort(ArrayList<String> fields) {
		this.fields = (ArrayList<String>) fields.clone();
	}

	public PaperShort(String[] fields) {
		this.fields = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			addField(fields[i]);
		}
	}

	public void addField(String a) {
		fields.add(a);
	}

	public String getField(PaperFields field) {
		return fields.get(field.ordinal());
	}
}

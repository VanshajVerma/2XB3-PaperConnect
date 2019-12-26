package com.paperconnect.client;

import java.util.ArrayList;

//Similar to Paper.java, but has lists of IDs and titles with a compareTo() method that compares keywords lexicographically
public class LookupTableLine implements Comparable<LookupTableLine> {
	private String keyword;
	private String rightHalf;
	private ArrayList<PaperShort> paperData;

	public LookupTableLine(String keyword) {
		this.keyword = keyword;
		this.rightHalf = "";
		this.paperData = new ArrayList<PaperShort>();
	}

	public LookupTableLine(String keyword, String rightHalf) {
		this.keyword = keyword;
		this.rightHalf = rightHalf;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getRightHalf() {
		return this.rightHalf;
	}
	
	public ArrayList<PaperShort> getData(){
		return this.paperData;
	}
	
	public void addPaperData(String[] data) {
		paperData.add(new PaperShort(data));
	}

	// don't use toString() method. generates strings that are too big for java to
	// handle
	public String toString() {
		StringBuilder x = new StringBuilder(keyword + " = " + rightHalf);
		for(PaperShort p:paperData) {
			x.append(p.toString());
		}
		return x.toString();
	}

	@Override
	public int compareTo(LookupTableLine j) {
		return (keyword.compareTo(j.keyword));
	}
}

package com.paperconnect.util;

import java.util.ArrayList;

import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperFields;
public class Search {

	public static Paper binarySearchID(ArrayList<Paper> list, String id) {
		int min = 0;
		int max = list.size() - 1;
		int mid = 0;
		String midString = "";
		while (max >= min) {
			mid = min + (max - min) / 2;
			midString = list.get(mid).getField(PaperFields.ID);
			if (id.compareTo(midString) == 0) {
				return list.get(mid);
			} else if (id.compareTo(midString) > 0) {
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}
		return null;
	}

	public static Paper sequentialSearchID(ArrayList<Paper> list, String id) {
		for (Paper p : list) {
			if (p.getField(PaperFields.ID).equals(id)) {
				return p;
			}
		}
		return null;
	}

	public static LookupTableLine binarySearchKeyword(ArrayList<LookupTableLine> list, String keyword) {
		int min = 0;
		int max = list.size() - 1;
		int mid = 0;
		String midString = "";
		while (max >= min) {
			mid = min + (max - min) / 2;
			midString = list.get(mid).getKeyword();
			if (keyword.compareTo(midString) == 0) {
				return list.get(mid);
			} else if (keyword.compareTo(midString) > 0) {
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}
		return null;
	}
}

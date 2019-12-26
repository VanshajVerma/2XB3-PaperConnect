package com.paperconnect.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.paperconnect.client.LookupTableLine;
import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperFields;
import com.paperconnect.client.PaperShort;
import com.paperconnect.graph.BreadthFirstSearch;
import com.paperconnect.graph.DiGraph;
import com.paperconnect.graph.GraphConstruction;
import com.paperconnect.util.Search;

public class DataServer {

	public static void init() {
		System.out.println("LOOKUP_TABLE INITIALIZATION STARTING");
		LookupTable.init();
		System.out.println("LOOKUP_TABLE INITIALIZATION COMPLETE");
		System.out.println("-------------------------------------");
		System.out.println("PAPER_LIST INITIALIZATION STARTING");
		PaperList.init();
		System.out.println("PAPER_LIST INITIALIZATION COMPLETE");
		System.out.println("-------------------------------------");
	}

	public static class PaperList {
		public static ArrayList<Paper> papers;

		public static void init() {
			readPaperFile("data/ap_paperList.txt");
		}

		private static void readPaperFile(String fileName) {
			String line = null, id, title, abst, author, publishDate;
			int citations;
			JSONObject obj;
			long start = System.nanoTime();
			papers = new ArrayList<Paper>();

			try {
				FileReader fileReader = new FileReader(fileName);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				int counter = 0;
				while ((line = bufferedReader.readLine()) != null) { // Get paper node from line in input file
					obj = (JSONObject) new JSONParser().parse(line);

					// Get id of paper
					id = obj.get("id").toString();

					// Get title of paper
					title = obj.get("title").toString();

					// Get abstract of paper
					try {
						abst = obj.get("abstract").toString();
					} catch (NullPointerException e) {
						abst = "N/A";
					}

					// Get number of citations of paper
					citations = (int)((long) obj.get("n_citation"));

					ArrayList<String> references = new ArrayList<String>();

					try {
						// get all references of paper
						Iterator<String> iterator = ((JSONArray) obj.get("references")).iterator();
						while (iterator.hasNext()) {
							references.add(iterator.next());
						}
					} catch (NullPointerException e) {

					}

					// Get author of paper
					try {
						author = obj.get("author").toString();
					} catch (NullPointerException e) {
						author = "N/A";
					}

					// get publish date of paper
					try {
						publishDate = obj.get("year").toString();
					} catch (NullPointerException e) {
						publishDate = "N/A";
					}
					// Load paper data into PaperADT and store it in list of PaperADTs
					papers.add(new Paper(id, title, abst, references, author, publishDate, citations));
					obj = null;
					counter++;
					if(counter % 100000 == 0) {
						System.out.println(counter + "->" + (System.nanoTime() - start) / 1000000000.0 + " seconds.");
						System.gc();
					}
				}
				System.out.println("Done in " + (System.nanoTime() - start) / 1000000000.0 + " seconds." + counter +" nodes");
				bufferedReader.close();
			} catch (

			FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}

		public static Paper retrievePaper(String id) {
			DiGraph graph = GraphConstruction.Graph(id, 3, 2);
			String tree = BreadthFirstSearch.getGraphJSONString(graph, graph.getRoot(), 3, 2);
			Paper result = graph.getRoot();
			result.setField(PaperFields.TREE, tree);
			return result;
		}
	}

	public static class LookupTable {
		static ArrayList<LookupTableLine> lookupTable;

		public static void init() {
			readLookupTable("data/ap_lookup.txt");
		}

		private static void readLookupTable(String fileName) {
			String line = null;
			JSONObject keywordLine;
			JSONArray paperList;
			JSONObject paper;
			lookupTable = new ArrayList<LookupTableLine>();
			long start = System.nanoTime();
			int count = 0;
			LookupTableLine tableLine = null;
			try {
				FileReader fileReader = new FileReader(fileName);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				while (true) {
					try {
						line = bufferedReader.readLine();
						if (line == null) {
							break;
						}
						HashSet<String> uniqueIDs = new HashSet<String>();
						keywordLine = (JSONObject) new JSONParser().parse(line);
						paperList = (JSONArray) keywordLine.get("paper_list");
						tableLine = new LookupTableLine(keywordLine.get("keyword").toString());

						for (int i = 0; i < paperList.size(); i++) {
							paper = (JSONObject) paperList.get(i);
							if (!uniqueIDs.contains(paper.get("id").toString())) {
								tableLine.addPaperData(
										new String[] { paper.get("id").toString(), paper.get("title").toString(),
												paper.get("author").toString(), paper.get("year").toString() });
								uniqueIDs.add(paper.get("id").toString());
							}
						}
						lookupTable.add(tableLine);
						count++;
					} catch (Exception f) {
						continue;
					}
				}
				System.out.println(
						"Done in " + (System.nanoTime() - start) / 1000000000.0 + " seconds. " + (count) + " nodes.");
				bufferedReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public static ArrayList<PaperShort> retrievePapers(String keyword) {
			LookupTableLine result = Search.binarySearchKeyword(lookupTable, keyword);
			if (result == null) {
				return null;
			}
			return result.getData();
		}
	}

}

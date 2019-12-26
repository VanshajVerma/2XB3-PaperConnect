package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.Collections;

import com.paperconnect.client.Paper;
import com.paperconnect.server.DataServer;
import com.paperconnect.util.Search;

public class GraphConstruction {

	private static void buildGraph(String id, int height, int width,  DiGraph citeGraph) {
		Paper paper = Search.binarySearchID(DataServer.PaperList.papers, id);
		int counter = width;
		String source = null;

		if (height == 0 || paper == null) {
			return;
		}
		
		if(paper.getReferences().size() == 0) {
			citeGraph.addVertex(id);
			return;
		}
		
		ArrayList<String> references = paper.getReferences();
		Collections.sort(references);
		citeGraph.addVertex(id);

		for (int i = 0; i < references.size() && counter > 0; i++) {
			source = references.get(i);
			paper = Search.binarySearchID(DataServer.PaperList.papers, source);
			if(paper == null)
				continue;
			citeGraph.addCiteEdge(id, paper);
			buildGraph(source, width, height - 1, citeGraph);
			counter--;
		}

		return;
	}

	public static DiGraph Graph(String id, int height, int width) {
		Paper root = Search.binarySearchID(DataServer.PaperList.papers, id);
		DiGraph citeGraph = new DiGraph(root);
		buildGraph(id, width, height, citeGraph);
		return citeGraph;
	}
}
package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.Hashtable;

import com.paperconnect.client.Paper;

public class DiGraph {
	private Hashtable<String, ArrayList<Paper>> citeGraph;
	private Paper root;

	public DiGraph(Paper root) {
		this.citeGraph = new Hashtable<String, ArrayList<Paper>>();
		this.root = root; 
	}
	
	public void addVertex(String vertex) {
		citeGraph.put(vertex, new ArrayList<Paper>());
	}
	
	public void addCiteEdge(String vertex, Paper edge) {
		citeGraph.get(vertex).add(edge);
	}
	
	public Hashtable<String, ArrayList<Paper>> getGraph() {
		return citeGraph;
	}
	
	public ArrayList<Paper> getChildren(String id){
		return citeGraph.get(id);
	}
	
	public Paper getRoot() {
		return root;
	}
}

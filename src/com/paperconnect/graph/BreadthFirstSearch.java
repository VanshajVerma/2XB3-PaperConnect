package com.paperconnect.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperFields;

public class BreadthFirstSearch {

	public static ArrayList<Paper> compute(DiGraph graph, Paper startNode, int maxDepth, int maxChildren) {

		ArrayList<Paper> Result = new ArrayList<Paper>();
		Paper current;
		ArrayList<Paper> neighbors;

		Queue<Paper> queue = new LinkedList<>();
		queue.add(startNode);
		queue.add(null);

		startNode.setVisited(true);
		int level = 0;

		while (!queue.isEmpty()) {
			current = queue.poll();
			Result.add(current);

			if (current == null) {
				level++;
				if (level >= maxDepth) {
					break;
				}

				queue.add(null);
				if (queue.peek() == null)
					break; // when you have two consecutive null in the queue, this means you are at the
							// end of the queue.
				else
					continue;
			}

			neighbors = graph.getChildren(current.getField(PaperFields.ID));
			for (int i = 0; i < Math.min(maxChildren, neighbors.size()); i++) {

				if (neighbors != null && neighbors.get(i) != null && neighbors.get(i).getVisited() != true) {
					queue.add(neighbors.get(i));
					// Result.add(neighbors.get(i));
					neighbors.get(i).setVisited(true);
				}
			}
		}
		return Result;
	}

	public static String getGraphJSONString(DiGraph graph, Paper startNode, int maxDepth, int maxChildren) {
		int x = 0;
		int y = 0;
		JSONObject obj = new JSONObject();
		JSONObject newObj;
		JSONArray nodes = new JSONArray();
		JSONArray edges = new JSONArray();
		ArrayList<Paper> children;
		ArrayList<Paper> ans = compute(graph, startNode, maxDepth, maxChildren);
		int counter = 0;
		for (Paper p : ans) {
			if (p == null) {
				x = 0;
				y++;
			} else {
				newObj = new JSONObject();
				newObj.put("id", p.getField(PaperFields.ID));
				newObj.put("label", p.getField(PaperFields.TITLE));
				newObj.put("color", "#008cc2");
				newObj.put("x", x);
				newObj.put("y", y);
				newObj.put("size", 3);
				nodes.add(newObj);
				children = graph.getChildren(p.getField(PaperFields.ID));
				if (children != null) {
					for (int i = 0; i < children.size(); i++) {
						if (!children.get(i).getVisited()) {
							continue;
						}
						newObj = new JSONObject();
						newObj.put("id", String.valueOf(counter++));
						newObj.put("source", p.getField(PaperFields.ID));
						newObj.put("color", "#dc143c");
						newObj.put("target", children.get(i).getField(PaperFields.ID));
						edges.add(newObj);
					}
				}
				x++;
				p.setVisited(false);
			}
		}
		obj.put("nodes", nodes);
		obj.put("edges", edges);
		return obj.toJSONString();
	}
}
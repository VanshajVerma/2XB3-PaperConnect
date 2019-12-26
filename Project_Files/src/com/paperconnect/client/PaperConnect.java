package com.paperconnect.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.paperconnect.exception.InvalidIdException;
import com.paperconnect.exception.KeywordException;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PaperConnect implements EntryPoint {

	private ArrayList<PaperShort> papers = new ArrayList<PaperShort>();
	private PaperServiceAsync paperSvc = GWT.create(PaperService.class);
	private Label errorMsgLabel = new Label();

	public void onModuleLoad() {

		// Associate the Main panel with the HTML host page
		RootPanel.get("graphContainer").setVisible(false);
		RootPanel.get("errorMessage").add(errorMsgLabel);

		searchListener(this);
		tableListener(this);
	}

	private void addPapers(ArrayList<PaperShort> paperLs) {
		papers.clear();
		for (int i = 0; i < paperLs.size(); i++) {

			if (papers.contains(paperLs.get(i)))
				return;

			// Add the paper to the table
			papers.add(paperLs.get(i));
		}
	}

	private void retrievePaper(String title) {
		
		String id = "";
		for(PaperShort paper : papers) {
			if(paper.getField(PaperFields.TITLE).equals(title)) {
				id = paper.getField(PaperFields.ID);
				System.out.println(id+" "+paper.getField(PaperFields.TITLE));
				break;
			}
		}
		
		// Initialize the service proxy
		if (paperSvc == null) {
			paperSvc = GWT.create(PaperService.class);
		}

		// Set up the callback object.
		AsyncCallback<Paper> callback = new AsyncCallback<Paper>() {
			public void onFailure(Throwable caught) {
				errorMsgLabel.setText("Id:" + ((InvalidIdException) caught).getId() + " not found");
				errorMsgLabel.setVisible(true);
			}

			@Override
			public void onSuccess(Paper result) {
				initPaperTable("paperTable", true);
				JSONObject obj = new JSONObject();
				obj.put("title", new JSONString(result.getField(PaperFields.TITLE)));
				obj.put("author", new JSONString(result.getField(PaperFields.AUTHOR)));
				obj.put("publishDate", new JSONString(result.getField(PaperFields.PUBLISH_DATE)));
				obj.put("abstract", new JSONString(result.getField(PaperFields.ABSTRACT)));
				addPaperInTable(obj, "paperTable", true);
				JSONValue val = JSONParser.parseStrict(result.getField(PaperFields.TREE));
				JSONObject graphObj = val.isObject();
				RootPanel.get("graphContainer").setVisible(true);
				displayGraph(graphObj, "graphContainer");
				errorMsgLabel.setVisible(false);
			}
		};

		// Make the call to the paper service
		paperSvc.retrievePaper(id, callback);
	}

	private void retrievePaperLs(String keyword) {
		// Initialize the service proxy
		if (paperSvc == null) {
			paperSvc = GWT.create(PaperService.class);
		}

		// Set up the callback object.
		AsyncCallback<ArrayList<PaperShort>> callback = new AsyncCallback<ArrayList<PaperShort>>() {
			public void onFailure(Throwable caught) {
				errorMsgLabel.setText("Keyword:" + ((KeywordException) caught).getKeyword() + " not valid");
				errorMsgLabel.setVisible(true);
			}

			public void onSuccess(ArrayList<PaperShort> result) {
				RootPanel.get("graphContainer").setVisible(false);
				initPaperTable("paperTable", false);
				addPapers(result);
				for (PaperShort paper : result) {
					JSONObject obj = new JSONObject();
					obj.put("title", new JSONString(paper.getField(PaperFields.TITLE)));
					obj.put("author", new JSONString(paper.getField(PaperFields.AUTHOR)));
					obj.put("publishDate", new JSONString(paper.getField(PaperFields.PUBLISH_DATE)));
					addPaperInTable(obj, "paperTable", false);
				}
				addClickHandlersToTable();
				errorMsgLabel.setVisible(false);
			}
		};

		// Make the call to the paper service
		paperSvc.retrievePaperLs(keyword, callback);
	}

	public native void initPaperTable(String elementId, boolean singlePaper) /*-{
		$wnd.initPaperTable(elementId, singlePaper);
	}-*/;

	public native void displayGraph(JSONObject obj, String elementId) /*-{
		$wnd.drawGraph(obj, elementId);
	}-*/;

	public native void addPaperInTable(JSONObject obj, String elementId, boolean singlePaper) /*-{
		$wnd.addPaperToTable(obj, elementId, singlePaper);
	}-*/;

	public native void addClickHandlersToTable() /*-{
		$wnd.addRowHandlers();
	}-*/;

	public native void searchListener(PaperConnect pc) /*-{
		$wnd.onSearchEnter = function(keyword) {
			pc.@com.paperconnect.client.PaperConnect::retrievePaperLs(Ljava/lang/String;)(keyword);
		};
	}-*/;

	public native void tableListener(PaperConnect pc) /*-{
		$wnd.onTableClick = function(title) {
			pc.@com.paperconnect.client.PaperConnect::retrievePaper(Ljava/lang/String;)(title);
		};
	}-*/;
}

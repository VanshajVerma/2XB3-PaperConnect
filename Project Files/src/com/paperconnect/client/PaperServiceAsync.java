package com.paperconnect.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PaperServiceAsync {

	void retrievePaperLs(String keyword, AsyncCallback<ArrayList<PaperShort>> callback);
	void retrievePaper(String id, AsyncCallback<Paper> callback);
}

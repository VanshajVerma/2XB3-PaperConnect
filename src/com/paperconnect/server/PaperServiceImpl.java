package com.paperconnect.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.paperconnect.client.Paper;
import com.paperconnect.client.PaperService;
import com.paperconnect.client.PaperShort;
import com.paperconnect.exception.InvalidIdException;
import com.paperconnect.exception.KeywordException;

public class PaperServiceImpl extends RemoteServiceServlet implements PaperService {

	@Override
	public ArrayList<PaperShort> retrievePaperLs(String keyword) throws KeywordException {
		ArrayList<PaperShort> result = DataServer.LookupTable.retrievePapers(keyword);
		if (result == null)
			throw new KeywordException(keyword);

		return result;
	}

	@Override
	public Paper retrievePaper(String id) throws InvalidIdException {
		Paper result = DataServer.PaperList.retrievePaper(id);
		if (result == null)
			throw new InvalidIdException(id);
		
		return result;
	}
}

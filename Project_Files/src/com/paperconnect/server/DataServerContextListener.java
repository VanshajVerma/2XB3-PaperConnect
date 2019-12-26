package com.paperconnect.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DataServerContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		DataServer.init();
	}

}

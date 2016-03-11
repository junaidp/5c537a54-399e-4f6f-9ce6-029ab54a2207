package com.currencies.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;



public class CurrencyMonitor implements EntryPoint {
	
	
	public void onModuleLoad() {
		CurrencyServiceAsync rpcService = GWT.create(CurrencyService.class);
	    HandlerManager eventBus = new HandlerManager(null);
	    AppController appViewer = new AppController(rpcService, eventBus);
	    appViewer.go(RootPanel.get("bodyContainer"));
	}
}

package com.currencies.client.presenter;


import com.currencies.client.CurrencyServiceAsync;
import com.currencies.client.event.MonitorEvent;
import com.currencies.client.view.ApplicationConstants;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class MainPresenter implements Presenter 

{
	
	private final Display display;
	private final CurrencyServiceAsync rpcService;
	private final HandlerManager eventBus;

	public interface Display 
	{
		Widget asWidget();
		TextBox getTxtGetIn();
		Button getBtnGetIn();
	
	}  

	public MainPresenter(CurrencyServiceAsync rpcService, HandlerManager eventBus, Display view) 
	{
		this.display = view;
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		
	}

	public void go(HasWidgets container) 
	{
		container.clear();
		container.add(display.asWidget());
		bind();
	}

	private void bind() {
		setHandlers();
		RootPanel.get("loadingMessage").setVisible(false);
	
	}

	@Override
	public void setHandlers() {
		
	
		display.getBtnGetIn().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				display.getBtnGetIn().setEnabled(false);
				eventBus.fireEvent(new MonitorEvent());
			}});
		
	}
	
	
	
	
	
	
}

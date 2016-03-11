package com.currencies.client;


import com.currencies.client.event.MonitorEvent;
import com.currencies.client.event.MonitorEventHandler;
import com.currencies.client.presenter.MainPresenter;
import com.currencies.client.presenter.MonitorPresenter;
import com.currencies.client.presenter.Presenter;
import com.currencies.client.view.ApplicationConstants;
import com.currencies.client.view.MainView;
import com.currencies.client.view.MonitorView;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;



public class AppController implements Presenter, ValueChangeHandler<String> {
	private final HandlerManager eventBus;

	private final CurrencyServiceAsync rpcService; 
	private HasWidgets container;
	private String createPasswordtoken ="";
	private String beanName ;
	
	Presenter presenter = null;

	public AppController(CurrencyServiceAsync rpcService, HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.rpcService = rpcService;
	

		bind();
	}


	private void bind() {
		History.addValueChangeHandler(this);

		eventBus.addHandler(MonitorEvent.TYPE,
				new MonitorEventHandler() {
			public void onMain(MonitorEvent event) {
					History.newItem(ApplicationConstants.TOKEN_MONITOR);
			}
		}); 
		
	
	}

	public void go(final HasWidgets container) {
		this.container = container;
		

		if ("".equals(History.getToken())) {
			History.newItem(ApplicationConstants.TOKEN_MAIN);
		}
		else {
			History.fireCurrentHistoryState();
		}
	}
	
	

	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();

		if (token != null) {
			presenter = null;

			if (token.equals(ApplicationConstants.TOKEN_MAIN)) {
				presenter = new MainPresenter(rpcService, eventBus, new MainView());
				if (presenter != null) {
					presenter.go(container);
				}
			}
			
			else if (token.equals(ApplicationConstants.TOKEN_MONITOR)) {
				presenter = new MonitorPresenter(rpcService, eventBus, new MonitorView());
				if (presenter != null) {
					presenter.go(container);
				}
			}

		
		}
	} 
	
	private void setContainer(HasWidgets container) {
		this.container = container;
		this.container.clear();
	}


	@Override
	public void setHandlers() {
			
	}

}

package com.currencies.client.presenter;


import java.util.ArrayList;
import java.util.Date;

import com.currencies.client.CurrencyServiceAsync;
import com.currencies.client.event.MonitorEvent;
import com.currencies.client.view.ApplicationConstants;
import com.currencies.client.view.DisplayAlert;
import com.currencies.shared.entities.CurrenciesEntity;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class MonitorPresenter implements Presenter 

{

	private final Display display;
	private final CurrencyServiceAsync rpcService;
	private final HandlerManager eventBus;
	private boolean currenciesFetched = false;

	public interface Display 
	{
		Widget asWidget();
		Image getImgRefresh();
		CellTable<CurrenciesEntity> getTableCurrencies();
		Button getBtnAddCurrency();
		TextBox getTxtCurrenyName();
		Column<CurrenciesEntity, String> getDeleteColumn();
		Label getLblLastCalculation();

	}  

	public MonitorPresenter(CurrencyServiceAsync rpcService, HandlerManager eventBus, Display view) 
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
		fetchCurrencyCalculations();
	}

	private void bind() {
		setHandlers();
	}

	@Override
	public void setHandlers() {

		display.getImgRefresh().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fetchCurrencyCalculations();
			}
		});

		display.getBtnAddCurrency().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addCurrency();
			}
		});
		
		display.getDeleteColumn().setFieldUpdater(new FieldUpdater<CurrenciesEntity, String>() {

			@Override
			public void update(int index, CurrenciesEntity currenciesEntity , String value) {
				deleteCurreny(currenciesEntity);
			}


		});
	}

	private void deleteCurreny(CurrenciesEntity currenciesEntity) {
		rpcService.deleteCurrency(currenciesEntity.getCurrencyId(), new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught+"");
			}

			@Override
			public void onSuccess(String result) {
				new DisplayAlert(result);
				fetchCurrencyCalculations();
			}
		});

	}

	private void addCurrency() {
		String currency = display.getTxtCurrenyName().getText();
		if(verifyCurrency(currency)){
			rpcService.addCurrency(currency, new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Currency Name invalid, Please add a valid currency symbol");
				}

				@Override
				public void onSuccess(String result) {
					new DisplayAlert(result);
					fetchCurrencyCalculations();
				}
			});
		}
	}

	private boolean verifyCurrency(String currency) {
		return currency.matches("[a-zA-Z]+");
	}

	private void fetchCurrencyCalculations() {

		rpcService.fetchCurrencyCalculations(new AsyncCallback<ArrayList<CurrenciesEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught+"");
			}

			@Override
			public void onSuccess(ArrayList<CurrenciesEntity> result) {
				display.getTableCurrencies().setRowData(0, result);
				display.getTableCurrencies().setRowCount(result.size());
				display.getLblLastCalculation().setText(new Date()+"");
				if(currenciesFetched){
					new DisplayAlert("Currencies calculations Refreshed");
				}	
				currenciesFetched = true;
			
			}
		});

	}
}

package com.currencies.client.presenter;


import java.util.ArrayList;
import java.util.Date;

import com.currencies.client.CurrencyServiceAsync;
import com.currencies.client.view.ApplicationConstants;
import com.currencies.client.view.DisplayAlert;
import com.currencies.client.view.LoadingPopup;
import com.currencies.shared.entities.CurrenciesEntity;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
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
		Label getLblError();

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
					display.getLblError().setText(ApplicationConstants.CURRENCY_INVALID);
				}

				@Override
				public void onSuccess(String result) {
					if(result.equals(ApplicationConstants.CURRENCY_ALREADY_ADDED)){
						display.getLblError().setText(result);
					}else{
					display.getLblError().setText("");
					new DisplayAlert(result);
					fetchCurrencyCalculations();
					}
				}
			});
		}
		else{
			display.getLblError().setText(ApplicationConstants.CURRENCY_INVALID);
		}
	}

	private boolean verifyCurrency(String currency) {
		return currency.matches("[a-zA-Z]+");
	}

	private void fetchCurrencyCalculations() {
		final LoadingPopup loadingPopup = new LoadingPopup();
		loadingPopup.display();
		rpcService.fetchCurrencyCalculations(new AsyncCallback<ArrayList<CurrenciesEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught+"");
				if(loadingPopup!=null){
					loadingPopup.remove();
				}
			}

			@Override
			public void onSuccess(ArrayList<CurrenciesEntity> result) {
				
				if(loadingPopup!=null){
					loadingPopup.remove();
				}
				display.getLblError().setText("");
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

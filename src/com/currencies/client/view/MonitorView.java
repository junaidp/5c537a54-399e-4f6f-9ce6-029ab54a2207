package com.currencies.client.view;

import com.currencies.client.presenter.MonitorPresenter.Display;
import com.currencies.shared.entities.CurrenciesEntity;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MonitorView extends Composite implements Display {

	private static MonitorViewUiBinder uiBinder = GWT
			.create(MonitorViewUiBinder.class);

	interface MonitorViewUiBinder extends UiBinder<Widget, MonitorView> {
	}

	
	private Column<CurrenciesEntity, String> currencyNameColumn;
	private Column<CurrenciesEntity, String> hkdPriceColumn;
	private Column<CurrenciesEntity, String> deleteColumn;
	private CellTable<CurrenciesEntity> tableCurrencies = new CellTable<CurrenciesEntity>();
	
	@UiField
	TextBox txtCurrenyName;
	@UiField
	Button btnAddCurrency;
	@UiField
	VerticalPanel tableContainer;
	@UiField 
	Label lblLastCalculation;
	@UiField
	Image imgRefresh;
	@UiField
	Label lblError;

	public MonitorView() {
		initWidget(uiBinder.createAndBindUi(this));
		tableCurrencies.setWidth("300px");
		tableContainer.add(tableCurrencies);
		txtCurrenyName.getElement().setPropertyString("placeholder", ApplicationConstants.ENTER_CURRENCY);
		tableLayout();
	}
	
	public void tableLayout(){
		
		tableCurrencies.setEmptyTableWidget(new HTML("No Record found"));
		tableCurrencies.setRowCount(0);

		currencyNameColumn = new Column<CurrenciesEntity, String>(new TextCell()) {
			@Override
			public String getValue(CurrenciesEntity object) {
				return object.getCurrencyName();
			}
		};
		tableCurrencies.addColumn(currencyNameColumn,"Symbol");
		
		hkdPriceColumn = new Column<CurrenciesEntity, String>(new TextCell()) {
			@Override
			public String getValue(CurrenciesEntity object) {
				return object.getPriceInHKD()+"";
			}
		};
		tableCurrencies.addColumn(hkdPriceColumn,"price in HKD");
		
		deleteColumn = new Column<CurrenciesEntity, String>(new ButtonCell()) {
			@Override
			public String getValue(CurrenciesEntity object) {
				return "Delete";
			}
		};
		tableCurrencies.addColumn(deleteColumn,"");
		
	}

	public Image getImgRefresh() {
		return imgRefresh;
	}

	public void setImgRefresh(Image imgRefresh) {
		this.imgRefresh = imgRefresh;
	}

	public CellTable<CurrenciesEntity> getTableCurrencies() {
		return tableCurrencies;
	}

	public void setTableCurrencies(CellTable<CurrenciesEntity> tableCurrencies) {
		this.tableCurrencies = tableCurrencies;
	}

	public TextBox getTxtCurrenyName() {
		return txtCurrenyName;
	}

	public void setTxtCurrenyName(TextBox txtCurrenyName) {
		this.txtCurrenyName = txtCurrenyName;
	}

	public Button getBtnAddCurrency() {
		return btnAddCurrency;
	}

	public void setBtnAddCurrency(Button btnAddCurrency) {
		this.btnAddCurrency = btnAddCurrency;
	}

	public Column<CurrenciesEntity, String> getDeleteColumn() {
		return deleteColumn;
	}

	public void setDeleteColumn(Column<CurrenciesEntity, String> deleteColumn) {
		this.deleteColumn = deleteColumn;
	}

	public Label getLblLastCalculation() {
		return lblLastCalculation;
	}

	public void setLblLastCalculation(Label lblLastCalculation) {
		this.lblLastCalculation = lblLastCalculation;
	}

	public Label getLblError() {
		return lblError;
	}

	public void setLblError(Label lblError) {
		this.lblError = lblError;
	}

	
	

}

package com.currencies.client.view;

import com.currencies.client.presenter.MainPresenter.Display;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class MainView extends Composite implements Display {

	private static SubscriptionVerificationViewUiBinder uiBinder = GWT
			.create(SubscriptionVerificationViewUiBinder.class);

	interface SubscriptionVerificationViewUiBinder extends
			UiBinder<Widget, MainView> {
	}
	@UiField TextBox txtGetIn;
	@UiField Button btnGetIn;
	
	public MainView() {
		initWidget(uiBinder.createAndBindUi(this));
		txtGetIn.getElement().setPropertyString("placeholder", ApplicationConstants.GETIN_PLACE_HOLDER);
	
	}

	public TextBox getTxtGetIn() {
		return txtGetIn;
	}

	public void setTxtGetIn(TextBox txtGetIn) {
		this.txtGetIn = txtGetIn;
	}

	public Button getBtnGetIn() {
		return btnGetIn;
	}

	public void setBtnGetIn(Button btnGetIn) {
		this.btnGetIn = btnGetIn;
	}




}

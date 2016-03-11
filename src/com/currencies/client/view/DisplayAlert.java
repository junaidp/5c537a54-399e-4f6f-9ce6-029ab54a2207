package com.currencies.client.view;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Label;

public class DisplayAlert {
	public DisplayAlert(String message){
		final DecoratedPopupPanel popup = new DecoratedPopupPanel();
		popup.setWidget(new Label(message));
		popup.center();
		Timer time = new Timer() {
			public void run() {
				popup.removeFromParent();
			}

		};//timer for showing the popup of "update"
		time.schedule(1500);
	}
}

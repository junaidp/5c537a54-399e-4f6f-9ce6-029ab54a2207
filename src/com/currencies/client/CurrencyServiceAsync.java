package com.currencies.client;

import java.util.ArrayList;

import com.currencies.shared.entities.CurrenciesEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface CurrencyServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void fetchCurrencyCalculations(AsyncCallback<ArrayList<CurrenciesEntity>> callback);
	void addCurrency(String currencyName, AsyncCallback<String> callback);
	void deleteCurrency(int currencyId, AsyncCallback<String> callback);
}

package com.currencies.client;

import java.util.ArrayList;

import com.currencies.shared.entities.CurrenciesEntity;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface CurrencyService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	ArrayList<CurrenciesEntity> fetchCurrencyCalculations()throws Exception;
	String addCurrency(String currencyName) throws Exception;
	String deleteCurrency(int currencyId)throws Exception;
	 
}

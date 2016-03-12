package com.currencies.server;

import java.util.ArrayList;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.currencies.client.CurrencyService;
import com.currencies.database.SQLHelper;
import com.currencies.shared.entities.CurrenciesEntity;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CurrencyServiceImpl extends RemoteServiceServlet implements
		CurrencyService {

	public String greetServer(String input) throws IllegalArgumentException {
		return "";
	}

	@Override
	public ArrayList<CurrenciesEntity> fetchCurrencyCalculations() throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		SQLHelper rdbHelper = (SQLHelper) ctx.getBean("ManagerCurrencies");
		return rdbHelper.fetchCurrencyCalculations();
		
	}

	@Override
	public String addCurrency(String currencyName) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		SQLHelper rdbHelper = (SQLHelper) ctx.getBean("ManagerCurrencies");
		return rdbHelper.addCurrency(currencyName);
	}

	@Override
	public String deleteCurrency(int currencyId) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		SQLHelper rdbHelper = (SQLHelper) ctx.getBean("ManagerCurrencies");
		return rdbHelper.deleteCurrency(currencyId);
	}
	
	
}

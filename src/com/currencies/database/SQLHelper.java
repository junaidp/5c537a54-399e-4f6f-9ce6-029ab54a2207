package com.currencies.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.currencies.shared.entities.CurrenciesEntity;

public class SQLHelper {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public String addCurrency(String currencyName)throws Exception{
	
		Session session = null;
		try{
			session = sessionFactory.openSession();
			getRateInHK(currencyName);
			CurrenciesEntity curreniesEntity = new CurrenciesEntity();
			curreniesEntity.setCurrencyName(currencyName);
			session.save(curreniesEntity);
			session.flush();
			return "Currency Added";
		}catch(Exception ex){
			throw new Exception("Currency Invalid, Please use a valid currency Symbol: "+ ex);
		}
	}
	
	public String deleteCurrency(int currencyId) throws Exception{
		Session session = null;
		try{
			session = sessionFactory.openSession();
			CurrenciesEntity currenciesEntity = (CurrenciesEntity) session.get(CurrenciesEntity.class, currencyId);
			session.delete(currenciesEntity);
			session.flush();
			return "currency deleted";
		}catch(Exception ex){
			throw new Exception("Error in deleteCurrency: "+ ex);
		}
	}

	public ArrayList<CurrenciesEntity> fetchCurrencyCalculations() throws Exception {
		Session session = null;
		ArrayList<CurrenciesEntity> listCurrencies = new ArrayList<CurrenciesEntity>();
		try{
			session = sessionFactory.openSession();
			Criteria crit = session.createCriteria(CurrenciesEntity.class);
			List rsList = crit.list();

			for(Iterator it=rsList.iterator();it.hasNext();)
			{
				CurrenciesEntity currenciesEntity =  (CurrenciesEntity)it.next();
				float priceInHKD = getRateInHK(currenciesEntity.getCurrencyName());
				currenciesEntity.setPriceInHKD(priceInHKD);
				listCurrencies.add(currenciesEntity);
			}
		return listCurrencies;
	}catch(Exception ex){
		System.out.println("Error in fetchCurrencyCalculations: "+ ex);
		throw new Exception("Error in fetchCurrencyCalculations: "+ ex);
	}
	
}

	private float getRateInHK(String currencyFrom) throws ClientProtocolException, IOException {
		 HttpClient httpclient = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet("http://quote.yahoo.com/d/quotes.csv?s=" + currencyFrom + "HKD" + "=X&f=l1&e=.csv");
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        String responseBody = httpclient.execute(httpGet, responseHandler);
	        httpclient.getConnectionManager().shutdown();
	        return Float.parseFloat(responseBody);
	}
	
}

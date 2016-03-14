package com.currencies.database;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.currencies.client.view.ApplicationConstants;
import com.currencies.shared.entities.CurrenciesEntity;

public class SQLHelper {

	private SessionFactory sessionFactory;
	private String lastMonthformattedDate; // placing these here , to avoid reading whole page on every loop
	Document doc;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public String addCurrency(String currencyName)throws Exception{

		Session session = null;
		try{
			session = sessionFactory.openSession();
			if(currencyAlreadyAdded(currencyName)){
				return ApplicationConstants.CURRENCY_ALREADY_ADDED;
			}
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

	private boolean currencyAlreadyAdded(String currencyName) throws Exception {
		Session session = null;
		try{
			session = sessionFactory.openSession();
			Criteria crit = session.createCriteria(CurrenciesEntity.class);
			crit.add(Restrictions.eq("currencyName", currencyName));
			if(crit.list().size()>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception ex){
			throw new Exception("currencyAlreadyAdded check failed: "+ ex);
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
				float lastMonthPrice =  (float) getHistoricalRate(currenciesEntity.getCurrencyName());
				calculateChange(currenciesEntity, priceInHKD, lastMonthPrice);
				listCurrencies.add(currenciesEntity);
			}
			return listCurrencies;
		}catch(Exception ex){
			System.out.println("Error in fetchCurrencyCalculations: "+ ex);
			throw new Exception("Error in fetchCurrencyCalculations: "+ ex);
		}

	}

	private void calculateChange(CurrenciesEntity currenciesEntity,
			float priceInHKD, float lastMonthPrice) {
		float diff  = priceInHKD/lastMonthPrice;
		float diffPercent = diff * 100;
		float percent =  diffPercent - 100;
		NumberFormat formatter = new DecimalFormat("#0.0");  
		String formattedPercent = formatter.format(percent);
		String result = percent>0?"UP":"DOWN";
		currenciesEntity.setChange(formattedPercent +"% " + result);
	}

	private float getHistoricalRate(String currencyName) throws IOException {
		float lastMonthRate = 0;
		if(lastMonthformattedDate == null || lastMonthformattedDate.equals("")){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			Date lastMonth = cal.getTime();
			lastMonthformattedDate = new SimpleDateFormat("yyyy-MM-dd").format(lastMonth);
			String url = "http://www.x-rates.com/historical/?from=HKD&amount=1&date="+lastMonthformattedDate+"";
			doc = Jsoup.connect(url).userAgent("Mozilla").get();
		}
		Elements links = doc.select("a[href]");  
		for(int i=0; i< links.size(); i++){
			if(links.get(i).outerHtml().contains("to="+currencyName.toLowerCase()) || links.get(i).outerHtml().contains("to="+currencyName.toUpperCase()) ){
				lastMonthRate = Float.parseFloat(links.get(i).ownText());
				break;
			}
		}
		return lastMonthRate;

	}

	private float getRateInHK(String currencyFrom) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://quote.yahoo.com/d/quotes.csv?s=" + "HKD"+ currencyFrom  + "=X&f=l1&e=.csv");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		String responseBody = httpclient.execute(httpGet, responseHandler);
		httpclient.getConnectionManager().shutdown();

		return Float.parseFloat(responseBody);
	}

}

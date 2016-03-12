package com.currencies.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.hql.ast.tree.RestrictableStatement;
import org.json.JSONObject;

import com.currencies.client.view.ApplicationConstants;
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
				listCurrencies.add(currenciesEntity);
				
				/////FOR HISTORICAL DATA. If we use some paid API or other way , below i will get two rates,get the difference and attache the percentageChange in the entity and send to client
//				float priceInUS = getRateInUS(currenciesEntity.getCurrencyName());
//				float hitoryPriceInUS =  (float) getHistoricalRate(currenciesEntity.getCurrencyName());
//				
//				float diff = priceInUS - hitoryPriceInUS;
//				float percent = (priceInUS/diff) * 100;
//				currenciesEntity.setChange(percent +percent>0?"UP":"DOWN");
			}
		return listCurrencies;
	}catch(Exception ex){
		System.out.println("Error in fetchCurrencyCalculations: "+ ex);
		throw new Exception("Error in fetchCurrencyCalculations: "+ ex);
	}
	
}

	private float getRateInHK(String currencyFrom) throws Exception {
		 HttpClient httpclient = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet("http://quote.yahoo.com/d/quotes.csv?s=" + "HKD"+ currencyFrom  + "=X&f=l1&e=.csv");
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();

	        String responseBody = httpclient.execute(httpGet, responseHandler);
	        httpclient.getConnectionManager().shutdown();
	        
	        return Float.parseFloat(responseBody);
	}
	
	

	
	
	
////////////////////////////////////////////        HISTORY PRICE       ////////////////////////////////////////////////////	
	
//	private float getRateInUS(String currencyFrom) throws Exception {
//		 HttpClient httpclient = new DefaultHttpClient();
//	        HttpGet httpGet = new HttpGet("http://quote.yahoo.com/d/quotes.csv?s=" + "USD"+ currencyFrom  + "=X&f=l1&e=.csv");
//	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//
//	        String responseBody = httpclient.execute(httpGet, responseHandler);
//	        httpclient.getConnectionManager().shutdown();
//	        
//	        return Float.parseFloat(responseBody);
//	}
	
//	private float getRateInHK(String currencyFrom) throws Exception {
//		getJSON("http://www.xe.com/currencytables/?from=HKD&date=2016-02-01", 10000);
//		     String result = sendLiveRequest(currencyFrom);
//	      
//	        return Float.parseFloat(result);
//	}
	
	
    // Trying open connection, but restriction coming up
//	public String getJSON(String url, int timeout) {
//	    HttpURLConnection c = null;
//	    try {
//	        URL u = new URL(url);
//	        c = (HttpURLConnection) u.openConnection();
//	        c.setRequestMethod("GET");
//	        c.setRequestProperty("Content-length", "0");
//	        c.setUseCaches(false);
//	        c.setAllowUserInteraction(false);
//	        c.setConnectTimeout(timeout);
//	        c.setReadTimeout(timeout);
//	        c.connect();
//	        int status = c.getResponseCode();
//
//	        switch (status) {
//	            case 200:
//	            case 201:
//	                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
//	                StringBuilder sb = new StringBuilder();
//	                String line;
//	                while ((line = br.readLine()) != null) {
//	                    sb.append(line+"\n");
//	                }
//	                br.close();
//	                return sb.toString();
//	        }
//
//	    } catch (MalformedURLException ex) {
//	        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//	    } catch (IOException ex) {
//	        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//	    } finally {
//	       if (c != null) {
//	          try {
//	              c.disconnect();
//	          } catch (Exception ex) {
//	             Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//	          }
//	       }
//	    }
//	    return null;
//	}
	
//An other service giving historical rates, but need installations on a machine
//private String sendLiveRequest(String currencyFrom) throws ClientProtocolException, IOException {
//	
//		
//	 HttpClient httpclient = new DefaultHttpClient();
//     HttpGet httpGet = new HttpGet("http://globalcurrencies.xignite.com/xGlobalCurrencies.json/GetHistoricalRate?Symbol=HKDUSD&AsOfDate=3/4/2016&FixingTime=22:00&PriceType=Mid");
//     ResponseHandler<String> responseHandler = new BasicResponseHandler();
//     String responseBody = httpclient.execute(httpGet, responseHandler);
//     httpclient.getConnectionManager().shutdown();
//     return Float.parseFloat(responseBody)+"";
//	
//
//}
//An other service giving historical rates but free for only US as a base currency
//	 public static double getHistoricalRate(String currencyFrom)throws Exception{
//		 String ACCESS_KEY = "647fd60e8a93b2b4fdc37190e643beec";
//		 String BASE_URL = "http://apilayer.net/api/";
//		 String ENDPOINT = "live";
//
//
//		    // this object is used for executing requests to the (REST) API
//		    CloseableHttpClient httpClient = HttpClients.createDefault();
//		      // The following line initializes the HttpGet Object with the URL in order to send a request
//	        HttpGet get = new HttpGet(BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY);
//
//	        try {
//	            CloseableHttpResponse response =  httpClient.execute(get);
//	            HttpEntity entity = response.getEntity();
//
//	            // the following line converts the JSON Response to an equivalent Java Object
//	            JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));
//	            
////	            Calendar cal = Calendar.getInstance();
////	            cal.add(Calendar.MONTH, -1);
////	            Date timeStampDate = cal.getTime();
//	            
//	            Date timeStampDate = new Date((long)(exchangeRates.getLong("timestamp")*1000)); 
//	            
//	          
//	            
//	            response.close();
//	            return exchangeRates.getJSONObject("quotes").getDouble("USD"+currencyFrom);   
//		   
//	        } catch (ClientProtocolException e) {
//	            // TODO Auto-generated catch block
//	            throw e;
//	        } catch (Exception e) {
//	            // TODO Auto-generated catch block
//	            throw e;
//	        } 
//	    }
	
	
	
// An other way for getting historical amounts but PAID	
//	 public static String getHTML(String urlToRead) throws Exception {
//		 
//		 
//	 URL url = new URL("http://jsonrates.com/get/?from=USD&to=EUR?access_key=647fd60e8a93b2b4fdc37190e643beec");
//		 String data = IOUtils.toString(url);
//		 JSONObject json = new JSONObject(data);
//		 Double rate = json.getDouble("rate");
//		 return rate +"";
		 
		/// trying http..but getting same restrictions error. 
//	      StringBuilder result = new StringBuilder();
//	      URL url = new URL(urlToRead);
//	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//	      conn.setRequestMethod("GET");
//	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//	      String line;
//	      while ((line = rd.readLine()) != null) {
//	         result.append(line);
//	      }
//	      rd.close();
//	      return result.toString();
//		 
//		 String url = "http://www.xe.com/currencytables/?from=HKD&date=2016-02-01";
//			
//			URL obj = new URL(url);
//			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//			// optional default is GET
//			con.setRequestMethod("GET");
//
//			//add request header
////			con.setRequestProperty("User-Agent", USER_AGENT);
//
//			int responseCode = con.getResponseCode();
//			System.out.println("\nSending 'GET' request to URL : " + url);
//			System.out.println("Response Code : " + responseCode);
//
//			BufferedReader in = new BufferedReader(
//			        new InputStreamReader(con.getInputStream()));
//			String inputLine;
//			StringBuffer response = new StringBuffer();
//
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			in.close();
//
//			//print result
//			System.out.println(response.toString());
//			return "";
//	   }
	
}

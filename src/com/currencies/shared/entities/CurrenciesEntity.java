package com.currencies.shared.entities;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity

@Table(name="currencies")
public class CurrenciesEntity   implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="currencyId")
	private int currencyId;
	
	@Column(name="currencyName")
	private String currencyName;
	
	
	@Transient
	private float priceInHKD;
	
	
	public CurrenciesEntity(){}


	public int getCurrencyId() {
		return currencyId;
	}


	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}


	public String getCurrencyName() {
		return currencyName;
	}


	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}


	public float getPriceInHKD() {
		return priceInHKD;
	}


	public void setPriceInHKD(float priceInHKD) {
		this.priceInHKD = priceInHKD;
	}


	
}

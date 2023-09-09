package com.playground.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Stocks")
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="name")
	private String name;

	@Column(name="bidPrice")
	private BigDecimal bidPrice;
	
	@Column(name="bidQty")
	private BigDecimal bidQty;
	
	@Column(name="askPrice")
	private BigDecimal askPrice;
	
	@Column(name="askQty")
	private BigDecimal askQty;

	@OneToMany(targetEntity = Transaction.class) 
	private transient List<Transaction> transaction;
	
	public Stock(){
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(BigDecimal bidPrice) {
		this.bidPrice = bidPrice;
	}

	public BigDecimal getBidQty() {
		return bidQty;
	}

	public void setBidQty(BigDecimal bidQty) {
		this.bidQty = bidQty;
	}

	public BigDecimal getAskPrice() {
		return askPrice;
	}

	public void setAskPrice(BigDecimal askPrice) {
		this.askPrice = askPrice;
	}

	public BigDecimal getAskQty() {
		return askQty;
	}

	public void setAskQty(BigDecimal askQty) {
		this.askQty = askQty;
	}

	public List<Transaction> getTransaction() {
		return transaction;
	}

	public void setTransaction(List<Transaction> transaction) {
		this.transaction = transaction;
	}

	@Override
	public String toString() {
		return "Stock [id=" + id + ", name=" + name + ", bidPrice=" + bidPrice + ", bidQty=" + bidQty + ", askPrice="
				+ askPrice + ", askQty=" + askQty + ", transaction=" + transaction + "]";
	}
	
}

package com.playground.model.json;

import java.math.BigDecimal;

public class HoubiStock {

	private String symbol;
	
	private BigDecimal open;
	
	private BigDecimal high;
	
	private BigDecimal low;

	private BigDecimal close;
	
	private BigDecimal amount;
	
	private BigDecimal vol;
	
	private int count;
	
	private BigDecimal bid;
	
	private BigDecimal bidSize;
	
	private BigDecimal ask;
	
	private BigDecimal askSize;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getVol() {
		return vol;
	}

	public void setVol(BigDecimal vol) {
		this.vol = vol;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public BigDecimal getBid() {
		return bid;
	}

	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	public BigDecimal getBidSize() {
		return bidSize;
	}

	public void setBidSize(BigDecimal bidSize) {
		this.bidSize = bidSize;
	}

	public BigDecimal getAsk() {
		return ask;
	}

	public void setAsk(BigDecimal ask) {
		this.ask = ask;
	}

	public BigDecimal getAskSize() {
		return askSize;
	}

	public void setAskSize(BigDecimal askSize) {
		this.askSize = askSize;
	}
}

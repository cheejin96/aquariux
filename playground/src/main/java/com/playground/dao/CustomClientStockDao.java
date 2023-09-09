package com.playground.dao;

import java.util.List;

import com.playground.model.ClientStock;

public interface CustomClientStockDao {
	
	public List<ClientStock> getClientStocks(int clientId);
}

package com.playground.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.playground.model.Stock;

public interface StockDao extends JpaRepository<Stock, Integer>{

	Stock findByNameIgnoreCase(String name);
	
//	Stock findByStock(int id);
//	
//	void addOrUpdateStock(Stock stock);
//	
//	void deleteStock(int id);
	
	//@Query("SELECT s FROM Stock")
//	public List<Stock> getStocks();
	
}

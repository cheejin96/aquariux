package com.playground.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playground.dao.TransactionDao;
import com.playground.model.Transaction;

@Service
public class TransactionService {

	@Autowired
	TransactionDao transactionDao;
	
	public boolean saveTransaction(Transaction transaction) {
		System.out.println("saveTransaction | entry ...");
		Transaction newTransaction = transactionDao.saveAndFlush(transaction);
		
		if(newTransaction == null) {
			System.out.println("saveTransaction | fail to insert into database ...");
			return false;
		}
		System.out.println("saveTransaction | exit ...");
		return true;
	}
	
	public List<Transaction> getTransaction(Integer clientId, Integer stockId){
		System.out.println("getTransaction | entry ...");
		List<Transaction> trans = null;
		if(clientId != null && stockId != null) {
			System.out.println("getTransaction | proceed to get client transactions on particular stock ...");
			trans = getClientStockTransaction(clientId, stockId);
			
		}else if(clientId != null && stockId == null) {
			System.out.println("getTransaction | proceed to get client transactions ...");
			trans = getClientTransaction(clientId);
			
		}else {
			System.out.println("getTransaction | proceed to get stock transactions ...");
			trans = getStockTransaction(stockId);
		}
		System.out.println("getTransaction | exit ...");
		return trans;
	}
	
	public List<Transaction> getClientStockTransaction(int clientId, int stockId){
		System.out.println("getClientStockTransaction | entry ...");
		List<Transaction> clientStockTrans = null;
		
		clientStockTrans = transactionDao.findByClientIdAndStockId(clientId, stockId);
		if(clientStockTrans != null) {
			System.out.println("getClientStockTransaction | clientStockTrans not found for client id : " + clientId);
		}
		
		System.out.println("getClientStockTransaction | exit ...");
		return clientStockTrans;
	}
	
	public List<Transaction> getClientTransaction(int clientId){
		System.out.println("getClientTransaction | entry ...");
		List<Transaction> clientTrans = null;
		
		clientTrans = transactionDao.findByClientId(clientId);
		if(clientTrans != null) {
			System.out.println("getClientTransaction | clientTrans not found for client id : " + clientId);
		}
		
		System.out.println("getClientTransaction | exit ...");
		return clientTrans;
	}
	
	public List<Transaction> getStockTransaction(int stockId){
		System.out.println("getStockTransaction | entry ...");
		List<Transaction> stockTrans = null;
		
		stockTrans = transactionDao.findByStockId(stockId);
		if(stockTrans != null) {
			System.out.println("getStockTransaction | stockTrans not found for stock id : " + stockId);
		}
		
		System.out.println("getStockTransaction | exit ...");
		return stockTrans;
	}
}

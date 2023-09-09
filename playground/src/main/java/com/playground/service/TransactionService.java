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
	
	public List<Transaction> getTransaction(int clientId){
		System.out.println("getTransaction | entry ...");
		List<Transaction> clientTrans = null;
		
		clientTrans = transactionDao.findByClientId(clientId);
		if(clientTrans != null) {
			System.out.println("getTransaction | clientTrans not found for client id : " + clientId);
		}
		
		System.out.println("getTransaction | exit ...");
		return clientTrans;
	}
}

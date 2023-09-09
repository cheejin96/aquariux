package com.playground.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.playground.model.Client;
import com.playground.model.Transaction;

public interface TransactionDao extends JpaRepository<Transaction, Integer>{
	
	List<Transaction> findByClientId(int clientId);
	
}

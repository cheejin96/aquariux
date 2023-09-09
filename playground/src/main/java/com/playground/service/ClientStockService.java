package com.playground.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playground.dao.ClientStockDao;
import com.playground.model.ClientStock;

@Service
public class ClientStockService {
	
	@Autowired
	ClientStockDao clientStockDao;
	
	public List<ClientStock> getClientStocks(int clientId){
		System.out.println("getClientStocks | entry ...");
		List<ClientStock> clientStocks = clientStockDao.findByClientId(clientId);
		if(clientStocks == null) {
			System.out.println("getClientStocks | clientStocks not found for client id = " + clientId);
			return null;
		}
		System.out.println("getClientStocks | exit ...");
		return clientStocks;
	}
	
	public void saveClientStock(ClientStock cs) {
		System.out.println("saveClientStock | entry ...");
		ClientStock newCs = clientStockDao.saveAndFlush(cs);
		if(newCs == null) {
			System.out.println("saveClientStock | fail to save");
		}
		System.out.println("saveClientStock | exit ...");
	}
}

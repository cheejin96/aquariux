package com.playground.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playground.dao.ClientDao;
import com.playground.dao.StockDao;
import com.playground.model.Client;
import com.playground.model.ClientStock;
import com.playground.model.Stock;
import com.playground.model.Transaction;

import jakarta.websocket.server.PathParam;

@Service
public class StockService {
	
	@Autowired
	ClientService clientService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	ClientStockService clientStockService;
	
	@Autowired
	StockDao stockDao;
	
	public Stock getStockById(int stockId) {
		Stock stock = null;
		if(stockDao.findById(stockId).isPresent()) {
			stock = stockDao.findById(stockId).get();
		}
		return stock;
	}
	
	public List<Stock> getStockList(){
		return stockDao.findAll();
	}
	
	public void updateStock(List<Stock> stocks) {
		System.out.println("updateStock| entry ...");
		for(Stock stock: stocks) {
			Stock tempStock = stockDao.findByNameIgnoreCase(stock.getName());
			if(tempStock != null) {
				System.out.println("updateStock| stock found in database, proceed to update ...");
				tempStock.setAskPrice(stock.getAskPrice());
				tempStock.setAskQty(stock.getAskQty());
				tempStock.setBidPrice(stock.getBidPrice());
				tempStock.setBidQty(stock.getBidQty());
				stock = tempStock;
			}
			stockDao.saveAndFlush(stock);
		}
		System.out.println("updateStock| exit ...");
	}
	
	public boolean buyNSellStock(int clientId, int stockId, BigDecimal quantity, String action) {
		System.out.println("buyNSellStock| entry ...");
		//Check if valid client and stock 
		System.out.println("buyNSellStock| get client ...");
		Client client = clientService.getClientById(clientId);
		System.out.println("buyNSellStock| client id : " + client.getId());
		Stock stock = getStockById(stockId);
		System.out.println("buyNSellStock| stock name: " + stock.getName());
		List<ClientStock> clientStocks = null;
		ClientStock clientStock = null;
		if(client == null || stock == null) {
			System.out.println("buyNSellStock| client nor stock not found");
			return false;
		}
		
		if(action.equalsIgnoreCase("buy")) {
			System.out.println("buyNSellStock| perform buy action ...");
			//Check client if have enough money to buy
			if(!validBuyer(client, stock, quantity)) {
				return false;
			}
		}else if(action.equalsIgnoreCase("sell")) {
			System.out.println("buyNSellStock| perform sell action ...");
			//Check client if have client have the stock to sell
			clientStocks = clientStockService.getClientStocks(clientId);
			clientStock = clientStocks.stream().filter(s -> s.getStock().getId() == stock.getId()).findFirst().orElse(null);
			if(!validSeller(clientStock, stock, quantity)) {
				return false;
			}
			
		}
		
		//Proceed to paid
		return payment(clientStock, client,stock,quantity,action);
	}
	
	public boolean validBuyer(Client client, Stock stock, BigDecimal quantity) {
		System.out.println("validBuyer| entry ...");
		//Check if stock have enough value to sell
		if(stock.getBidQty().compareTo(quantity) == -1) {
			System.out.println("validBuyer| stock not enought quantity to sell ...");
			System.out.println("validBuyer| stock quantity = " + stock.getBidQty());
			System.out.println("validBuyer| client buy quantity = " + quantity);
			return false;
		}
		
		//Check if client have enough amount to buy
		BigDecimal payAmount = stock.getBidPrice().multiply(quantity);
		if(client.getWallet().compareTo(payAmount) == -1) {
			System.out.println("validBuyer| client wallet amount not enought ...");
			System.out.println("validBuyer| client wallet amount = " + client.getWallet());
			System.out.println("validBuyer| payAmount = " + payAmount);
			return false;
		}
		
		System.out.println("validBuyer| exit ...");
		return true;
	}
	
	public boolean validSeller(ClientStock clientStock, Stock stock, BigDecimal quantity) {
		System.out.println("validSeller| entry ...");
		//Check if client's stock have enough amount to buy
		if(clientStock == null) {
			System.out.println("validSeller| Client dont have Stock to sell ...");
			return false;
		}
		
		if(clientStock.getQuantity().compareTo(quantity) == -1) {
			System.out.println("validSeller| client stocks not enough quantity to sell ...");
			return false;
		}
		System.out.println("validSeller| exit ...");
		return true;
	}
	
	public boolean payment(ClientStock clientStock, Client client, Stock stock, BigDecimal quantity, String action) {
		System.out.println("payment| entry ...");
		try {
			BigDecimal payAmount = stock.getBidPrice().multiply(quantity);
			BigDecimal clientWallet = client.getWallet();
			
			//Create new transaction
			System.out.println("payment| initilizing transaction ...");
			Transaction transaction = new Transaction();
			transaction.setAction(action);
			transaction.setClient(client);
			transaction.setStock(stock);
			transaction.setValue(stock.getBidPrice());
			transaction.setQuantity(quantity);
			transaction.setTotalAmount(payAmount);
			
			//Update client wallet
			if(action.equalsIgnoreCase("buy")) {
				System.out.println("payment| buy | entry ...");
				if(clientStock == null) {
					clientStock = new ClientStock();
					clientStock.setClient(client);
					clientStock.setStock(stock);
					clientStock.setQuantity(quantity);
				}else {
					clientStock.setQuantity(clientStock.getQuantity().add(quantity));
				}
				
				clientWallet = clientWallet.subtract(payAmount);
				
			}else if(action.equalsIgnoreCase("sell")) {
				System.out.println("payment| sell | entry ...");
				//Update client's stock list
				clientStock.setQuantity(clientStock.getQuantity().subtract(quantity));
				
				clientWallet = clientWallet.add(payAmount);
			}
			client.setWallet(clientWallet);
			
			System.out.println("payment| saving into database ...");
			
			clientService.saveClient(client);
			clientStockService.saveClientStock(clientStock);
			transactionService.saveTransaction(transaction);
			
			System.out.println("payment| exit ...");
			return true;
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

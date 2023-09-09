package com.playground.controller;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.http.HttpRequest;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.playground.model.Client;
import com.playground.model.ClientStock;
import com.playground.model.Stock;
import com.playground.model.Transaction;
import com.playground.service.ClientService;
import com.playground.service.ClientStockService;
import com.playground.service.StockService;
import com.playground.service.TransactionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;

@RestController
public class MainController {

	@Autowired
	ClientService clientService;
	
	@Autowired
	StockService stockService;
	
	@Autowired
	ClientStockService clientStockService;
	
	@Autowired
	TransactionService transService;
	
	/**
	 * This api is use to trade the stock 
	 * 
	 * @param request
	 * @param clientId - Target Client's ID 
	 * @param stockId - Target Stock's ID 
	 * @param quantity - Target Stock's quantity
	 * @param action - BUY / SELL
	 * @return Transaction status message
	 */
	@RequestMapping(value = "/trade", method = RequestMethod.POST)
	@ResponseBody
	public String trade(HttpServletRequest request, 
			@PathParam(value="clientId") int clientId, 
			@PathParam(value="stockId") int stockId,
			@PathParam(value="quantity") BigDecimal quantity,
			@PathParam(value="action") String action
			) {
		
		try {
			System.out.println("trade| entry ...");
			if(action.isBlank() || clientId <= 0 || stockId <= 0) {
				System.out.println("trade| invalid parameters...");
				return "Invalid request";
			}
			
			if(action.equalsIgnoreCase("buy") || action.equalsIgnoreCase("sell")) {
				
				//Proceed to buy or sell 
				System.out.println("trade| proceed to trade ...");
				boolean successFlag = stockService.buyNSellStock(clientId, stockId, quantity, action);
				
				System.out.println("trade| exit ...");
				if(successFlag) {
					return("Transaction successful");
				}
				return("Transaction fail");
			}else {
				System.out.println("trade| action not support");
				return "false";
			}
		}catch(Exception e) {
			
			return "false";
		}
		
	}
	
	/**
	 * This api is to get latest best aggregated stock price list
	 * @param request
	 * @return Stock list
	 */
	@RequestMapping(value = "/stocks", method = RequestMethod.GET)
	@ResponseBody
	public String stockList(HttpServletRequest request) {
		
		try {
			//Return stock list 
			List<Stock> stocks = stockService.getStockList();
			if(stocks == null) {
				return "No stock is found";
			}
			
			if(stocks.size() == 0) {
				return "No stock is found";
			}
			
			return toJSON(stocks);
		}catch(Exception e) {
			
			return "false";
		}
	}
	
	
	/**
	 * This api is to get client profile
	 * 
	 * @param request
	 * @param clientId - Target Client's ID
	 * @return client basic profile info
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	@ResponseBody
	public String profile(HttpServletRequest request, @PathParam(value="clientId") int clientId) {
		
		try {
			//Return client basic profile with wallet balance
			Client client =  clientService.getClientById(clientId);
			if(client != null) {
				return toJSON(client);
			}
			return null;
		}catch(Exception e) {
			
			return "false";
		}
	}
	
	/**
	 * This api is to get stock list that Client have bought
	 * 
	 * @param request
	 * @param clientId - Target Client's ID
	 * @return 
	 */
	@RequestMapping(value = "/clientStock", method = RequestMethod.POST)
	@ResponseBody
	public String clientStock(HttpServletRequest request, @PathParam(value="clientId") int clientId) {
		System.out.println("clientStock | entry ...");
		try {
			//Return stock list bought by client
			List<ClientStock> clientStocks = clientStockService.getClientStocks(clientId);
			if(clientStocks != null) {
				System.out.println("clientStock | client stocks found ...");
				return toJSON(clientStocks);
			}
			System.out.println("clientStock | exit ...");
			return "Failed to get client stocks";
		}catch(Exception e) {
			
			return "false";
		}
	}
	
	@RequestMapping(value = "/clientTrans", method = RequestMethod.POST)
	@ResponseBody
	public String getClientTrans(HttpServletRequest request, @PathParam(value="clientId") int clientId) {
		System.out.println("getClientTrans | entry ...");
		try {
			//Return stock list bought by client
			List<Transaction> clientTrans = transService.getTransaction(clientId);
			if(clientTrans != null) {
				System.out.println("getClientTrans | client transactions found ...");
				return toJSON(clientTrans);
			}
			System.out.println("getClientTrans | exit ...");
			return "Failed to get client stocks";
		}catch(Exception e) {
			
			return "false";
		}
	}
	private String toJSON(Object obj) {
		Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
		return gson.toJson(obj);
	}
}

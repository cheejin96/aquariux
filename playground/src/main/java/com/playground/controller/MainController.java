package com.playground.controller;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.http.HttpRequest;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

import io.micrometer.common.util.StringUtils;
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
			@RequestParam Integer clientId, 
			@RequestParam Integer stockId,
			@RequestParam BigDecimal quantity,
			@RequestParam String action
			) {
		
		try {
			System.out.println("trade| entry ...");
			if(action.isBlank() || clientId == null || stockId == null || BigDecimal.ZERO.equals(quantity)) {
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
				return("Transaction fail, action not support");
			}
		}catch(Exception e) {
			e.printStackTrace();
			return("Transaction fail");
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
	public String profile(HttpServletRequest request, @RequestParam Integer clientId) {
		
		System.out.println("profile| entry...");
		if(clientId == null) {
			System.out.println("profile| invalid parameters...");
			return "Invalid request";
		}
		
		try {
			//Return client basic profile with wallet balance
			System.out.println("profile| proceed to get client profile...");
			Client client =  clientService.getClientById(clientId);
			if(client != null) {
				System.out.println("profile| exit...");
				return toJSON(client);
			}
			return "Client not found";
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
	@RequestMapping(value = "/client/stock", method = RequestMethod.POST)
	@ResponseBody
	public String clientStock(HttpServletRequest request, @RequestParam int clientId) {
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
	
	/**
	 * This api is to get transactions based on the parameter giving
	 * - when clientId is null and stockId is not null - return stock transactions list
	 * - when clientId is not null and stockId is null - return client transactions list
	 * - when clientId is not null and stockId is not null - return client transactions list on that particular stock
	 * 
	 * @param request
	 * @param clientId - Target Client's ID
	 * @param stockId - Target Stock's ID
	 * @return
	 */
	@RequestMapping(value = "/transaction", method = RequestMethod.POST)
	@ResponseBody
	public String getTrans(HttpServletRequest request, @RequestParam(required = false) Integer clientId, @RequestParam(required = false) Integer stockId) {
		System.out.println("getTrans | entry ...");
		
		if(clientId == null && stockId == null) {
			return "Either clientId or stockId must have value.";
		}
		try {
			//Return stock list bought by client
			List<Transaction> trans = transService.getTransaction(clientId, stockId);
			if(trans != null) {
				System.out.println("getTrans | transactions found ...");
				return toJSON(trans);
			}
			System.out.println("getTrans | exit ...");
			return "Transactions are empty";
		}catch(Exception e) {
			
			return "false";
		}
	}
	
	/**
	 * This api is to add new client
	 * 
	 * @param request
	 * @param name - Client's name
	 * @return Client ID
	 */
	@RequestMapping(value = "/client/add", method = RequestMethod.POST)
	@ResponseBody
	public String newClient(HttpServletRequest request, @RequestParam String name) {
		
		System.out.println("newClient| entry...");
		if(StringUtils.isBlank(name)) {
			System.out.println("newClient| invalid parameters...");
			return "Invalid request";
		}
		
		try {
			//Return client basic profile with wallet balance
			System.out.println("newClient| proceed to add client profile...");
			Client client = clientService.saveClient(name);
			System.out.println("newClient| exit...");
			return "Add client successful, Client ID = " +client.getId();
		}catch(Exception e) {
			
			return("Add client fail");
		}
	}
	
	private String toJSON(Object obj) {
		Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
		return gson.toJson(obj);
	}
}

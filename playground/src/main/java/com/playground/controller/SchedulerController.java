package com.playground.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.playground.model.Stock;
import com.playground.model.json.BinanceStock;
import com.playground.model.json.HoubiStock;
import com.playground.service.StockService;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;

@EnableScheduling
@RestController
public class SchedulerController {

	private final String binanceURL = "https://api.binance.com/api/v3/ticker/bookTicker";

	private final String houbiURL = "https://api.huobi.pro/market/tickers";
	
	@Autowired
	private StockService stockSerivce;

	/**
	 * Create a 10 seconds interval scheduler to retrieve the pricing from the
	 * source above and store the best pricing into the database. Hints: Bid Price
	 * use for SELL order, Ask Price use for BUY order
	 */
	// Run every 10 seconds
	@Scheduled(fixedRate = 10000)
	public void schedulerCall() {
		System.out.println("schedulerCall initiate ...");
		try {

			System.out.println("scheduleCall| proceed to get latest stocks ....");

			List<BinanceStock> fromBinance = binanceAPI();
			
			List<HoubiStock> fromHoubi = houbiAPI();
			
			
			List<Stock> stocks = getBestPricingStock(fromBinance, fromHoubi);
			
			if(stocks.size() > 0) {
				//Proceed to store into DB
				System.out.println("scheduleCall| proceed to update stocks ....");
				stockSerivce.updateStock(stocks);
			}
			
		} catch (Exception e) {
			System.out.println("scheduleCall| FAILED");
			System.out.println("scheduleCall| Exception = " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public List<Stock> getBestPricingStock(List<BinanceStock> fromBinance, List<HoubiStock> fromHoubi) {
		
		List<Stock> stocks = new ArrayList<>();
		
		for(BinanceStock bs: fromBinance) {
			
			//New instance
			Stock stock = new Stock();
			stock.setName(bs.getSymbol());
			
			//binance stock attribute
			BigDecimal binanceSellPrice = new BigDecimal(bs.getBidPrice());//sell
			BigDecimal binanceSellQty = new BigDecimal(bs.getBidQty());//sell qty
			BigDecimal binanceBuyPrice = new BigDecimal(bs.getAskPrice());//buy
			BigDecimal binanceBuyQty = new BigDecimal(bs.getAskQty());//buy qty

			//Get houbi stock to compare
			HoubiStock hs = fromHoubi.stream().filter( s -> s.getSymbol().equalsIgnoreCase(bs.getSymbol())).findFirst().orElseGet(null);
			
			if(hs == null) {
				//Stock not found from Houbi
				//Straight get binance stock
				//Sell
				stock.setBidPrice(binanceSellPrice);
				stock.setBidQty(binanceSellQty);
				//Buy
				stock.setAskPrice(binanceBuyPrice);
				stock.setAskQty(binanceBuyQty);
				
			}else {
				
				//Stock found from Houbi
				//Need to check which are best pricing
				
				//Compare By Profit
				//Binance
				BigDecimal binanceSellProfit = binanceSellPrice.multiply(binanceSellQty);
				BigDecimal binanceBuyProfit = binanceBuyPrice.multiply(binanceBuyQty);
				BigDecimal binanceProfit = binanceBuyProfit.subtract(binanceSellProfit);
				
				//Houbi
				BigDecimal houbiSellProfit = hs.getBid().multiply(hs.getBidSize());
				BigDecimal houbiBuyProfit = hs.getAsk().multiply(hs.getAskSize());
				BigDecimal houbiProfit = houbiSellProfit.subtract(houbiBuyProfit);
			
				if(binanceProfit.compareTo(houbiProfit) == -1) { // houbiProfit larger
					
					//Sell
					stock.setBidPrice(hs.getBid());
					stock.setBidQty(hs.getBidSize());
					//Buy
					stock.setAskPrice(hs.getAsk());
					stock.setAskQty(hs.getAskSize());
					
				}else {//binanceProfit larger
					
					//Sell
					stock.setBidPrice(binanceSellPrice);
					stock.setBidQty(binanceSellQty);
					//Buy
					stock.setAskPrice(binanceBuyPrice);
					stock.setAskQty(binanceBuyQty);
				}
			}
			
			stocks.add(stock);
		}
		
		return stocks;
	}
	
	public List<BinanceStock> binanceAPI() throws Exception {
		// Call binance url to get best pricing
		String response = callURL(binanceURL, null);
		System.out.println("schedulerCall| binanceAPI | response = " + response);

		// convert resposne to json
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();

		Gson gson = builder.create();
		Type listType = new TypeToken<List<BinanceStock>>(){}.getType();
		List<BinanceStock> binanceStocks = gson.fromJson(response, listType);
		List<BinanceStock> onlyETHnBTC = new ArrayList<BinanceStock>();
		System.out.println("schedulerCall| binanceAPI | binanceStocks size = " + binanceStocks.size());
		//Filter to get ETH and BTC
		for(BinanceStock bs : binanceStocks) {
			if(bs.getSymbol().equalsIgnoreCase("ETHBTC") || bs.getSymbol().equalsIgnoreCase("BTCUSDT")) {
				onlyETHnBTC.add(bs);
			}
		}
		
		System.out.println("schedulerCall| binanceAPI | onlyETHnBTC size = " + onlyETHnBTC.size());
		
		return onlyETHnBTC;
	}
	
	public List<HoubiStock> houbiAPI() throws Exception {
		// Call binance url to get best pricing
		String response = callURL(houbiURL, null);
		System.out.println("schedulerCall| houbiAPI | response = " + response);

		// convert resposne to json
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();

		Gson gson = builder.create();
		Type listType = new TypeToken<List<HoubiStock>>(){}.getType();
		JsonObject convertedObject = new Gson().fromJson(response, JsonObject.class);
		JsonArray jsonArray = convertedObject.getAsJsonArray("data");
		List<HoubiStock> houbiStocks = gson.fromJson(jsonArray, listType);
		List<HoubiStock> onlyETHnBTC = new ArrayList<HoubiStock>();
		
		System.out.println("schedulerCall| houbiAPI | stock price list size = " + houbiStocks.size());
		//Filter to get ETH and BTC
		for(HoubiStock hs : houbiStocks) {
			if(hs.getSymbol().equalsIgnoreCase("ETHBTC") || hs.getSymbol().equalsIgnoreCase("BTCUSDT")) {
				onlyETHnBTC.add(hs);
			}
		}
		
		System.out.println("schedulerCall| houbiAPI | onlyETHnBTC size = " + onlyETHnBTC.size());
		
		return onlyETHnBTC;
	}

	public String callURL(String targetUrl, String method) throws Exception {
		System.out.println("callURL| entry");
		System.out.println("callURL| targetUrl = " + targetUrl);
		System.out.println("callURL| method = " + method);
		StringBuilder result = new StringBuilder();
		URL url = new URL(targetUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (StringUtils.isNotBlank(method)) {
			conn.setRequestMethod(method);
		}
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();

		System.out.println("callURL| success");
		return result.toString();
	}
}

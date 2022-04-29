package stockanalyzer.ctrl;

import stockanalyzer.ui.UserInterface;
import yahooApi.YahooFinance;
import yahooApi.beans.Quote;
import yahooApi.beans.QuoteResponse;
import yahooApi.beans.Result;
import yahooApi.beans.YahooResponse;
import yahooApi.exceptions.YahooException;
import yahoofinance.Stock;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {


		
	public void process(String ticker) {
		System.out.println("Start process");

		//TODO implement Error handling 

		//TODO implement methods for
		//1) Daten laden

		QuoteResponse quote = (QuoteResponse) getData(ticker);


		//2) Daten Analyse

		try {
			System.out.println("Max: " + quote.getResult().stream().mapToDouble(Result::getAsk).max().orElseThrow(() -> new YahooException("This shouldn't been happening.")));
			System.out.println("Avg: " + quote.getResult().stream().mapToDouble(Result::getAsk).average().orElseThrow(() -> new YahooException("This shouldn't been happening.")));
			System.out.println("Count: " + quote.getResult().stream().mapToDouble(Result::getAsk).count());
		}catch (YahooException e){
			UserInterface.print(e.getMessage());
		}


	}
	

	public Object getData(String searchString) {
		YahooFinance yahooFinance = new YahooFinance();

		List<String> tickers = Arrays.asList(searchString.split(","));

		YahooResponse response = yahooFinance.getCurrentData(tickers);
		QuoteResponse quotes = response.getQuoteResponse();

		return quotes;
	}


	public void closeConnection() {
		
	}
}

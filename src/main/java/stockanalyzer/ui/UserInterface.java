package stockanalyzer.ui;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import downloader.Downloader;
import downloader.ParallelDownloader;
import downloader.SequentialDownloader;
import stockanalyzer.ctrl.Controller;
import yahooApi.exceptions.YahooException;

public class UserInterface 
{

	private Controller ctrl = new Controller();

	public void getDataFromCtrl1(){
		ctrl.process("AAPL,AMZN,INTC");
	}

	public void getDataFromCtrl2(){
		ctrl.process("BABA");
	}

	public void getDataFromCtrl3(){
		ctrl.process("ABBV");
	}
	public void getDataFromCtrl4(){

	}
	
	public void getDataForCustomInput() {

		List<String> tickers = Arrays.asList("AMZN", "BABA", "ABBV", "ZNGA", "AMD", "AAPL", "NIO", "INTC");
		Downloader parallelDownloader = new ParallelDownloader();
		Downloader sequentialDownloader = new SequentialDownloader();
		long startTime, endTime;

		try {
			startTime = System.currentTimeMillis();
			ctrl.downloadTickers(tickers, parallelDownloader);
			endTime = System.currentTimeMillis();
			System.out.println("Time for parallel downloading.");
			System.out.println(endTime-startTime);


			startTime = System.currentTimeMillis();
			ctrl.downloadTickers(tickers, sequentialDownloader);
			endTime = System.currentTimeMillis();
			System.out.println("Time for sequential downloading.");
			System.out.println(endTime-startTime);


		}catch (YahooException e){
			UserInterface.print(e.toString());
		}

		String tickersAsString = String.join(",", tickers);
		ctrl.process(tickersAsString);
	}


	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interfacx");
		menu.setTitel("Wählen Sie aus:");
		menu.insert("a", "AMZN", this::getDataFromCtrl1);
		menu.insert("b", "BABA", this::getDataFromCtrl2);
		menu.insert("c", "ABBV", this::getDataFromCtrl3);
		menu.insert("d", "Downloader",this::getDataForCustomInput);
		menu.insert("z", "Choice User Imput:",this::getDataFromCtrl4);
		menu.insert("q", "Quit", null);
		Runnable choice;
		while ((choice = menu.exec()) != null) {
			 choice.run();
		}
		ctrl.closeConnection();
		System.out.println("Program finished");
	}


	protected String readLine() 
	{
		String value = "\0";
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = inReader.readLine();
		} catch (IOException e) {
		}
		return value.trim();
	}

	protected Double readDouble(int lowerlimit, int upperlimit) 
	{
		Double number = null;
		while(number == null) {
			String str = this.readLine();
			try {
				number = Double.parseDouble(str);
			}catch(NumberFormatException e) {
				number=null;
				System.out.println("Please enter a valid number:");
				continue;
			}
			if(number<lowerlimit) {
				System.out.println("Please enter a higher number:");
				number=null;
			}else if(number>upperlimit) {
				System.out.println("Please enter a lower number:");
				number=null;
			}
		}
		return number;
	}

	public static void print(String msg){
		System.out.println(msg);
	}
}

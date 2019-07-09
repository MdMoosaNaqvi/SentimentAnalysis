package com.uofm.corenlp;

import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;

public class TwitterSenitmentAnalysis {

	static Map<String, Map<Date, StockSentiment>> map = new HashMap<String, Map<Date, StockSentiment>>();
	SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();
	
	public void readCSVForStock(String file) {
		 try { 
		    	
		        // Create an object of filereader 
		        // class with CSV file as a parameter. 
		        FileReader filereader = new FileReader(file); 
		  
		        // create csvReader object passing 
		        // file reader as a parameter 
		        CSVReader csvReader = new CSVReader(filereader); 
		        String[] nextRecord; 
		  
		        // we are going to read data line by line 
		        while ((nextRecord = csvReader.readNext()) != null) {
		        	if(!map.containsKey(nextRecord[0])) {
		        		map.put(nextRecord[0], null);
		        	}
		        	
		        }
		        csvReader.close();
		    } 
		    catch (Exception e) { 
		        e.printStackTrace(); 
		    } 
		} 
	
	public void readCSVForTweets(String file) {
		 try { 
		    	
		        // Create an object of filereader 
		        // class with CSV file as a parameter. 
		        FileReader filereader = new FileReader(file); 
		  
		        // create csvReader object passing 
		        // file reader as a parameter 
		        CSVReader csvReader = new CSVReader(filereader); 
		        String[] nextRecord; 
		  
		        // we are going to read data line by line 
		        while ((nextRecord = csvReader.readNext()) != null) {
		        	String symbols = nextRecord[4];
		        	String[] symbolList = symbols.split("-");
		        	for (int i=0; i<symbolList.length; i++) {
		        		Map<Date,StockSentiment> valuemap = map.get(symbolList[i]);
		        		Date messageDate = convertStringToDate(nextRecord[2]);
		        		String message = nextRecord[1]; 
		        		int messageSentimentScore = sentimentAnalysis.performSentimentAnalysis(message);
		        		if (valuemap != null) {
		        			//TODO: Check if date exist, if yes update record else create new record in value map and update it.
		        			if (valuemap.containsKey(messageDate)) {
		        				StockSentiment stockSentiment = valuemap.get(messageDate);
		        				List<String> tempList = stockSentiment.messageList;
			        			tempList.add(message);
			        			stockSentiment.messageList = tempList;
			        			stockSentiment.sentimentScore = stockSentiment.sentimentScore + messageSentimentScore;
			        			stockSentiment.totalMessages = stockSentiment.totalMessages + 1;
			        			Map<Date, StockSentiment> temp = new HashMap<Date, StockSentiment>();
			        			temp.put(messageDate, stockSentiment);
			        			map.put(symbolList[i], temp);
		        			} else {
		        				StockSentiment stockSentiment = new StockSentiment();
			        			List<String> tempList = new ArrayList<String>();
			        			tempList.add(message);
			        			stockSentiment.messageList = tempList;
			        			stockSentiment.sentimentScore = messageSentimentScore;
			        			stockSentiment.totalMessages = 1;
			        			Map<Date, StockSentiment> temp = new HashMap<Date, StockSentiment>();
			        			temp.put(messageDate, stockSentiment);
			        			map.put(symbolList[i], temp);
		        			}
		        		} else {
		        			//TODO: Add <Date, StockSentiment> map record
		        			StockSentiment stockSentiment = new StockSentiment();
		        			List<String> tempList = new ArrayList<String>();
		        			tempList.add(message);
		        			stockSentiment.messageList = tempList;
		        			stockSentiment.sentimentScore = messageSentimentScore;
		        			stockSentiment.totalMessages = 1;
		        			Map<Date, StockSentiment> temp = new HashMap<Date, StockSentiment>();
		        			temp.put(messageDate, stockSentiment);
		        			map.put(symbolList[i], temp);
		        		}
		        	}
		        }
		        csvReader.close();
		    } 
		    catch (Exception e) { 
		        e.printStackTrace(); 
		    } 
		} 
	
	Date convertStringToDate(String dateString) {
		dateString = dateString.substring(dateString.indexOf(" "));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" MMM dd hh:mm:ss +0000 yyyy");
		Date d1 = null;
		try {
			d1 = simpleDateFormat.parse(dateString);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			d1 = formatter.parse(formatter.format(d1));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d1;
	}
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		TwitterSenitmentAnalysis twitterSenitmentAnalysis = new TwitterSenitmentAnalysis();
		twitterSenitmentAnalysis.readCSVForStock("C:\\Users\\Maxim\\Documents\\FT\\stocks_short.csv");
		twitterSenitmentAnalysis.readCSVForTweets("C:\\\\Users\\\\Maxim\\\\Documents\\\\FT\\stockerbot_short.csv");
		/*String testDateString = "Wed Jul 18 21:33:26 +0000 2018";
		testDateString = testDateString.substring(testDateString.indexOf(" "));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" MMM dd hh:mm:ss +0000 yyyy");
		Date d1 = simpleDateFormat.parse(testDateString);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		d1 = formatter.parse(formatter.format(d1));
        System.out.println("Date: " + d1);*/
		
		for (Map.Entry<String, Map<Date,StockSentiment>> set : map.entrySet()) {
			if (set.getValue() != null) {
				Map<Date, StockSentiment> tempMap = set.getValue();
				for (Map.Entry<Date,StockSentiment> subSet : tempMap.entrySet()) {
					//System.out.println(subSet.getKey() + " " + subSet.getValue().totalMessages + " " + subSet.getValue().messageList.get(0));
					System.out.println("________________________________________________________________________________");
					System.out.println(set.getKey());
					System.out.println("Date " + subSet.getKey());
					System.out.println("Total Messages : " + subSet.getValue().totalMessages);
					System.out.println("Message List on " + subSet.getKey());
					List<String> messageList = subSet.getValue().messageList;
					for (String message : messageList) {
						System.out.println(message);
					}
					System.out.println(subSet.getValue().sentimentScore);
				}
			}
			
		}
	}

}

package com.uofm.corenlp;

import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.opencsv.CSVReader;

public class Test {
	
	static Calendar c1 = Calendar.getInstance();
	static Calendar c2 = Calendar.getInstance();
	SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();
	Map<String, String> map = new HashMap<String, String>();
	static Mongo mongo = new Mongo("localhost", 27017);
	static DB db = mongo.getDB("config");
	static DBCollection collection = db.getCollection("social_media");
	static DBCollection nmcollection = db.getCollection("news_media");
	static Map<String, String> stockNameQuoteMap = new HashMap<String, String>();
	public void readCSVForTweets(String file) {
		 try { 
		    	
		        // Create an object of filereader 
		        // class with CSV file as a parameter. 
		        FileReader filereader = new FileReader(file); 
		  
		        // create csvReader object passing 
		        // file reader as a parameter 
		        CSVReader csvReader = new CSVReader(filereader); 
		        String[] nextRecord; 
		        int messageCount = 0;
		        // we are going to read data line by line 
		        while ((nextRecord = csvReader.readNext()) != null) {
		        	messageCount += 1;
		        	System.out.println("Total number of messages read "  + messageCount);
		        	String symbols = nextRecord[4];
		        	String[] symbolList = symbols.split("-");
		        	for (int i=0; i<symbolList.length; i++) {
		        		//Map<Date,StockSentiment> valuemap = map.get(symbolList[i]);
		        		Date messageDate = convertStringToDate(nextRecord[2]);
		        		String message = nextRecord[1]; 
		        		int messageSentimentScore = sentimentAnalysis.performSentimentAnalysis(message);
		        		//TODO: First find the record with same date and symbol if not insert the record
		        		// to update? Can it be done by ObjectId???
		        		
		        		BasicDBObject find = new BasicDBObject();
		        		find.append("stock_quote", symbolList[i]);
		        		find.append("date", messageDate);
		        		DBObject cursorDoc = collection.findOne(find);
		        		BasicDBObject record = new BasicDBObject();
		        		if(cursorDoc != null) {
		        			ObjectId id = (ObjectId) cursorDoc.get("_id");
		        			BasicDBObject rec = (BasicDBObject) cursorDoc;
		        			BasicDBObject query = new BasicDBObject();
		        			query.append("_id", id);
		        			
		        			int sentimentScore = (Integer) rec.get("sentiment_score");
		        			sentimentScore += messageSentimentScore;
		        			rec.append("sentiment_score", sentimentScore);
		        			
		        			List<String> messageList = (ArrayList<String>) rec.get("message_list");
		        			messageList.add(message);
		        			rec.append("message_list", messageList);
		        			
		        			int totalMessageCount = rec.getInt("total_message_count");
		        			totalMessageCount += 1;
		        			rec.append("total_message_count", totalMessageCount);
		        			
		        			collection.update(query, rec);
		        		} else {
		        			record.append("stock_quote", symbolList[i]);
			        		record.append("date", messageDate);
			        		record.append("sentiment_score", messageSentimentScore);
			        		record.append("total_message_count", 1);
			        		List<String> messageList = new ArrayList<String>();
			        		messageList.add(message);
			        		record.append("message_list", messageList);
			        		collection.insert(record);
		        		}
		        		

		        	}
		        }
		        csvReader.close();
		    } 
		    catch (Exception e) { 
		        e.printStackTrace(); 
		    } 
		} 
	
	public int readCSVForNews(String file, int messageCount) {
		 try { 
		    	
		        // Create an object of filereader 
		        // class with CSV file as a parameter. 
		        FileReader filereader = new FileReader(file); 
		  
		        // create csvReader object passing 
		        // file reader as a parameter 
		        CSVReader csvReader = new CSVReader(filereader); 
		        String[] nextRecord; 
		        //int messageCount = 0;
		        // we are going to read data line by line 
		        while ((nextRecord = csvReader.readNext()) != null) {
		        	messageCount += 1;
		        	System.out.println("Total number of messages read "  + messageCount);
		        	String newsHeadline = nextRecord[1];
		        	//String symbols = nextRecord[4];
		        	String[] keywordList = newsHeadline.split(" ");
		        	String stockQuote = "";
		        	boolean isNewsContainStockName = false;
		        	for(int i = 0; i<keywordList.length; i++) {
		        		String keyword = keywordList[i].toLowerCase();
		        		if(stockNameQuoteMap.containsKey(keyword)) {
		        			isNewsContainStockName = true;
		        			stockQuote = stockNameQuoteMap.get(keyword);
		        			break;
		        		}
		        	}
		        	if (isNewsContainStockName) {
		        		//Map<Date,StockSentiment> valuemap = map.get(symbolList[i]);
		        		Date messageDate = convertStringToDateReuters(nextRecord[0]);
		        		String message = nextRecord[1]; 
		        		int messageSentimentScore = sentimentAnalysis.performSentimentAnalysis(message);
		        		//TODO: First find the record with same date and symbol if not insert the record
		        		// to update? Can it be done by ObjectId??? Yeah bro!!!!!!!
		        		
		        		BasicDBObject find = new BasicDBObject();
		        		find.append("stock_quote", stockQuote);
		        		find.append("date", messageDate);
		        		DBObject cursorDoc = nmcollection.findOne(find);
		        		BasicDBObject record = new BasicDBObject();
		        		if(cursorDoc != null) {
		        			ObjectId id = (ObjectId) cursorDoc.get("_id");
		        			BasicDBObject rec = (BasicDBObject) cursorDoc;
		        			BasicDBObject query = new BasicDBObject();
		        			query.append("_id", id);
		        			
		        			int sentimentScore = (Integer) rec.get("sentiment_score");
		        			sentimentScore += messageSentimentScore;
		        			rec.append("sentiment_score", sentimentScore);
		        			
		        			List<String> messageList = (ArrayList<String>) rec.get("message_list");
		        			messageList.add(message);
		        			rec.append("message_list", messageList);
		        			
		        			int totalMessageCount = rec.getInt("total_message_count");
		        			totalMessageCount += 1;
		        			rec.append("total_message_count", totalMessageCount);
		        			
		        			nmcollection.update(query, rec);
		        		} else {
		        			record.append("stock_quote", stockQuote);
		        			record.append("date", messageDate);
		        			record.append("sentiment_score", messageSentimentScore);
		        			record.append("total_message_count", 1);
		        			List<String> messageList = new ArrayList<String>();
		        			messageList.add(message);
		        			record.append("message_list", messageList);
		        			nmcollection.insert(record);
		        		}
		        	}
		        		
		        		

		        	
		        }
		        csvReader.close();
		    } 
		    catch (Exception e) { 
		        e.printStackTrace(); 
		    } 
		 return messageCount;
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
	
	Date convertStringToDateReuters(String dateString) {
		dateString = dateString.substring(0,8);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
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
	public static void main(String[] args) {
		/*Date date1 = new GregorianCalendar(2008, Calendar.FEBRUARY, 28).getTime();
		Date date2 = new GregorianCalendar(2008, Calendar.FEBRUARY, 29).getTime();
		
        c1.setTime(date1);
        c1.add(Calendar.DATE, 1);
        c2.setTime(date2);
		Test test = new Test();
		System.out.println(test.isDateSame(c1, c2));
		Map<String, Map<Date, StockSentiment>> sentimentMap = new HashMap<String, Map<Date, StockSentiment>>();
		StockSentiment stockSentiment = new StockSentiment();
		stockSentiment.sentimentScore = 1;
		List<String> messageList = new ArrayList<String>();
		messageList.add("This is good message");
		messageList.add("This is bad message");
		stockSentiment.messageList = messageList;
		Map<Date, StockSentiment> sentiMap = new HashMap<Date, StockSentiment>();
		sentiMap.put(date1, stockSentiment);
		sentimentMap.put("APPL", sentiMap);
		GsonBuilder gsonMapBuilder = new GsonBuilder();
		 
		Gson gsonObject = gsonMapBuilder.create();
 
		
		BasicDBObject record = new BasicDBObject();
		record.append("stock", "Apple");
		record.append("date", date1);
		record.append("sentiment_score", 1);
		record.append("message_list", messageList);
		String JSONObject = gsonObject.toJson(sentimentMap);
		System.out.println(JSONObject);
		
		// convert JSON to DBObject directly
		DBObject dbObject = (DBObject) JSON
				.parse(JSONObject);

		//collection.insert(record);

		BasicDBObject find = new BasicDBObject();
		find.append("stock", "Apple");
		find.append("Date", date1);
		DBCursor cursorDoc = collection.find(find);
		while (cursorDoc.hasNext()) {
			BasicDBObject rec = (BasicDBObject) cursorDoc.next();
			ObjectId id = (ObjectId) rec.get("_id");
			BasicDBObject query = new BasicDBObject();
			query.append("_id", id);
			int sentimentScore = (Integer) rec.get("sentiment_score");
			sentimentScore += -2;
			rec.append("sentiment_score", sentimentScore);
			collection.update(query, rec);
			System.out.println(rec);
		}

		System.out.println("Done");
*/
		stockNameQuoteMap = new NewExcel().readMap();
		Test twitterSenitmentAnalysis = new Test();
		String date = "20070102 11:16 PM EST";
		//System.out.println(twitterSenitmentAnalysis.convertStringToDateReuters(date));
		//twitterSenitmentAnalysis.readCSVForStock("C:\\Users\\Maxim\\Documents\\FT\\stocks_short.csv");
		//twitterSenitmentAnalysis.readCSVForTweets("C:\\\\Users\\\\Maxim\\\\Documents\\\\FT\\stockerbot-export.csv");

		//TODO: Add logic to read all files from data folder and increment message count globally
		int messageCount =0;
		File folder = new File("C:\\Users\\Maxim\\eclipse-workspace\\UnPickle\\data");
		File[] listOfFiles = folder.listFiles();
		for(int i= 0; i<listOfFiles.length; i++) {
			//System.out.println(listOfFiles[i].getPath());
			messageCount = twitterSenitmentAnalysis.readCSVForNews(listOfFiles[i].getPath(), messageCount);
		}
	}
}

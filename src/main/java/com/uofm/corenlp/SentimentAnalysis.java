package com.uofm.corenlp;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import com.opencsv.CSVReader;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalysis {
	
	static String[] stockNames;
	static List<DailyStockSentiment> dailyStockSentiments = new ArrayList<DailyStockSentiment>();
	static Calendar c = Calendar.getInstance();
	public int performSentimentAnalysis(String news) {
		 Properties props = new Properties();
	        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
	        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	        int mainSentiment = 0;
	            int longest = 0;
	            Annotation annotation = pipeline.process(news);
	            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
	                Tree tree = sentence.get(SentimentAnnotatedTree.class);
	                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
	                String partText = sentence.toString();
	                if (partText.length() > longest) {
	                    mainSentiment = sentiment;
	                    longest = partText.length();
	                }
	 
	            }
	        mainSentiment = mainSentiment - 2;
	        // System.out.println(mainSentiment);
	        return mainSentiment;
	    }
	
	public void readDataLineByLine(String file) 
	{ 
	  
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
	            //for (String cell : nextRecord) { 
	                //System.out.println(nextRecord[1]);
	            	match(nextRecord[1]);
	            //} 
	            //System.out.println(); 
	        }
	        csvReader.close();
	    } 
	    catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	} 
	
	
	public void match(String newsHeadLine) {
		int sentiment = performSentimentAnalysis(newsHeadLine);
		String  stockName = "";
		for (String fullStockName : stockNames) {
			stockName = fullStockName.split(" ")[0];
			if (newsHeadLine.contains(stockName)) {
				break;
			}
			}
		if(!stockName.isEmpty()) {
			
			boolean isStockPresent = false;
			System.out.println(stockName + "<-----> " + newsHeadLine);
			//for (DailyStockSentiment dailyStockSentiment : dailyStockSentiments) {
			System.out.println("List size " + dailyStockSentiments.size());
			for (int i = 0; i < dailyStockSentiments.size(); i++) {
				if (dailyStockSentiments.get(i).stockName.equals(stockName)) {
					// update stockSentiment
					DailyStockSentiment dailyStockSentiment = dailyStockSentiments.get(i);
					if(isDateSame(dailyStockSentiment.date, c)) {
						dailyStockSentiment.numOfArticle = dailyStockSentiment.numOfArticle + 1;
						dailyStockSentiment.sentimentScore = dailyStockSentiment.sentimentScore + sentiment;
						dailyStockSentiments.add(i, dailyStockSentiment);
						isStockPresent = true;
						break;
					}
					
				}
			}
			if (!isStockPresent) {
				DailyStockSentiment dailyStockSentiment = new DailyStockSentiment();
				dailyStockSentiment.stockName = stockName;
				dailyStockSentiment.numOfArticle = 1;
				dailyStockSentiment.sentimentScore = sentiment;
				dailyStockSentiment.date = c;
				dailyStockSentiments.add(dailyStockSentiment);
			}
		}
		
	}
	
	private boolean isDateSame(Calendar c1, Calendar c2) {
	    return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && 
	            c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
	            c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
	}
	public static void main(String []args) {
		SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();
		int score = sentimentAnalysis.performSentimentAnalysis("Senate wants emergency alerts to go out through Netflix Spotify etc. https://t.co/23yy3whBlc by @grg");
		System.out.println(score);
		stockNames = new NewExcel().read();
		File folder = new File("C:\\Users\\Maxim\\eclipse-workspace\\UnPickle\\data");
		File[] listOfFiles = folder.listFiles();
		List<DailyStockSentiment> dailyStockSentimentList = new ArrayList<DailyStockSentiment>();
		Date date = new GregorianCalendar(2009, Calendar.JANUARY, 1).getTime();
		
        c.setTime(date);
		for (int i = 0; i < listOfFiles.length; i++) {
			DailyStockSentiment dailyStockSentiment = new DailyStockSentiment();
			dailyStockSentiment.date = c;
			dailyStockSentiment.sentimentScore = 0;
			c.add(Calendar.DATE, 1); 
			//dailyStockSentimentList.add(dailyStockSentiment);
		  if (listOfFiles[i].isFile()) {
		    System.out.println("File " + listOfFiles[i].getName());
		    sentimentAnalysis.readDataLineByLine(listOfFiles[i].getPath());
		    
		  } else if (listOfFiles[i].isDirectory()) {
		    System.out.println("Directory " + listOfFiles[i].getName());
		  }
		  c.add(Calendar.DATE, 1);
		}
		for (DailyStockSentiment dailyStockSentiment : dailyStockSentiments) {
			System.out.println(dailyStockSentiment.stockName + " " + dailyStockSentiment.numOfArticle + " " + dailyStockSentiment.sentimentScore);
		}
	}

}

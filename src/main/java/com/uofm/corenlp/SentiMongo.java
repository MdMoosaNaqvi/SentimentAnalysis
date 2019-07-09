package com.uofm.corenlp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;


public class SentiMongo {
	static Mongo mongo = new Mongo("localhost", 27017);
	static DB db = mongo.getDB("config");
	static DBCollection collection = db.getCollection("senti_words");
	public static void main(String[] args) {
		 try{
	            BufferedReader buf = new BufferedReader(new FileReader("C:\\Users\\Maxim\\eclipse-workspace\\SentiWords\\SentiWords.txt"));
	            ArrayList<String> words = new ArrayList<String>();
	            String lineJustFetched = null;
	            String[] lineArray;
	            String[] wordArray;
	            int counter = 0;
	            while(true){
	                lineJustFetched = buf.readLine();
	                if(lineJustFetched == null){  
	                    break; 
	                }else{
	                    lineArray = lineJustFetched.split("\t");
	                    if(lineArray[0].endsWith("#a")) {
	                    	counter++;
	                    	String word = lineArray[0].split("#")[0];
	                    	//System.out.println(word + " " + lineArray[1]);
	                    	BasicDBObject record = new BasicDBObject();
	                    	record.append("word", word);
	                    	record.append("score", lineArray[1]);
	                    	collection.insert(record);
	                    }
	                }	               
	            }

	            buf.close();

	            System.out.println(counter);
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	}

}
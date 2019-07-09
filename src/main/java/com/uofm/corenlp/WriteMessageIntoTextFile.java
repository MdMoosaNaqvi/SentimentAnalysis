package com.uofm.corenlp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class WriteMessageIntoTextFile {
	
	static Mongo mongo = new Mongo("localhost", 27017);
	static DB db = mongo.getDB("config");
	static DBCollection senti_collection = db.getCollection("senti_words");
	static DBCollection media_collection = db.getCollection("news_media");
	static DBCollection token_score_collection = db.getCollection("token_score");
	
	public static void main(String[] args) {
		
		BasicDBObject find = new BasicDBObject();
		find.append("stock_quote", "NDAQ");
		DBCursor cursor = token_score_collection.find(find);
		int totalMessageCount = 0;
		  BufferedWriter pbw = null, nbw = null;
	        FileWriter pfw = null, nfw = null;

	        try {

	            pfw = new FileWriter("PositiveNDAQ.txt");
	            pbw = new BufferedWriter(pfw);
	            
	           nfw = new FileWriter("NegativeNDAQ.txt");
	           nbw = new BufferedWriter(nfw);
	        } catch (IOException e) {
	            System.err.format("IOException: %s%n", e);
	        }
		while(cursor.hasNext()) {
			DBObject obj = cursor.next();
			Integer wordCount = (Integer) obj.get("count");
			String word = (String) obj.get("word");
			Double score = ((Number) obj.get("score")).doubleValue();
			for (int i = 0; i< wordCount; i++) {
				
				if (score >= 0) {
				try {
					pfw.write(word);
					pfw.write(" ");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}else {
					try {
						nfw.write(word);
						nfw.write(" ");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			/*ArrayList<String> messageList = (ArrayList<String>) obj.get("message_list");
			for(String message: messageList) {
				 try {
					bw.write(message);
					bw.write("\n");
					 //System.out.println(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
		}
		try {
			nbw.close();
			pbw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

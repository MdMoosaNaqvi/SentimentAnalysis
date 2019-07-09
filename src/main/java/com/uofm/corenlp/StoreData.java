package com.uofm.corenlp;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class StoreData {

	static Mongo mongo = new Mongo("localhost", 27017);
	static DB db = mongo.getDB("config");
	static DBCollection senti_collection = db.getCollection("senti_words");
	static DBCollection media_collection = db.getCollection("news_media");
	static DBCollection token_score_collection = db.getCollection("token_score");
	
	public static void main(String[] args) {
		
		BasicDBObject find = new BasicDBObject();
		find.append("stock_quote", "NDAQ");
		DBCursor cursor = media_collection.find(find);
		int totalMessageCount = 0;
		while(cursor.hasNext()) {
			DBObject obj = cursor.next();
			ArrayList<String> messageList = (ArrayList<String>) obj.get("message_list");
			for(String message: messageList) {
				
				String[] tokenWords = message.split(" ");
				for(int i = 0; i<tokenWords.length; i++) {
					BasicDBObject findSentiWord = new BasicDBObject();
					findSentiWord.append("word", tokenWords[i]);
					findSentiWord.append("stock_quote", "NDAQ");
	        		DBObject cursorDoc = token_score_collection.findOne(findSentiWord);
	        		BasicDBObject record = new BasicDBObject();
	        		if(cursorDoc != null) {
	        			ObjectId id = (ObjectId) cursorDoc.get("_id");
	        			BasicDBObject rec = (BasicDBObject) cursorDoc;
	        			BasicDBObject query = new BasicDBObject();
	        			query.append("_id", id);
	        			
	        			int count = (Integer) rec.get("count");
	        			count += 1;
	        			rec.append("count", count);
	        			
	        			
	        			
	        			token_score_collection.update(query, rec);
	        		} else {
	        			BasicDBObject findSentiWord1 = new BasicDBObject();
	        			String word = tokenWords[i].toLowerCase();
	        			findSentiWord1.append("word", word);
		        		DBObject cursorDoc1 = senti_collection.findOne(findSentiWord1);
		        		if(word.equalsIgnoreCase("microsoft") || word.equalsIgnoreCase("google") || word.equalsIgnoreCase("apple") || word.equalsIgnoreCase("funds")) {
		        			record.append("stock_quote", "NDAQ");
		        			record.append("score", 0);
		        			record.append("count", 1);
		        			record.append("word", word);
		        			token_score_collection.insert(record);
		        		}
		        		if(cursorDoc1 != null) {
		        			String scoreStr = (String) cursorDoc1.get("score");
		        			Double score = Double.valueOf(scoreStr);
		        			record.append("stock_quote", "NDAQ");
		        			record.append("score", score);
		        			record.append("count", 1);
		        			record.append("word", word);
		        			token_score_collection.insert(record);
		        		}
	        		}
				}
				System.out.println("Total message read for NDAQ" + totalMessageCount++);
			}
		}
	}

}

package com.uofm.corenlp;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

/**
 * Java MongoDB : Convert JSON data to DBObject
 * 
 */

public class App {
	public static void main(String[] args) {

		try {

			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("config");
			DBCollection collection = db.getCollection("social_media");

			// convert JSON to DBObject directly
			DBObject dbObject = (DBObject) JSON
					.parse("{'name':'mkyong', 'age':30}");

			collection.insert(dbObject);

			DBCursor cursorDoc = collection.find();
			while (cursorDoc.hasNext()) {
				System.out.println(cursorDoc.next());
			}

			System.out.println("Done");

		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
}

package mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mathias on 5/15/17.
 */
public class MongoImporter {

    public static void main(String[] args) {
        MongoDatabase connection = MongoDBConnector.getDBConnection();

        MongoCollection<Document> cities = connection.getCollection("cities");
        
        BufferedReader in = new BufferedReader(new FileReader("/home/mathias/Project/DataCleaner/Files/mongo_city_data.txt"));
        String str;

        List<String> list = new ArrayList<String>();
        while((str = in.readLine()) != null){
            list.add(str);
        }

        for (int i = 0; i < list.size(); i++) {
            Document location = Document.parse(list.get(i));
            cities.insertOne(location);
            System.out.println(list.get(i));
        }

        System.out.println("Finished bulk-insert");
    }
}

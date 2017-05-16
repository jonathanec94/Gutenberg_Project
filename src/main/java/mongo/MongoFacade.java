/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo;

import DbInterface.DbInterface;
import DtoEntity.DtoBookAuthor;
import DtoEntity.DtoBookCity;
import DtoEntity.DtoCity;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entity.Book;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author nikolai
 */
public class MongoFacade implements DbInterface {

    @Override
    public List<DtoCity> findCities(List<String> city) {
        System.out.println("findCities");
        List list = new ArrayList<>();
        MongoDatabase dbConnection = MongoDBConnector.getDBConnection();
        MongoCollection<Document> cityCollection = dbConnection.getCollection("cities");
        List<Document> cities = cityCollection.find().into(new ArrayList<Document>());

        for (Document cityObject : cities) {
            System.out.println(cityObject);
            if (cityObject.get("latitude").toString().isEmpty() == false && cityObject.get("longitude").toString().isEmpty() == false) {

                list.add(new DtoCity(cityObject.get("name").toString(), Double.parseDouble(cityObject.get("latitude").toString()), Double.parseDouble(cityObject.get("longitude").toString())));
            }
        }
        return list;

    }

    @Override
    public boolean insertBook(Book book) {
        System.out.println("insertBook");
        MongoDatabase db = MongoDBConnector.getDBConnection();
        MongoCollection<Document> books = db.getCollection("books");

        //create book
        books.insertOne(new Document().append("author", book.getAuthor()).append("title", book.getTitle()).append("cities", book.getCities()).append("tmpCities", book.getTmpCities()));
        return true;
    }

    @Override
    public List<DtoBookAuthor> getBooksByCity(final String city) {
        System.out.println("getBooksByCity");
        List list = new ArrayList<>();
        MongoDatabase dbConnection = MongoDBConnector.getDBConnection();
        FindIterable<Document> iterable = dbConnection.getCollection("books").find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                if (document.get("cities").toString().contains(city)) {
                    System.out.println(document.get("title").toString());
                }
            }
        });
        return list;
    }

    @Override
    public List<DtoCity> getCitiesByTitle(final String title) {
        System.out.println("getCitiesByTitle");
        List list = new ArrayList<>();
        MongoDatabase dbConnection = MongoDBConnector.getDBConnection();
        BasicDBObject query = new BasicDBObject("title", title);
        FindIterable<Document> iterable = dbConnection.getCollection("cities").find(query);
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                if (document.get("name").toString().toLowerCase().equals(title.toLowerCase())) {
                    System.out.println(document.get("name").toString());
                }
            }
        });
        return list;
    }

    @Override
    public List<DtoBookAuthor> getBooksByAuthor(final String authorString) {
        System.out.println("getBooksByAuthor");
        List list = new ArrayList<>();
        MongoDatabase dbConnection = MongoDBConnector.getDBConnection();
        FindIterable<Document> iterable = dbConnection.getCollection("books").find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                if (document.get("author").toString().toLowerCase().equals(authorString.toLowerCase())) {
                    System.out.println(document.get("name").toString());

                }
            }
        });
        return list;
    }

    @Override
    public List<DtoBookCity> getBooksByGeolocation(double latitude, double longitude) {
        System.out.println("getBooksByGeolocation");
        List list = new ArrayList<>();

        BasicDBObject criteria = new BasicDBObject("$near", new double[]{-80.23, 13.1112});
        criteria.put("$maxDistance", 1000);

        BasicDBObject query = new BasicDBObject("location", criteria);
        MongoDatabase dbConnection = MongoDBConnector.getDBConnection();
        FindIterable<Document> iterable = dbConnection.getCollection("cities").find(query);
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.get("name").toString());
            }
        });
        return list;
    }




}

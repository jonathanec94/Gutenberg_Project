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
import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entity.Book;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

/**
 *
 * @author nikolai
 */
public class MongoFacade implements DbInterface{

    @Override
    public List<DtoCity> findCities(List<String> city) {
        List  list = new ArrayList<DtoCity>();
        MongoDatabase dbConnection = MongoDBConnector.getDBConnection();
        MongoCollection<Document> cityCollection = dbConnection.getCollection("cities");
        List<Document> cities = (List<Document>) cityCollection.find().into(  new ArrayList<Document>());

        for (Document cityObject : cities) {
            System.out.println(cityObject);
            if(cityObject.get("latitude").toString().isEmpty() == false && cityObject.get("longitude").toString().isEmpty() == false) {

                list.add(new DtoCity(cityObject.get("name").toString(), Double.parseDouble(cityObject.get("latitude").toString()), Double.parseDouble(cityObject.get("longitude").toString())));
            }
        }
        return list;

    }

    @Override
    public boolean insertBook(Book book) {
        try{
            MongoDatabase dbConnection = MongoDBConnector.getDBConnection();
            DBCollection coll = dbConnection.getCollection("books");
            System.out.println("Collection selected successfully");

            BasicDBObject doc = new BasicDBObject("title", book.getTitle()).
                    append("author", book.getAuthor()).
                    append("cities", book.getCities()).
                    append("tmpCities", book.getTmpCities());

            coll.insert(doc);
            System.out.println("Document inserted successfully");
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    @Override
    public List<DtoBookAuthor> getBooksByCity(String city) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DtoCity> getCitiesByTitle(String title) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DtoBookAuthor> getBooksByAuthor(String author) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DtoBookCity> getBooksByGeolocation(double latitude, double longitude) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}

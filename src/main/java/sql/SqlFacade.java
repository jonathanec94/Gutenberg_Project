/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import DbInterface.DbInterface;
import DtoEntity.DtoBookAuthor;
import DtoEntity.DtoBookCity;
import DtoEntity.DtoCity;
import entity.Book;
import entity.City;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author nikolai
 */
public class SqlFacade implements DbInterface {
    Connection con;
     public SqlFacade()
    {
         SqlDBConnector.getDBConnection();
    }
    
       public SqlFacade(DataSource ds)
    {
         con = SqlDBConnector.setSource(ds);
    }
    
    @Override
    public List<DtoCity> findCities(List<String> city) {
        List<DtoCity> result = new ArrayList();
        for (String item : city) {
            result.add(new DtoCity(item, 2, 1));
        }
        return result;
    }

    @Override
    public boolean insertBook(Book book) {
        Statement stmt = null;

        try {
            SqlDBConnector.getDBConnection().setAutoCommit(false);
            stmt = SqlDBConnector.getDBConnection().createStatement();

            String sqlBook = "INSERT INTO BOOKS (title,author) "
                    + "VALUES (?, ?) RETURNING id;";
            PreparedStatement preparedStatement = null;
            preparedStatement = SqlDBConnector.getDBConnection().prepareStatement(sqlBook);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());

            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            int bookIdFromDb = rs.getInt(1);

            String sqlBooksCities = "INSERT INTO CITYINBOOK (cityname,bookid) VALUES ";
            List<String> cities = book.getTmpCities();
            int citiesSize = cities.size();
            if (citiesSize > 0) {
                for (int i = 0; i < citiesSize; i++) {
                    if (i == (citiesSize - 1)) {
                        sqlBooksCities += "('" + cities.get(i) + "', " + bookIdFromDb + ");";
                    } else {
                        sqlBooksCities += "('" + cities.get(i) + "', " + bookIdFromDb + "),";
                    }
                }
                // System.out.println(sqlBooksCities);
                stmt.executeUpdate(sqlBooksCities);
            }
            stmt.close();
            SqlDBConnector.getDBConnection().commit();

        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    @Override
    public List<DtoBookAuthor> getBooksByCity(String city) {
        city = city.toLowerCase();
        List<DtoBookAuthor> listOfBooks = new ArrayList<>();

        String sqlFindBooksByCity = "SELECT books.id, books.title, books.author FROM cities "
                + "INNER JOIN cityinbook ON cityinbook.cityname = cities.name "
                + "INNER JOIN books ON books.id = cityinbook.bookid "
                + "WHERE cityname = ?;";
        PreparedStatement statementGetBooks = null;
        try {
            statementGetBooks = SqlDBConnector.getDBConnection().prepareStatement(sqlFindBooksByCity);
            statementGetBooks.setString(1, city);
            ResultSet rsFindBooks = statementGetBooks.executeQuery();

            while (rsFindBooks.next()) {
                
                //System.out.println("id: "+ rsFindBooks.getInt("id"));
                listOfBooks.add(new DtoBookAuthor(rsFindBooks.getString("title"), rsFindBooks.getString("author")));
            }
            rsFindBooks.close();
            statementGetBooks.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return listOfBooks;
    }

    @Override
    public List<DtoCity> getCitiesByTitle(String title) {
        List<DtoCity> listOfCities = new ArrayList<>();
        String sqlGetCitiesByTtile = "SELECT cities.name, cities.latitude, cities.longitude FROM books "
                + "INNER JOIN cityinbook ON cityinbook.bookid = books.id "
                + "INNER JOIN cities ON cities.name = cityinbook.cityname "
                + "WHERE books.title = ?;";
        PreparedStatement statementGetCities = null;
        try {
            statementGetCities = SqlDBConnector.getDBConnection().prepareStatement(sqlGetCitiesByTtile);
            statementGetCities.setString(1, title);
            ResultSet rsCities = statementGetCities.executeQuery();
            while (rsCities.next()) {
                listOfCities.add(new DtoCity(rsCities.getString("name"), rsCities.getDouble("latitude"), rsCities.getDouble("longitude")));
            }
            rsCities.close();
            statementGetCities.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return listOfCities;
    }

    @Override
    public List<DtoBookAuthor> getBooksByAuthor(String author) {
        List<DtoBookAuthor> listOfBooks = new ArrayList();

        String sqlFindAllBooksOnAuthor = "SELECT id, title, author FROM books WHERE books.author = ?";
        PreparedStatement statementFindBooks = null;
        try {
            statementFindBooks = SqlDBConnector.getDBConnection().prepareStatement(sqlFindAllBooksOnAuthor);
            statementFindBooks.setString(1, author);
            ResultSet rsBooks = statementFindBooks.executeQuery();

            while (rsBooks.next()) {
                int id = rsBooks.getInt("id");
                String title = rsBooks.getString("title");
                String authorFromBook = rsBooks.getString("author");

                String sqlFindCities = "SELECT cities.name, cities.latitude, cities.longitude FROM cityinbook "
                        + "INNER JOIN cities ON cityinbook.cityname = cities.name "
                        + "WHERE cityinbook.bookid = ?;";
                PreparedStatement statementFindCities = null;
                statementFindCities = SqlDBConnector.getDBConnection().prepareStatement(sqlFindCities);
                statementFindCities.setInt(1, id);
                ResultSet rsCities = statementFindCities.executeQuery();

                DtoBookAuthor dtoBookAuthor = new DtoBookAuthor(title, authorFromBook);
                List<DtoCity> listOfCities = new ArrayList<>();
                while (rsCities.next()) {
                    listOfCities.add(new DtoCity(rsCities.getString("name"), rsCities.getDouble("latitude"), rsCities.getDouble("longitude")));
                }
                dtoBookAuthor.setCities(listOfCities);
                //add to the list that we return
                listOfBooks.add(dtoBookAuthor);

                rsCities.close();
                statementFindCities.close();
            }

            rsBooks.close();
            statementFindBooks.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        //first find all books from author

        //now run loop on all the books, and find all the cities in the book
        //make a DtoBookAuthor object with both the author and a list of cities
        return listOfBooks;
    }

    @Override
    public List<DtoBookAuthor> getBooksByGeolocation(double latitude, double longitude) {
        List<DtoBookAuthor> books = new ArrayList<>();
        int radiusInMeter = 10000;

        try {
            String sqlFindCitiesNearBy = "SELECT * FROM books "
                    + "INNER JOIN cityinbook ON cityinbook.bookid = books.id "
                    + "INNER JOIN cities ON cities.name = cityinbook.cityname "
                    + "WHERE ST_DWithin(cities.geom, ST_GeographyFromText(?), ? );";
            PreparedStatement statementFindCities = null;
            statementFindCities = SqlDBConnector.getDBConnection().prepareStatement(sqlFindCitiesNearBy);
            //not right to do, but how should i escape single quotes in a PreparedStatement???
            statementFindCities.setString(1, "SRID=4326;POINT(" + latitude + " " + longitude + ")");
            statementFindCities.setInt(2, radiusInMeter);

            ResultSet rsFindCities = statementFindCities.executeQuery();

            while (rsFindCities.next()) {
                books.add(new DtoBookAuthor(rsFindCities.getString("title"), rsFindCities.getString("author")));
            }
            rsFindCities.close();
            statementFindCities.close();
        } catch (SQLException ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            return null;
        }

        return books;
    }

    //not a part of the interface
    public boolean insertGeoOnCities() {
        Statement stmt = null;
        try {
            stmt = SqlDBConnector.getDBConnection().createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM cities WHERE geom IS NULL LIMIT 50000;");
            while (rs.next()) {
                int id = rs.getInt("id");
                Float latitude = rs.getFloat("latitude");
                Float longitude = rs.getFloat("longitude");
                PreparedStatement update = SqlDBConnector.getDBConnection().prepareStatement("UPDATE cities "
                        + "SET geom = ST_GeomFromText('POINT(" + latitude + " " + longitude + ")', 4326) "
                        + "WHERE id = " + id);
                update.executeUpdate();
                // System.out.println("latitude: "+latitude + " ::: longitude: "+longitude);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            return false;
        }
        return true;
    }

}

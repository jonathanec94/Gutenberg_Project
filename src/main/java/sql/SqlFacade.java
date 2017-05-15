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
import java.util.List;

/**
 *
 * @author nikolai
 */
public class SqlFacade implements DbInterface{
    
    Connection con = SqlDBConnector.getDBConnection();

    @Override
    public List<City> findCities(List<String> city) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean insertBook(Book book) {
         Statement stmt = null;

        try {
            con.setAutoCommit(false);
            stmt = con.createStatement();

            String sqlBook = "INSERT INTO BOOKS (title,author) "
                    + "VALUES (?, ?) RETURNING id;";
            PreparedStatement preparedStatement = null;
            preparedStatement = con.prepareStatement(sqlBook);
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
            con.commit();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        return false;
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

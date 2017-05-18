/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gutenberg_project;

import DbInterface.Facade;
import DtoEntity.DtoBookAuthor;
import DtoEntity.DtoCity;
import entity.Book;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import sql.SqlDBConnector;
import sql.SqlFacade;

/**
 *
 * @author nikolai
 */
public class Main {

//    SqlFacade sqlFacade = new SqlFacade();
//    Facade instance = new Facade(sqlFacade);
//    public static void main(String[] args) throws IOException {
//        Main main = new Main();
//        //main.testGetEnglishWords();
////        main.instance.insertBooksWithCitiesHelper(); 
//    }
    public static void main(String[] args) {
        //Main main = new Main();
        SqlFacade sqlFacade = new SqlFacade();
        //List<DtoCity> list = sqlFacade.getBooksByGeolocation(55.67594, 12.56553);
        //        List<DtoCity> list = sqlFacade.getCitiesByTitle("Moby Dick");
        //        System.out.println(list.get(1).getName());
        List<DtoBookAuthor> list = sqlFacade.getBooksByAuthor("Abraham Lincoln");
        System.out.println(list.get(1).getCities().size());
    }
}

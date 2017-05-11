/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gutenberg_project;

import DbInterface.Facade;
import entity.Book;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import sql.SqlDBConnector;

/**
 *
 * @author nikolai
 */
public class Main {

    Facade instance = new Facade(null);
//new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/10267.txt")))

    public void testGetEnglishWords() throws IOException{
       Book book = instance.findAllPossibleCitiesInBook(new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/10267.txt"))));
        System.out.println("size: "+book.getTmpCities().size()); 
//       for (String city : book.getTmpCities()) {
//            System.out.println(city);
//        }
    }
    
       
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        //main.testGetEnglishWords();
        
        main.instance.insertBooksWithCitiesHelper();
        
    }
//    public static void main(String[] args) {
//        if(SqlDBConnector.getDBConnection() != null)
//            System.out.println("not null");
//    }
}

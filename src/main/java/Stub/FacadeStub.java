/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Stub;

import DbInterface.DbInterface;
import DbInterface.Facade;
import entity.Book;
import entity.City;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonathan
 */
public class FacadeStub extends Facade {
    
    public FacadeStub(DbInterface db) {
        super(db);
    }
    
    
    public Book findAllPossibleCitiesInBook(BufferedReader in){
       Book bookStub = new Book();
       List<City> list = new ArrayList<>();
       list.add(new City (1,"city 1")); 
       list.add(new City (2, "city 2"));
       bookStub.setAuthor("mocked author");
       bookStub.setTitle("Mock title");
       bookStub.setCities(list);
       
       return bookStub;
    }
    
    public boolean insertBooksWithCitiesHelper(){
            boolean result = true;
        for (int i = 39999; i < 54661; i++) {
            String path = "/home/nikolai/Desktop/dbtextfiles/txt/" + i + ".txt";
            //stubbed methood call away
            //result += insertBooksWithCities(path);
        }
        return result;
    }
    
      public boolean insertBooksWithCities(String path) throws FileNotFoundException, IOException {
//        System.out.println("path: "+path);
        //find all book files here in folder
        Book book = null;
        if (new File(path).exists()) {

            book = findAllPossibleCitiesInBook(new BufferedReader(new FileReader(path)));

            if (book != null) {
                //stubbed methood call away
                //db.insertBook(book);
            }
        } else {
            //System.out.println("File does not exist: " + path);
            return false;
        }

        return true;
    }
      


    
}

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
import java.util.ArrayList;
import java.util.List;

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

    
}

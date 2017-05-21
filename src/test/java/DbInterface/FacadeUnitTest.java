/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbInterface;

import DbInterface.Facade;
import Stub.FacadeStub;
import entity.Book;
import entity.City;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.spy;
import org.mockito.Spy;

/**
 *
 * @author Jonathan
 */
public class FacadeUnitTest {
    
    public FacadeUnitTest() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void testFindAllCitiesUnit() throws IOException{
        Facade instance = new Facade(null);
        
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
      
        // if title or author length is lower than 500
        Mockito.when(bufferedReader.readLine()).thenReturn("Title: TestMock title", "Author: TestMock author", "*** START", "City wgwe penhagen","test. Hello there","Dubai","Zaranj", null); 
        
        //result
        Book book = instance.findAllPossibleCitiesInBook(bufferedReader);
        
        //assertions
        assertThat(book.getAuthor(), is("TestMock author"));
        assertThat(book.getTitle(), is("TestMock title"));
        assertThat(book.getTmpCities().size(), is(2));
        assertThat(book.getTmpCities().get(1), is("dubai"));
        assertThat(book.getTmpCities().get(0), is("zaranj"));
        
        ////////////////////////////////////////////////////////////////////////////////////
        
        // if title or author length is higher than 500
        String title =  "Title : https://www.google.dk/search?q=git+checkout+restore+files&oq=git+checkout+recover+fil&aqs=chrome.4.69i57jhttps://www.google.dk/search?q=git+checkout+restore+files&oq=git+checkout+recover+fil&aqs=chrome.4.69i57jhttps://www.google.dk/search?q=git+checkout+restore+files&oq=git+checkout+recover+fil&aqs=chrome.4.69i57jhttps://www.google.dk/search?q=git+checkout+restore+files&oq=git+checkout+recover+fil&aqs=chrome.4.69i57jhttps://www.google.dk/search?q=git+checkout+restore+files&oq=git+checkout+recover+fil&aqs=chrome.4.69i57j";
        String author = "Author : https://www.google.dk/search?q=git+checkout+restore+files&oq=git+checkout+recover+fil&aqs=chrome.4.69i57jhttps://www.google.dk/search?q=git+checkout+restore+files&oq=git+checkout+recover+fil&aqs=chrome.4.69i57jhttps://www.google.dk/search?q=git+checkout+restore+files&oq=git+checkout+recover+fil&aqs=chrome.4.69i57jhttps://www.google.dk/search?q=git+checkout+restore+files&oq=git+checkout+recover+fil&aqs=chrome.4.69i57jhttps://www.google.dk/search?q=git+checkout+restore+files&oq=git+checkout+recover+fil&aqs=chrome.4.69i57j";
        Mockito.when(bufferedReader.readLine()).thenReturn(title, author, "*** START", "City wgwe penhagen","test. Hello there","Dubai","Zaranj", null); 
        //result
        Book book2 = instance.findAllPossibleCitiesInBook(bufferedReader);
        
        //assertions
        assertThat(book2.getAuthor(), is(""));
        assertThat(book2.getTitle(), is(""));
        assertThat(book2.getTmpCities().size(), is(2));
        assertThat(book2.getTmpCities().get(1), is("dubai"));
        assertThat(book2.getTmpCities().get(0), is("zaranj"));        
    }
    
    /* 
    Test
        - First we test when the file dosent exists.
        - Second we test when the files exists.
    
        To make this unit test we have to make a stub, so we can control the behavior
    */
    @Test
    public void testInsertBooksWithCities() throws IOException{
       FacadeStub instance = new FacadeStub(null);
       Book book = instance.findAllPossibleCitiesInBook(null);
        
      boolean resFalse = instance.insertBooksWithCities("ThisFileDosentExists.txt");
      assertThat(resFalse, is(false));

      boolean resTrue = instance.insertBooksWithCities(this.getClass().getResource("/10267.txt").getFile());
      assertThat(resTrue, is(true));
    }
    
    @Test
    public void testInsertBooksWithCitiesHelper() throws IOException{
        FacadeStub instance = new FacadeStub(null);
        assertThat(instance.insertBooksWithCitiesHelper(), is(true));
    }
    
    @Test
    public void testGetEnglishWords() throws IOException{
      FacadeStub instance = new FacadeStub(null);
      BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
      
      //setting up tests for the try 
      Mockito.when(bufferedReader.readLine()).thenReturn("City","City_2", null); 
      HashSet hs = instance.getEnglishWords(bufferedReader);
      assertThat(hs.size(),is(2));
      assertThat(hs.contains("city_2"), is(true));
      assertThat(hs.contains("city"),is(true));
      assertThat(hs.contains("City 3"),is(false));
      
      //Test for the catch
      BufferedReader bufferedReader2 = Mockito.mock(BufferedReader.class);
      Mockito.doThrow(new IOException()).when(bufferedReader2).readLine();
      HashSet hs2 = instance.getEnglishWords(bufferedReader2);

      assertThat(hs2.contains("ReadingfileFailed"),is(true));
    }
    
}

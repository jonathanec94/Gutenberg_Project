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
    
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        Mockito.when(bufferedReader.readLine()).thenReturn("Title: TestMock title", "Author: TestMock author", "*** START", "City wgwe penhagen","test. Hello there","Dubai","Zaranj", null);
        Facade instance = new Facade(null);
        Book book = instance.findAllPossibleCitiesInBook(bufferedReader);
        assertThat(book.getAuthor(), is("TestMock author"));
        assertThat(book.getTitle(), is("TestMock title"));
        assertThat(book.getTmpCities().size(), is(2));
        assertThat(book.getTmpCities().get(1), is("dubai"));
        assertThat(book.getTmpCities().get(0), is("zaranj"));

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
      
    }
    
}

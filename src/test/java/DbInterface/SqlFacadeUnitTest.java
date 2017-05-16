/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbInterface;

import DtoEntity.DtoBookAuthor;
import DtoEntity.DtoBookCity;
import DtoEntity.DtoCity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import sql.SqlFacade;

/**
 *
 * @author Jonathan
 */
public class SqlFacadeUnitTest {

    public SqlFacadeUnitTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void findCitiesTest() {
        DbInterface db = new SqlFacade();

        //adding cities to the list 
        List list = new ArrayList(Arrays.asList("city 1", "city 2", "city 3"));
        //calling the methood with the list
        List<DtoCity> result = db.findCities(list);
        assertThat(result.size(), is(3));
        assertThat(result.get(0).getName(), is("city 1"));
        assertThat(result.get(1).getName(), is("city 2"));
        assertThat(result.get(2).getName(), is("city 3"));
        
        assertThat(result.get(0).getLatitude(), is((double) 2));
        assertThat(result.get(1).getLatitude(), is((double) 2));
        assertThat(result.get(2).getLatitude(), is((double) 2));
        
        assertThat(result.get(0).getLongitude(), is((double) 1));
        assertThat(result.get(1).getLongitude(), is((double) 1));
        assertThat(result.get(2).getLongitude(), is((double) 1));

    }
    
    @Test
    public void getBooksByCityTest(){
          DbInterface db = new SqlFacade();
          List<DtoBookAuthor>  result = db.getBooksByCity("Copenhagen");
          
          assertThat(result.size(),is(2));
          assertThat(result.get(0).getTitle(),is("book 1"));
          assertThat(result.get(1).getAuthor(),is("author 1"));
          
    }
    
    @Test
    public void getCitiesByTitleTest(){
        DbInterface db = new SqlFacade();
        List<DtoCity> result = db.getCitiesByTitle("Title"); 
        assertThat(result.size(),is(2));
        assertThat(result.get(0).getName(), is("city 1"));
        assertThat(result.get(1).getName(), is("city 2"));
        assertThat(result.get(0).getLatitude(), is((double) 1));
        assertThat(result.get(1).getLatitude(), is((double) 2));
        assertThat(result.get(0).getLongitude(), is((double) 1));
        assertThat(result.get(1).getLongitude(), is((double) 2));
    }
    
       @Test
    public void getBooksByAuthorTest(){
          DbInterface db = new SqlFacade();
          List<DtoBookAuthor>  result = db.getBooksByAuthor("Author 1");
          
          assertThat(result.size(),is(2));
          assertThat(result.get(0).getTitle(),is("book 1"));
          assertThat(result.get(0).getAuthor(),is("author 1"));      
    }
    
    @Test
    public void getBooksByGeolocationTest(){
          DbInterface db = new SqlFacade();
          List<DtoBookCity>  result = db.getBooksByGeolocation(1,1);
          
          assertThat(result.size(),is(2));
          assertThat(result.get(0).getTitle(),is("book 1"));
          assertThat(result.get(0).getAuthor(),is("author 1"));
          assertThat(result.get(1).getTitle(),is("book 2"));
          assertThat(result.get(1).getAuthor(),is("author 2"));
          
    }
    
    
    
    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbInterface;

import DbInterface.Facade;
import DtoEntity.DtoBookAuthor;
import DtoEntity.DtoCity;
import entity.Book;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.mockito.Mockito;
import org.postgresql.ds.PGPoolingDataSource;
import sql.SqlDBConnector;
import sql.SqlFacade;

/**
 *
 * @author nikolai
 */
public class FacadeIntegrationTest {

    Facade instance;
    SqlFacade sql;
    Connection con;
    PGPoolingDataSource datasource;
    public FacadeIntegrationTest() {
        if (System.getenv("TRAVIS") != null) {
            datasource = new PGPoolingDataSource();
            datasource.setDataSourceName("Test travis");
            datasource.setServerName("127.0.0.1");
            datasource.setDatabaseName("travis_ci_test");
            datasource.setUser("travisUser");
            datasource.setPassword("travisPassfirebug");
            datasource.setPortNumber(5432);
        }
        else{
            datasource = new PGPoolingDataSource();
            datasource.setDataSourceName("Test db local");
            datasource.setServerName("188.226.136.226");
            datasource.setDatabaseName("test_db");
            datasource.setUser("administrator");
            datasource.setPassword("firebug");
            datasource.setPortNumber(5432);
        }
        
        
        
        sql = new SqlFacade(datasource);
        con = SqlDBConnector.setSource(datasource);

    }

    @BeforeClass
    public static void setUpClass() throws IOException, SQLException {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws FileNotFoundException, IOException, SQLException {
        instance = new Facade(sql);
        ScriptRunner runner = new ScriptRunner(con, false, false);
        runner.runScript(new BufferedReader(new FileReader(this.getClass().getResource("/insertScriptSqlForTest.sql").getFile())));
    }

    @After
    public void tearDown() {
        datasource.close();
    }

    /**
     * Test of findAllCities method, of class Facade.
     */
    @Test
    public void testFindAllCities() throws IOException, FileNotFoundException {
        //Should not be null
        BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/10267.txt")));

        //Book book = instance.findAllPossibleCitiesInBook("/home/nikolai/NetBeansProjects/Db_project/src/test/10267.txt");
        Book book = instance.findAllPossibleCitiesInBook(in);

        assertThat(book.getAuthor(), is("Captain Quincy Allen"));
        assertThat(book.getTitle(), is("The Outdoor Chums"));
        assertThat(book.getTmpCities().size(), is(17));
    }

    /* Test
        - First we test when the file dosent exists.
        - Second we test when the files exists.
     */
//    @Test
//    public void testInsertBooksWithCities() throws IOException {
//        BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/10267.txt")));
//
//        Book book = instance.findAllPossibleCitiesInBook(in);
//
//        boolean resFalse = instance.insertBooksWithCities("ThisFileDosentExists.txt");
//        assertThat(resFalse, is(false));
//
//        boolean resTrue = instance.insertBooksWithCities(this.getClass().getResource("/10267.txt").getFile());
//        assertThat(resTrue, is(true));
//    }

//    @Test
//    public void testinsertBooksWithCitiesHelper() throws IOException {
//        assertThat(instance.insertBooksWithCitiesHelper(), is(true));
//    }

    @Test
    public void getBooksByGeolocationTest() {
        List<DtoBookAuthor> result = instance.getBooksByGeolocation((double) 1.1, (double) 2.2);
    }

//    @Test
//    public void getBooksByAuthorTest() {
//        List<DtoBookAuthor> result = instance.getBooksByAuthor("Author");
//    }
//
//    @Test
//    public void getCitiesByTitleTest() {
//        List<DtoCity> result = instance.getCitiesByTitle("title");
//    }

    @Test
    public void getBooksByCityTest() {
        List<DtoBookAuthor> result = instance.getBooksByCity("city");
    }

}

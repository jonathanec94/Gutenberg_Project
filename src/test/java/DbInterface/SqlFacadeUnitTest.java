/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbInterface;

import DtoEntity.DtoBookAuthor;
import DtoEntity.DtoCity;
import entity.Book;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import sql.SqlFacade;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 *
 * @author Jonathan
 */
public class SqlFacadeUnitTest {

    DbInterface db;
    DataSource ds;

    Connection c;
    PreparedStatement stmt;
    ResultSet rs;

    public SqlFacadeUnitTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception, SQLException {
        ds = mock(DataSource.class);
        c = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
        db = new SqlFacade(ds);

        assertNotNull(ds);
        when(c.prepareStatement(any(String.class))).thenReturn(stmt);
        when(ds.getConnection()).thenReturn(c);

        when(stmt.executeQuery()).thenReturn(rs);
        when(stmt.executeUpdate()).thenReturn(1);
        when(c.prepareStatement(any(String.class))).thenReturn(stmt);;
    }

    @Test
    public void findCitiesTest() {

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
    public void getBooksByCityTest() throws SQLException {
        when(rs.getString("title")).thenReturn("Title mocked").thenReturn("Title mocked 2");
        when(rs.getString("author")).thenReturn("author mocked").thenReturn("author mocked 2");
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        List<DtoBookAuthor> result = db.getBooksByCity("Copenhagen");

        assertThat(result.size(), is(2));
        assertThat(result.get(0).getTitle(), is("Title mocked"));
        assertThat(result.get(0).getAuthor(), is("author mocked"));

    }

    @Test
    public void getCitiesByTitleTest() throws SQLException {
        when(rs.getString("name")).thenReturn("city 1").thenReturn("city 2");
        when(rs.getDouble("latitude")).thenReturn(1.1).thenReturn(1.2);
        when(rs.getDouble("longitude")).thenReturn(2.1).thenReturn(2.2);

        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        List<DtoCity> result = db.getCitiesByTitle("Title");

        assertThat(result.size(), is(2));
        assertThat(result.get(0).getName(), is("city 1"));
        assertThat(result.get(1).getName(), is("city 2"));
        assertThat(result.get(0).getLatitude(), is(1.1));
        assertThat(result.get(1).getLatitude(), is(1.2));
        assertThat(result.get(0).getLongitude(), is(2.1));
        assertThat(result.get(1).getLongitude(), is(2.2));
    }

    @Test
    public void getBooksByAuthorTest() throws SQLException {
        when(rs.getInt("id")).thenReturn(1).thenReturn(2);
        when(rs.getString("author")).thenReturn("author mocked");

        when(rs.getString("name")).thenReturn("city 1").thenReturn("city 2");
        when(rs.getDouble("latitude")).thenReturn(1.1).thenReturn(1.2);
        when(rs.getDouble("longitude")).thenReturn(2.1).thenReturn(2.2);

        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        List<DtoBookAuthor> result = db.getBooksByAuthor("Author 1");

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getAuthor(), is("author mocked"));
        assertThat(result.get(0).getCities().get(0).getName(), is("city 1"));
        assertThat(result.get(0).getCities().get(1).getName(), is("city 2"));
        assertThat(result.get(0).getCities().size(), is(2));
        assertThat(result.get(0).getCities().get(0).getLatitude(), is(1.1));
        assertThat(result.get(0).getCities().get(1).getLatitude(), is(1.2));
        assertThat(result.get(0).getCities().get(0).getLongitude(), is(2.1));
        assertThat(result.get(0).getCities().get(1).getLongitude(), is(2.2));

    }

    @Test
    public void getBooksByGeolocationTest() throws SQLException {
        when(rs.getString("name")).thenReturn("city 1").thenReturn("city 2");
        when(rs.getDouble("latitude")).thenReturn(1.1).thenReturn(1.2);
        when(rs.getDouble("longitude")).thenReturn(2.1).thenReturn(2.2);

        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        List<DtoCity> result = db.getBooksByGeolocation(1, 1);

        assertThat(result.size(), is(2));
        assertThat(result.get(0).getName(), is("city 1"));
        assertThat(result.get(0).getLatitude(), is(1.1));
        assertThat(result.get(0).getLongitude(), is(2.1));
        assertThat(result.get(1).getName(), is("city 2"));
        assertThat(result.get(1).getLatitude(), is(1.2));
        assertThat(result.get(1).getLongitude(), is(2.2));

    }
/*
    @Test
    public void insertBookTest() throws SQLException {
        Book book = new Book("Title", "author");
        List<String> list = new ArrayList();
        list.add("City 1");
        list.add("City 2");
        book.setTmpCities(list);

        when(rs.getInt(1)).thenReturn(100);
        when(rs.next()).thenReturn(true).thenReturn(false);

        boolean result = db.insertBook(book);
    }
*/
    @Test
    public void tryCatchTest() throws SQLException {
        when(rs.getString("title")).thenThrow(new SQLException());
        when(rs.getString("name")).thenThrow(new SQLException());
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(true);

        List<DtoBookAuthor> getBooksByCity = db.getBooksByCity("Copenhagen");
        List<DtoCity> getCitiesByTitle = db.getCitiesByTitle("Title");
        List<DtoBookAuthor> getBooksByAuthor = db.getBooksByAuthor("Author 1");
        List<DtoCity> getBooksByGeolocation = db.getBooksByGeolocation(1, 1);

        assertNull(getBooksByCity);
        assertNull(getCitiesByTitle);
        assertNull(getBooksByAuthor);
        assertNull(getBooksByGeolocation);
    }

}

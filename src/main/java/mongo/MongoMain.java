package mongo;

import entity.Book;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by joachimdittman on 10/05/2017.
 */
public class MongoMain {

    public static void main(String[] args) {
        MongoFacade m = new MongoFacade();
        List list = new ArrayList<>();
        list.add("Skælskør");
        list.add("København");
        list.add("Esbjerg");
        System.out.println(m.findCities(list).size());

        Book book = new Book();
        book.setAuthor("Arne");
        book.setCities(list);
        book.setTitle("The worlds end");
        book.setTmpCities(list);
        m.insertBook(book);

        m.getBooksByCity("Esbjerg");
        m.getCitiesByTitle("Trucuk");
        m.getBooksByAuthor("Arne");
        m.getBooksByGeolocation(52.0,9.0);
    }


}
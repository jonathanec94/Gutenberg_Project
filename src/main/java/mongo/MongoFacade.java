package mongo;

import DbInterface.DbInterface;
import DtoEntity.DtoBookAuthor;
import DtoEntity.DtoCity;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entity.Book;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Filters.*;

//import static com.mongodb.client.model.Filters.eq;

/**
 *
 * @author nikolai
 */
public class MongoFacade implements DbInterface {

    private MongoDatabase database = MongoDBConnector.getDBConnection();

    @Override
    public List<DtoCity> findCities(List<String> city) {
        List list = new ArrayList<>();
        MongoCollection<Document> cityCollection = database.getCollection("cities");
        List<Document> cities = cityCollection.find().into(new ArrayList<Document>());

        for (Document cityObject : cities) {
            System.out.println(cityObject);
            if (cityObject.get("latitude").toString().isEmpty() == false && cityObject.get("longitude").toString().isEmpty() == false) {

                list.add(new DtoCity(cityObject.get("name").toString(), Double.parseDouble(cityObject.get("latitude").toString()), Double.parseDouble(cityObject.get("longitude").toString())));
            }
        }
        return list;
    }

    @Override
    public boolean insertBook(Book book) {
        MongoCollection<Document> books = database.getCollection("books");
        books.insertOne(new Document().append("author", book.getAuthor()).append("title", book.getTitle()).append("cities", book.getCities()).append("tmpCities", book.getTmpCities()));
        return true;
    }

    /**
     * Given a city name the method returns all book titles
     * with corresponding authors that mention this city.
     */
    @Override
    public List<DtoBookAuthor> getBooksByCity(final String city) {
        List<DtoBookAuthor> result = new ArrayList<>();
        FindIterable<Document> documents = database.getCollection("books").find(eq("cities", city));

        for (Document document: documents) {
            result.add(new DtoBookAuthor(document.get("title").toString(), document.get("author").toString()));
        }

        return result;
    }

    /**
     * Given a book title, the method fetches all cities mentioned
     * in the book and plots all the cities onto a map.
     */
    @Override
    public List<DtoCity> getCitiesByTitle(final String title) {
        List<DtoCity> result = new ArrayList<>();
        ArrayList bookCities = new ArrayList();

        FindIterable<Document> books = database.getCollection("books").find(eq("title", title));

        for (Document book: books) {
            bookCities = (ArrayList) book.get("cities");
        }

        for (int i = 0; i < bookCities.size(); i++) {
            FindIterable<Document> cities = database.getCollection("cities").find(eq("name", bookCities.get(i)));

            for (Document city: cities) {
                ArrayList coordinates = (ArrayList) city.get("location", Document.class).get("coordinates");
                result.add(new DtoCity(city.getString("name"),
                        Double.valueOf(coordinates.get(0).toString()),
                        Double.valueOf(coordinates.get(1).toString())));
            }
        }

        plotCitiesToMap(result);

        return result;
    }

    /**
     * Given an author name the method lists all books written by that author
     * and plots all cities mentioned in any of the books onto a map.
     */
    @Override
    public List<DtoBookAuthor> getBooksByAuthor(final String author) {
        List<DtoCity> result = new ArrayList<>();
        List<DtoBookAuthor> bookAuthor = new ArrayList<>();
        Set<String> cities = new HashSet<>();

        FindIterable<Document> books = database.getCollection("books").find(eq("author", author));

        for (Document book: books) {

            bookAuthor.add(new DtoBookAuthor(book.getString("title"), book.getString("author")));

            ArrayList bookCities = (ArrayList) book.get("cities");

            for (int i = 0; i < bookCities.size(); i++) {
                cities.add(bookCities.get(i).toString());
            }
        }

        for (String cityString : cities) {
            FindIterable<Document> citiesList = database.getCollection("cities").find(eq("name", cityString));

            for (Document city : citiesList) {
                ArrayList coordinates = (ArrayList) city.get("location", Document.class).get("coordinates");
                result.add(new DtoCity(city.getString("name"),
                        Double.valueOf(coordinates.get(0).toString()),
                        Double.valueOf(coordinates.get(1).toString())));
            }

        }

        plotCitiesToMap(result);

        return bookAuthor;
    }

    /**
     * Given a geolocation, the method lists all books mentioning
     * a city in vicinity of the given geolocation.
     */
    @Override
    public List<DtoBookAuthor> getBooksByGeolocation(double latitude, double longitude) {
        List<DtoBookAuthor> result = new ArrayList<>();

        double distance = 10 / 6378.1;

        FindIterable<Document> cities = database.getCollection("cities").find(
                geoWithinCenterSphere("location", longitude, latitude, distance));

        for (Document city: cities) {
            FindIterable<Document> books = database.getCollection("books")
                    .find(eq("cities", city.getString("name")));

            for (Document book : books) {
                result.add(new DtoBookAuthor(book.getString("title"), book.getString("author")));
            }
        }

        return result;
    }

    public void plotCitiesToMap(List<DtoCity> cities) {

    }

}

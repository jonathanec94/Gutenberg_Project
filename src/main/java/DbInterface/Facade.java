/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbInterface;

import DtoEntity.DtoBookAuthor;
import DtoEntity.DtoCity;
import entity.Book;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nikolai
 */
public class Facade {

    DbInterface db;
    BufferedReader in;
    private HashSet<String> englishWords;

    public Facade(DbInterface db) {
        this.db = db;
        this.englishWords = getEnglishWords(new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/all_cities_txt.csv"))));
    }

    public boolean insertBooksWithCitiesHelper() throws IOException {
        boolean result = true;
        for (int i = 39999; i < 54661; i++) {
            String path = "/home/nikolai/Desktop/dbtextfiles/txt/" + i + ".txt";
            insertBooksWithCities(path);
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
                db.insertBook(book);
            }
        } else {
            //System.out.println("File does not exist: " + path);
            return false;
        }

        return true;
    }

    public Book findAllPossibleCitiesInBook(BufferedReader in) throws FileNotFoundException, IOException {
        Book book = new Book();
        book.setAuthor("Unknown");
        book.setTitle("Unknown");

        String line;
        String title;
        String author;

        boolean isBookStarted = false;
        HashSet<String> setWords = new HashSet<String>();

        while ((line = in.readLine()) != null) {
            String lineLower = line.toUpperCase().replaceAll(" ", "");
            if (lineLower.contains("***START")) {
                isBookStarted = true;
            } else if (!isBookStarted && line.toLowerCase().contains("title")) {
                title = line.replace("Title: ", "");
                if (title.length() < 500) {
                    book.setTitle(title);
                } else {
                    book.setTitle("");
                }
            } else if (!isBookStarted && line.toLowerCase().contains("author")) {
                author = line.replace("Author: ", "");
                if (author.length() < 500) {
                    book.setAuthor(author);
                } else {
                    book.setAuthor("");
                }
            }

            //Look through the book after the cities  
            if (isBookStarted) {
                //Find all words that starts with a uppercase
                String pattern = "([A-Z])\\w+";
                //String pattern = "^([A-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*$";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(line);

                //now match the word with the cities
                if (m.find()) {
                    String tmpStr = m.group(0);
                    if (tmpStr.length() > 2) {
                        String word = m.group(0).toLowerCase();
                        //if(!englishWords.contains(word))
                        if (englishWords.contains(word)) {
                            setWords.add(word);
                        }

                    }
                }
            }
        }
        in.close();

        List<String> list = new ArrayList<String>(setWords);
        book.setTmpCities(list);

        return book;
    }

    public HashSet getEnglishWords(BufferedReader in) {
        //BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/10000-english-word.txt")));
        //BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/all_cities_txt.csv")));
        String line;

        HashSet hs = new HashSet();

        try {
            while ((line = in.readLine()) != null) {
                hs.add(line.toLowerCase());
            }
            in.close();
        } catch (IOException ex) {
            hs.add("ReadingfileFailed");
        }
        return hs;
    }

    public void generateMap(List<DtoCity> cities) throws IOException {
        String baseUrl = "https://maps.googleapis.com/maps/api/staticmap?size=640x640&scale=2&markers=color:red|55.67594,12.56553|50.98715,4.83695";

        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);

        for (DtoCity city : cities) {
            sb.append("|");
            sb.append(city.getLongitude());
            sb.append(",");
            sb.append(city.getLatitude());
        }

        URL url = new URL(sb.toString());
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream("/home/mathias/Project/Gutenberg/src/main/java/mongo/" + System.currentTimeMillis() + ".png");

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    public List<DtoBookAuthor> getBooksByCity(String city) {
        return db.getBooksByCity(city);
    }

    public List<DtoCity> getCitiesByTitle(String title) {
        List<DtoCity> listDtoCity = db.getCitiesByTitle(title);
        try {
            generateMap(listDtoCity);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return listDtoCity;
    }

    public List<DtoBookAuthor> getBooksByAuthor(String author) {
        List<DtoBookAuthor> listDtoBookAuthor = db.getBooksByAuthor(author);
        try {
            List<DtoCity> allDtoCities = new ArrayList<>();
            for (DtoBookAuthor dtoBookAuthor : listDtoBookAuthor) {
                for (DtoCity dtoCity : dtoBookAuthor.getCities()) {
                    allDtoCities.add(dtoCity);
                }
            }
            generateMap(allDtoCities);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return listDtoBookAuthor;
    }

    public List<DtoBookAuthor> getBooksByGeolocation(double latitude, double longitude) {
        return db.getBooksByGeolocation(latitude, longitude);
    }
}

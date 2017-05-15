/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DbInterface;

import DbInterface.DbInterface;
import DtoEntity.DtoCity;
import entity.Book;
import entity.City;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        this.englishWords = getEnglishWords();
    }

    public boolean insertBooksWithCitiesHelper() {
        boolean result = true;
        for (int i = 39999; i < 54661; i++) {
            String path = "/home/nikolai/Desktop/dbtextfiles/txt/" + i + ".txt";
            insertBooksWithCities(path);
        }
        return result;
    }

    public boolean insertBooksWithCities(String path) {
//        System.out.println("path: "+path);
        //find all book files here in folder
        if (new File(path).exists()) {

            Book book = null;
            try {
                book = findAllPossibleCitiesInBook(new BufferedReader(new FileReader(path))); 
            } catch (IOException ex) {
               // System.out.println("Error in method insertBooksWithCities() - value: " + path);
                //ex.printStackTrace();
            }

            if (book != null) {
                db.insertBook(book);
            } else {
              //  System.out.println("Error in insertBooksWithCities()");
            }

//                System.out.println(path + ":----------" + book.toString());
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
                if(title.length() < 500)
                    book.setTitle(title);
                else
                    book.setTitle("");
            } else if (!isBookStarted && line.toLowerCase().contains("author")) {
                author = line.replace("Author: ", "");
                if(author.length() < 500)
                    book.setAuthor(author);
                else
                    book.setAuthor("");
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

    public HashSet getEnglishWords() {
        //BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/10000-english-word.txt")));
        BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/all_cities_txt.csv")));
        String line;

        HashSet hs = new HashSet();

        try {
            while ((line = in.readLine()) != null) {
                hs.add(line.toLowerCase());
            }
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hs;
    }

}

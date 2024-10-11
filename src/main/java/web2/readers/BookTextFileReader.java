package web2.readers;

import web2.entities.Author;
import web2.entities.Book;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class BookTextFileReader {
    public DataBooks data;
    public void loadListOfBooks(String filePathBooks, String filePathAuthors) {
        List<Book> books = new ArrayList<>();
        List<String> booksLinksToAuthors = new ArrayList<>();
        try (BufferedReader input = new BufferedReader(new FileReader(filePathBooks))) {
            String line;
            String[] splitLine;
            while ((line = input.readLine()) != null) {
                String[] booksStr = line.split(";");
                for (String bookStr : booksStr) {
                    splitLine = bookStr.split("\\|");
                    try {
                        if (splitLine.length != 4)
                            throw new InvalidParameterException("Should be 4 parameters");
                        Integer.parseInt(splitLine[0]);
                        Integer.parseInt(splitLine[2]);
                        books.add(new Book(Integer.parseInt(splitLine[0]), splitLine[1], Integer.parseInt(splitLine[2])));
                        booksLinksToAuthors.add(splitLine[0] + "," + splitLine[3]);
                    } catch (Exception ex) {
                        System.out.println(ex.getClass().getName() + ": " + ex.getMessage());
                    }
                }

            }
        }
        catch (IOException ex) {
            System.out.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        List<Author> authors = loadListOfAuthors(filePathAuthors);
        data = new DataBooks(books, authors, booksLinksToAuthors);
    }

    private static List<Author> loadListOfAuthors(String filePathAuthors) {
        List<Author> authors = new ArrayList<>();
        try (BufferedReader input = new BufferedReader(new FileReader(filePathAuthors))) {
            String line;
            String[] splitLine;
            while ((line = input.readLine()) != null) {
                String[] authorsStr = line.split(";");
                for (String authorStr : authorsStr) {
                    splitLine = authorStr.split("\\|");
                    try {
                        if (splitLine.length != 4)
                            throw new InvalidParameterException("Should be 4 parameters");
                        Integer.parseInt(splitLine[0]);
                        authors.add(new Author(Integer.parseInt(splitLine[0]), splitLine[1], splitLine[2]));
                    } catch (Exception ex) {
                        System.out.println(ex.getClass().getName() + ": " + ex.getMessage());
                    }
                }

            }
        }
        catch (IOException ex) {
            System.out.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        return authors;
    }

    public class DataBooks {
        public List<Book> bookList;
        public List<Author> authorList;
        public List<String> booksLinksToAuthors;

        public DataBooks(List<Book> bookList, List<Author> authorList, List<String> booksLinksToAuthors) {
            this.bookList = bookList;
            this.authorList = authorList;
            this.booksLinksToAuthors = booksLinksToAuthors;
        }
    }
}


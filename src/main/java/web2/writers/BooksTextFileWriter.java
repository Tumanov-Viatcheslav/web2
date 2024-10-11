package web2.writers;

import web2.entities.Author;
import web2.entities.Book;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class BooksTextFileWriter {
    public void writeBook(Book book, String fileName) {
        File file = new File(fileName);
        String regex = "";
        if (file.exists() && file.length() != 0)
            regex = ";";
        try (BufferedWriter output = new BufferedWriter(new FileWriter(fileName, true))) {
            output.write(regex + book.toString());
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void writeAuthor(Author author, String fileName) {
        File file = new File(fileName);
        String regex = "";
        if (file.exists() && file.length() != 0)
            regex = ";";
        try (BufferedWriter output = new BufferedWriter(new FileWriter(fileName, true))) {
            output.write(regex + author.toString());
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}

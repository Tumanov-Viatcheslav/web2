package web2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import web2.entities.Author;
import web2.entities.Book;
import web2.readers.BookTextFileReader;
import web2.writers.BooksTextFileWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {
    List<Book> bookList = new ArrayList<>();
    List<Author> authorList = new ArrayList<>();

    public List<Book> getAllBookList() {
        return bookList.stream().toList();
    }

    public List<Book> getFilteredBookList(String searchValue, String filter, String year) {
        List<Book> booksFiltered = new ArrayList<>();
        if (filter.equals("bookFilter"))
            booksFiltered = filterByBook(searchValue, year);
        else booksFiltered = filterByAuthor(searchValue);
        return booksFiltered;
    }

    private List<Book> filterByBook(String searchValue, String yearStr) {
        List<Book> booksFiltered = new ArrayList<>();
        int year;
        for (Book book : bookList) {
            if (yearStr.isEmpty())
                year = book.getYear();
            else year = Integer.parseInt(yearStr);
            if (book.getTitle().toLowerCase().contains(searchValue.toLowerCase()) && book.getYear() == year)
                booksFiltered.add(book);
        }
        return booksFiltered;
    }

    private List<Book> filterByAuthor(String searchValue) {
        List<Book> booksFiltered = new ArrayList<>();
        for (Author author : authorList) {
            if (author.getNameAndSurname().toLowerCase().contains(searchValue.toLowerCase()))
                booksFiltered.addAll(author.getBooks());
        }
        return booksFiltered;
    }

    public int newBookId() {
        int max = 0;
        for (Book book : bookList)
            if (max < book.getId())
                max = book.getId();
        return max + 1;
    }

    public int newAuthorId() {
        int max = 0;
        for (Author author : authorList)
            if (max < author.getId())
                max = author.getId();
        return max + 1;
    }

    public Book getBookById(int id) {
        for (Book book : bookList)
            if (book.getId() == id)
                return book;
        return null;
    }

    public Book getBookByTitleAndYear(String title, int year) {
        for (Book book : bookList)
            if (book.getTitle().equals(title) && book.getYear() == year)
                return book;
        return null;
    }

    public Author getAuthorById(int id) {
        for (Author author : authorList)
            if (author.getId() == id)
                return author;
        return null;
    }
    public Author getAuthorByNameAndSurname(String name, String surname) {
        for (Author author : authorList)
            if (author.getName().equals(name) && author.getSurname().equals(surname))
                return author;
        return null;
    }

    public void addBook(String bookTitle, String bookYear, List<Author> authors) throws Exception {
        if (getBookByTitleAndYear(bookTitle, Integer.parseInt(bookYear)) != null)
            return;
        Book book = new Book(newBookId(), bookTitle, Integer.parseInt(bookYear));
        Author authorToLink;
        for (Author author : authors) {
            if ((authorToLink = getAuthorByNameAndSurname(author.getName(), author.getSurname())) == null) {
                authorList.add(author);
                authorToLink = author;
            }
            book.addAuthor(authorToLink);
            authorToLink.addBook(book);

        }
        addBookOnly(book);
        BooksTextFileWriter writer = new BooksTextFileWriter();
        writer.writeBook(book, "src/main/resources/books.txt");
        for (Author author : book.getAuthors())
            writer.writeAuthor(author, "src/main/resources/authors.txt");
    }

    private void addBookOnly(Book book) {
        bookList.add(book);
    }

    private void addAuthor(Author author) {
        authorList.add(author);
    }

    private void addAuthors(List<Author> authors) {
        authorList.addAll(authors);
    }

    @PostConstruct
    public void loadInitialDataFromTextFile() {
        BookTextFileReader bookTextFileReader = new BookTextFileReader();
        if (new File("src/main/resources/books.txt").exists() && new File("src/main/resources/authors.txt").exists()) {
            bookTextFileReader.loadListOfBooks("src/main/resources/books.txt", "src/main/resources/authors.txt");
            bookList.addAll(bookTextFileReader.data.bookList);
            authorList.addAll(bookTextFileReader.data.authorList);
            for (String bookLinksToAuthors : bookTextFileReader.data.booksLinksToAuthors)
                linkAuthors(bookLinksToAuthors);
        }
    }

    private void linkAuthors(String bookLinksToAuthors) {
        String[] splitLine = bookLinksToAuthors.split(",");
        int bookId = Integer.parseInt(splitLine[0]);
        Book book = getBookById(bookId);
        Author author;
        for (int i = 1; i < splitLine.length; i++) {
            author = getAuthorById(Integer.parseInt(splitLine[i]));
            book.addAuthor(author);
            author.addBook(book);
        }
    }
}

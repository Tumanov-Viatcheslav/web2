package web2.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Author {
    private int id;
    private String name, surname;
    private final List<Book> books = new ArrayList<>();

    public Author(int id, String name, String surname) throws Exception {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return immutable list of books
     */
    public List<Book> getBooks() {
        return books.stream().toList();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addBooks(List<Book> booksToAdd) {
        books.addAll(booksToAdd);
    }

    public String getNameAndSurname() {
        return getName() + " " + getSurname();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author author)) return false;
        return Objects.equals(name, author.name) && Objects.equals(surname, author.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
    }

    @Override
    public String toString() {
        StringBuilder booksIdStr = new StringBuilder();
        for (Book book : books)
            booksIdStr.append(book.getId()).append(",");
        booksIdStr.replace(booksIdStr.length() - 1, booksIdStr.length(), "");
        return id +
                "|" + name +
                "|" + surname +
                "|" + booksIdStr;
    }
}

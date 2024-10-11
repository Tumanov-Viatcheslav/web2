package web2.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Book {
    private int id;
    private String title;
    private int year;
    private final List<Author> authors = new ArrayList<>();

    public Book(int id, String title, int year) throws Exception {
        if (year > 2024)
            throw new Exception("We do not accept books from future");
        this.id = id;
        this.title = title;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return immutable list of authors
     */
    public List<Author> getAuthors() {
        return authors.stream().toList();
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void addAuthors(List<Author> authorsToAdd) {
        authors.addAll(authorsToAdd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return year == book.year && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year);
    }

    @Override
    public String toString() {
        StringBuilder authorsIdStr = new StringBuilder();
        for (Author author : authors)
            authorsIdStr.append(author.getId()).append(",");
        authorsIdStr.replace(authorsIdStr.length() - 1, authorsIdStr.length(), "");
        return id +
                "|" + title +
                "|" + year +
                "|" + authorsIdStr;
    }
}

package web2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web2.entities.Author;
import web2.entities.Book;
import web2.services.LibraryService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LibraryController {
    @Autowired
    LibraryService libraryService;

    @GetMapping("/")
    public String index1() {
        return "redirect:library";
    }
    @GetMapping("/index")
    public String index2() {
        return "redirect:library";
    }

    @GetMapping("/library")
    public String library(
            Model model,
            @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(name = "filter", defaultValue = "") String filter,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "isBookFilter", defaultValue = "false") boolean isBookFilter
    ) {
        List<Book> bookList;
        if ((searchValue.isEmpty() && year.isEmpty()) ||
                (searchValue.isEmpty() && filter.equals("authorFilter"))) {
            if (filter.equals("bookFilter")) {
                model.addAttribute("isBookFilter", true);
            }
            bookList = libraryService.getAllBookList();
        }
        else try {
            if (filter.equals("bookFilter"))
                model.addAttribute("isBookFilter", true);
            model.addAttribute("searchValue", searchValue);
            model.addAttribute("filter", filter);
            model.addAttribute("year", year);
            try {
                if (!year.isEmpty())
                    Integer.parseInt(year);
            } catch (NumberFormatException e) {
                throw new Exception("Year should be integer");
            }
            bookList = libraryService.getFilteredBookList(searchValue, filter, year);
        } catch (Exception e) {
            System.out.println("Error " + e.getClass().getName() + ": " + e.getMessage());
            bookList = libraryService.getAllBookList();
        }

        model.addAttribute("bookList", bookList);
        return "library";
    }

    @GetMapping("/add_book")
    public String add_book(
            Model model,
            @RequestParam(name = "bookTitle", defaultValue = "") String bookTitle,
            @RequestParam(name = "bookYear", defaultValue = "") String bookYear,
            @RequestParam(name = "authorName1", defaultValue = "") String authorName1,
            @RequestParam(name = "authorSurname1", defaultValue = "") String authorSurname1,
            @RequestParam(name = "authorName2", defaultValue = "") String authorName2,
            @RequestParam(name = "authorSurname2", defaultValue = "") String authorSurname2,
            @RequestParam(name = "authorName3", defaultValue = "") String authorName3,
            @RequestParam(name = "authorSurname3", defaultValue = "") String authorSurname3,
            @RequestParam(name = "authorName4", defaultValue = "") String authorName4,
            @RequestParam(name = "authorSurname4", defaultValue = "") String authorSurname4
    ) {
        if (bookTitle.isEmpty() || bookYear.isEmpty() || authorName1.isEmpty() || authorSurname1.isEmpty()) {
        model.addAttribute("report", "Введите все параметры книги");
        return "add_book";
        }
        try {
            List<Author> authors = new ArrayList<>();
            int i = 0;
            authors.add(new Author(libraryService.newAuthorId(), authorName1, authorSurname1));
            i++;
            if (!authorName2.isEmpty() && !authorSurname2.isEmpty()) {
                authors.add(new Author(libraryService.newAuthorId() + i, authorName2, authorSurname2));
                i++;
            }
            if (!authorName3.isEmpty() && !authorSurname3.isEmpty()) {
                authors.add(new Author(libraryService.newAuthorId() + i, authorName3, authorSurname3));
                i++;
            }
            if (!authorName4.isEmpty() && !authorSurname4.isEmpty()) {
                authors.add(new Author(libraryService.newAuthorId() + i, authorName4, authorSurname4));
            }
            libraryService.addBook(bookTitle, bookYear, authors);
            model.addAttribute("bookTitle", bookTitle);
            model.addAttribute("bookYear", bookYear);
            model.addAttribute("authorName", authorName1);
            model.addAttribute("authorSurname", authorSurname1);
        } catch (Exception e) {
            model.addAttribute("report", "Ошибка " +
                    e.getClass().getName() + ": " + e.getMessage());
            return "add_book";
        }
        return "redirect:library";
    }
}

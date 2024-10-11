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
public class AuthorsController {
    @Autowired
    LibraryService libraryService;

    @GetMapping("/authors")
    public String authors(
            Model model,
            @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(name = "filter", defaultValue = "") String filter,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "isBookFilter", defaultValue = "false") boolean isBookFilter
    ) {
        List<Author> authorList;
        if ((searchValue.isEmpty() && year.isEmpty()) ||
                (searchValue.isEmpty() && filter.equals("authorFilter"))) {
            if (filter.equals("bookFilter")) {
                model.addAttribute("isBookFilter", true);
            }
            authorList = libraryService.getAllAuthorList();
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
            authorList = libraryService.getFilteredAuthorList(searchValue, filter, year);
        } catch (Exception e) {
            System.out.println("Error " + e.getClass().getName() + ": " + e.getMessage());
            authorList = libraryService.getAllAuthorList();
        }

        model.addAttribute("authorList", authorList);
        return "authors";
    }
}

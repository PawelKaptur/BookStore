package pl.jstk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jstk.entity.BookEntity;
import pl.jstk.enumerations.BookStatus;
import pl.jstk.mapper.BookMapper;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;


@Controller
public class BookContoller {

    private BookService bookService;

    @Autowired
    public BookContoller(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String getBooks(Model model) {
        model.addAttribute("bookList", bookService.findAllBooks());
        return "books";
    }

    @GetMapping("/books/{bookId}")
    public String getBookById(@RequestParam("id") Long bookId, Model model) {
        model.addAttribute("book", bookService.findBookbyId(bookId));
        return "book";
    }

    @GetMapping("/books/add")
    public String addBook(Model model){
        model.addAttribute("newBook", new BookTo());
        return "addBook";
    }

    @PostMapping("/greeting")
    public String addBook(@ModelAttribute("newBook") BookTo book, String title, String authors, BookStatus bookStatus){
        book.setId(213L);
        book.setTitle(title);
        book.setAuthors(authors);
        book.setStatus(bookStatus);
        bookService.saveBook(book);
        return "welcome";
    }
}

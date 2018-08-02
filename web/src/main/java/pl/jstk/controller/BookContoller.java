package pl.jstk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

import java.util.List;


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
    public String addBook(Model model) {
        model.addAttribute("newBook", new BookTo());
        return "addBook";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/greeting")
    public String addBook(@ModelAttribute("newBook") BookTo book, Model model) {
        bookService.saveBook(book);
        model.addAttribute("bookAdded", "Book was successfully added");
        model.addAttribute("book", book);
        return "welcome";
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/books/remove/{bookId}")
    public String removeBookById(@RequestParam("id") Long bookId, Model model) {
        bookService.deleteBook(bookId);
        model.addAttribute("bookRemoved", "Book was successfully removed.");
        return getBooks(model);
    }

    @GetMapping("/books/find")
    public String findBook(Model model) {
        model.addAttribute("book", new BookTo());
        return "findBook";
    }

    @PostMapping("/books/find")
    public String find(@ModelAttribute("book") BookTo book, Model model) {
        List<BookTo> books = bookService.findBooksByParams(book);
        model.addAttribute("books", books);
        if (books.size() == 0) {
            model.addAttribute("noBooks", "No books were found.");
        }

        return "findBook";
    }

    @ExceptionHandler({AccessDeniedException.class})
    public String handleException(Model model) {
        model.addAttribute("error", "Access denied, normal user cannot remove books");
        return "403";
    }
}

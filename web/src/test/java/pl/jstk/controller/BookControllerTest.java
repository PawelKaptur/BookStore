package pl.jstk.controller;

import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @MockBean
    private BookService bookServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldfindBooks() throws Exception {
        //given
        List<BookTo> books = new ArrayList<>();
        books.add(new BookTo());

        //when
        when(bookServiceMock.findAllBooks()).thenReturn(books);
        ResultActions resultActions = mockMvc.perform(get("/books"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("bookList", books));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldFindBook() throws Exception{
        //given
        BookTo book = new BookTo();
        long id = 10L;

        //when
        when(bookServiceMock.findBookbyId(id)).thenReturn(book);
        ResultActions resultActions = mockMvc.perform(get("/books/id").param("id", "10"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().attribute("book", book));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldReturnAddBookView() throws Exception{
        //when
        ResultActions resultActions = mockMvc.perform(get("/books/add"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("addBook"))
                .andExpect(model().attributeExists("newBook"));
    }


/*    @PostMapping("/greeting")
    public String addBook(@ModelAttribute("newBook") BookTo book, Model model) {
        bookService.saveBook(book);
        model.addAttribute("bookAdded", "Book was successfully added");
        model.addAttribute("book", book);
        return "welcome";
    }*/

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldAddBook() throws Exception{
        //given
        BookTo book = new BookTo();

        //when
        when(bookServiceMock.saveBook(book)).thenReturn(book);
        ResultActions resultActions = mockMvc.perform(post("greeting").flashAttr("newBook", book));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("welcome"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("bookAdded","Book was successfully added"));
    }

/*    @Secured("ROLE_ADMIN")
    @DeleteMapping("/books/remove/{bookId}")
    public String removeBookById(@RequestParam("id") Long bookId, Model model) {
        bookService.deleteBook(bookId);
        model.addAttribute("bookRemoved", "Book was successfully removed.");
        return getBooks(model);
    }*/

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldRemoveBook() throws Exception{
        //given
        List<BookTo> books = new ArrayList<>();
        long id = 10;
        books.add(new BookTo());

        //when
        //when(bookServiceMock.deleteBook(id)).thenReturn(books.get(0));
        ResultActions resultActions = mockMvc.perform(delete("books/remove/bookId").param("id", "10"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("bookRemoved", "Book was successfully removed."));
    }

/*    @GetMapping("/books/find")
    public String findBook(Model model) {
        model.addAttribute("book", new BookTo());
        return "findBook";
    }*/
}

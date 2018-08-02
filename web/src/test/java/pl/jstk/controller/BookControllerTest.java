package pl.jstk.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    public void shouldfindAllBooks() throws Exception {
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
    public void shouldFindBookById() throws Exception {
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
    public void shouldReturnAddBookView() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/books/add"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("addBook"))
                .andExpect(model().attributeExists("newBook"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    public void shouldAddBook() throws Exception {
        //given
        BookTo book = new BookTo();
        book.setId(6L);

        //when
        when(bookServiceMock.saveBook(book)).thenReturn(book);
        ResultActions resultActions = mockMvc.perform(post("/greeting").with(user("admin")).flashAttr("newBook", book));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("welcome"))
                .andExpect(model().attribute("bookAdded", "Book was successfully added"))
                .andExpect(model().attribute("book", book));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldRemoveBook() throws Exception {
        //given
        List<BookTo> books = new ArrayList<>();
        books.add(new BookTo());

        //when
        ResultActions resultActions = mockMvc.perform(delete("/books/remove/bookId").param("id", "10"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("bookRemoved", "Book was successfully removed."));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldReturnFindBookView() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/books/find"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("findBook"))
                .andExpect(model().attributeExists("book"));
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldFindBooks() throws Exception {
        //given
        List<BookTo> books = new ArrayList<>();
        BookTo book = new BookTo();
        book.setTitle("qwertz");
        books.add(book);

        //when
        when(bookServiceMock.findBooksByParams(book)).thenReturn(books);
        ResultActions resultActions = mockMvc.perform(post("/books/find").flashAttr("books", book));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("findBook"))
                .andExpect(model().attribute("books", books));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void shouldRedirectTo403View() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(delete("/books/remove/bookId").param("id", "0"));

        //then
        resultActions.andExpect(status().is4xxClientError())
                .andExpect(view().name("403"))
                .andExpect(model().attribute("error", "Access denied, normal user cannot remove books"));
    }
}

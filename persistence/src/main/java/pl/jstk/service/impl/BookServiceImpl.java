package pl.jstk.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jstk.entity.BookEntity;
import pl.jstk.mapper.BookMapper;
import pl.jstk.repository.BookRepository;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookTo> findAllBooks() {
        return BookMapper.map2To(bookRepository.findAll());
    }

    @Override
    public List<BookTo> findBooksByTitle(String title) {
        return BookMapper.map2To(bookRepository.findBookByTitle(title));
    }

    @Override
    public List<BookTo> findBooksByAuthor(String author) {
        return BookMapper.map2To(bookRepository.findBookByAuthor(author));
    }

    /**
     * Method get BookTo object in parameter and it searching with streams
     * fork books with same title and authors
     *
     * @param book to object that can have two parameters, title and authors
     * @return list of found books
     */
    @Override
    public List<BookTo> findBooksByParams(BookTo book) {
        List<BookTo> books = findAllBooks();
        if (book.getTitle().length() != 0) {
            books = books.stream().filter(b -> b.getTitle().equals(book.getTitle())).collect(Collectors.toList());
        }
        if (book.getAuthors().length() != 0) {
            books = books.stream().filter(b -> b.getAuthors().equals(book.getAuthors())).collect(Collectors.toList());
        }

        return books;
    }

    @Override
    public BookTo findBookbyId(Long id) {
        return BookMapper.map(bookRepository.findBookById(id));
    }

    @Override
    @Transactional
    public BookTo saveBook(BookTo book) {
        BookEntity entity = BookMapper.map(book);
        entity = bookRepository.save(entity);
        return BookMapper.map(entity);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}

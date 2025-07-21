package com.ruoyi.system.service;

import com.ruoyi.system.domain.Book;
import java.util.List;

public interface IBookService {
    List<Book> selectBookList(Book book);
    Book selectBookById(Long id);
    int insertBook(Book book);
    int updateBook(Book book);
    int deleteBookById(Long id);
}

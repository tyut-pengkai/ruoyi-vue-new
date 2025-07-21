package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Book;
import com.ruoyi.system.mapper.BookMapper;
import com.ruoyi.system.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookServiceImpl implements IBookService {
    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<Book> selectBookList(Book book) {
        return bookMapper.selectBookList(book);
    }

    @Override
    public Book selectBookById(Long id) {
        return bookMapper.selectBookById(id);
    }

    @Override
    public int insertBook(Book book) {
        return bookMapper.insertBook(book);
    }

    @Override
    public int updateBook(Book book) {
        return bookMapper.updateBook(book);
    }

    @Override
    public int deleteBookById(Long id) {
        return bookMapper.deleteBookById(id);
    }
}
package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Book;
import java.util.List;

public interface BookMapper {
    // 查询列表
    List<Book> selectBookList(Book book);

    // 根据ID查询
    Book selectBookById(Long id);

    // 新增
    int insertBook(Book book);

    // 修改
    int updateBook(Book book);

    // 删除
    int deleteBookById(Long id);
}
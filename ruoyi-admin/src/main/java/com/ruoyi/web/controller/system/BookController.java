package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.Book;
import com.ruoyi.system.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/book")
public class BookController extends BaseController {
    @Autowired
    private IBookService bookService;

    // 查询列表（支持分页）
    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(Book book) {
        startPage();  // 若依分页
        List<Book> list = bookService.selectBookList(book);
        return getDataTable(list);
    }

    // 查询详情
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(bookService.selectBookById(id));
    }

    // 新增
    @PostMapping
    public AjaxResult add(@RequestBody Book book) {
        return toAjax(bookService.insertBook(book));
    }

    // 修改
    @PutMapping
    public AjaxResult edit(@RequestBody Book book) {
        return toAjax(bookService.updateBook(book));
    }

    // 删除
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(bookService.deleteBookById(id));
    }
}

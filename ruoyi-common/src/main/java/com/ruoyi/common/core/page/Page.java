package com.ruoyi.common.core.page;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 鞋库通Page分页对象
 *
 * @author ruoyi
 */
//@NoArgsConstructor
//@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Page<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分页当前页码
     */
    private long pageNum;
    /**
     * 分页每页记录数
     */
    private long pageSize;
    /**
     *
     */
    private long pages;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> list;

    public Page() {
    }

    public Page(long pageNum, long pageSize, long pages, long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = pages;
        this.total = total;
        this.list = list;
    }

    public static <T> Page<T> convert(PageInfo<?> pageInfo) {
        return BeanUtil.toBean(pageInfo, Page.class);
    }

    public static <T> Page<T> convert(PageInfo<?> pageInfo, List<T> list) {
        Page<T> page = BeanUtil.toBean(pageInfo, Page.class);
        page.setList(CollectionUtils.isEmpty(list) ? new ArrayList<>() : list);
        return page;
    }

    public static <T> Page<T> convert(com.github.pagehelper.Page<T> srcPage) {
        Page<T> page = new Page<>();
        page.setTotal(srcPage.getTotal());
        page.setPageNum(srcPage.getPageNum());
        page.setPageSize(srcPage.getPageSize());
        page.setList(srcPage.getResult());
        return page;
    }


    public static <T> Page<T> empty(int pageSize, int pageNum) {
        Page<T> page = new Page<>();
        page.setList(new ArrayList<>());
        page.setTotal(0);
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        return page;
    }


}

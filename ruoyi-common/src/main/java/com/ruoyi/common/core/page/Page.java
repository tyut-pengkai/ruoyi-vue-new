package com.ruoyi.common.core.page;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.HttpStatus;
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
@NoArgsConstructor
@AllArgsConstructor
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

    /**
     * 消息状态码
     */
    private int code = HttpStatus.SUCCESS;

    /**
     * 消息内容
     */
    private String msg = "操作成功";

    public static <T> Page<T> convert(PageInfo<?> pageInfo, List<T> list) {
        Page<T> page = BeanUtil.toBean(pageInfo, Page.class);
        page.setList(CollectionUtils.isEmpty(list) ? new ArrayList<>() : list);
        return page;
    }

    public static <T> Page<T> empty() {
        Page<T> page = new Page<>();
        page.setList(new ArrayList<>());
        page.setTotal(0);
        page.setPageNum(1);
        page.setPageSize(10);
        return page;
    }


}

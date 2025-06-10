package com.ruoyi.common.core.page;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.ruoyi.common.core.domain.vo.BasePageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前页")
    private Long pageNum;

    @ApiModelProperty("每页记录数")
    private Long pageSize;

    @ApiModelProperty("总记录数")
    private Long total;

    @ApiModelProperty("结果集")
    private List<T> list;

    public static <T> PageVO<T> of(Page page, Class<T> clazz) {
        return new PageVO<>(page.getPageNum(), page.getPageSize(), page.getTotal(),
                BeanUtil.copyToList(page.getList(), clazz));
    }

    public static <T> PageVO<T> of(com.github.pagehelper.Page page, Class<T> clazz) {
        return new PageVO<>((long) page.getPageNum(), (long) page.getPageSize(), page.getTotal(),
                BeanUtil.copyToList(page.getResult(), clazz));
    }

    public static <T, E extends BasePageVO> PageVO<T> empty(E params) {
        return new PageVO((long) params.getPageNum(), (long) params.getPageSize(), 0L, ListUtil.empty());
    }

}

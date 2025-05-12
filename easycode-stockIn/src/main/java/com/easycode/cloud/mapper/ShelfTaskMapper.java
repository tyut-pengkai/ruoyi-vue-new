package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.ShelfTask;
import com.easycode.cloud.domain.dto.ShelfTaskDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 上架任务Mapper接口
 * 
 * @author zhanglei
 * @date 2023-04-12
 */
@Repository
public interface ShelfTaskMapper 
{
    /**
     * 查询上架任务
     * 
     * @param id 上架任务主键
     * @return 上架任务
     */
    public ShelfTask selectShelfTaskById(Long id);

    /**
     * 查询上架任务列表
     * 
     * @param shelfTask 上架任务
     * @return 上架任务集合
     */
    public List<ShelfTask> selectShelfTaskList(ShelfTask shelfTask);



    /**
     * 新增上架任务
     * 
     * @param shelfTask 上架任务
     * @return 结果
     */
    public int insertShelfTask(ShelfTask shelfTask);

    /**
     * 修改上架任务
     * 
     * @param shelfTask 上架任务
     * @return 结果
     */
    public int updateShelfTask(ShelfTask shelfTask);

    /**
     * 删除上架任务
     * 
     * @param id 上架任务主键
     * @return 结果
     */
    public int deleteShelfTaskById(Long id);

    /**
     * 批量删除上架任务
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteShelfTaskByIds(Long[] ids);

    /**
     * pda端查询上架任务
     * @param shelfTaskDto 上架任务
     * @return 上架任务集合
     */
    List<ShelfTask> pdaShelfTaskList(ShelfTaskDto shelfTaskDto);

    /**
     * 批量新增上架任务
     * @param shelfTaskList
     * @return
     */
    int insertShelfTaskList(@Param("shelfTaskList") List<ShelfTask> shelfTaskList);
}

package com.easycode.cloud.service;

import com.alibaba.fastjson.JSONObject;
import com.easycode.cloud.domain.ShelfTask;
import com.easycode.cloud.domain.dto.ShelfTaskDto;

import java.util.List;

/**
 * 上架任务Service接口
 * 
 * @author zhanglei
 * @date 2023-04-12
 */
public interface IShelfTaskService 
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
    public ShelfTask insertShelfTask(ShelfTask shelfTask);

    /**
     * 新增上架任务
     *
     * @param shelfTaskList 上架任务
     * @return 结果
     */
    public int insertShelfTaskList(List<ShelfTask> shelfTaskList);

    /**
     * 修改上架任务
     * 
     * @param shelfTask 上架任务
     * @return 结果
     */
    public int updateShelfTask(ShelfTask shelfTask);

    /**
     * 批量删除上架任务
     * 
     * @param ids 需要删除的上架任务主键集合
     * @return 结果
     */
    public int deleteShelfTaskByIds(Long[] ids);

    /**
     * 删除上架任务信息
     * 
     * @param id 上架任务主键
     * @return 结果
     */
    public int deleteShelfTaskById(Long id);

    /**
     * 关闭上架任务
     */
    public void closeShelfTask(Long[] ids);

    /**
     * pda端查询上架任务
     * @param shelfTaskDto 上架任务
     * @return 上架任务集合
     */
    List<ShelfTask> selectPdaShelfTaskList(ShelfTaskDto shelfTaskDto);

    /**
     * pda上架任务提交
     * @param shelfTaskDto 上架任务
     * @return 结果
     */
    int submit(ShelfTaskDto shelfTaskDto);

    /**
     * 根据任务单号查询任务
     * @param taskNo
     * @return
     */
    ShelfTask selectShelfTaskByTaskNo(String taskNo);

    /**
     * 打印
     * @param locationCode
     * @param id
     */
    JSONObject print(String locationCode, Long id);

    /**
     * 打印列表
     * @param locationCode
     * @param ids
     */
    void printList(String locationCode, List<Long> ids);

    /**
     * 更新打印次数
     *
     */
    void updateShelfTaskPrintSum(ShelfTask shelfTask);
}

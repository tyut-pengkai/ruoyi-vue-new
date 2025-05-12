package com.easycode.cloud.service;

import com.easycode.cloud.domain.RetTask;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.domain.vo.RetTaskVo;
import com.weifu.cloud.domain.vo.TaskInfoVo;

import java.util.List;


/**
 * 退货任务Service接口
 *
 * @author zhanglei
 * @date 2023-03-13
 */
public interface IRetTaskService
{
    /**
     * 查询退货任务
     *
     * @param id 退货任务主键
     * @return 退货任务
     */
    public RetTask selectRetTaskById(Long id);

    /**
     * 查询退货任务列表
     *
     * @param retTask 退货任务
     * @return 退货任务集合
     */
    public List<RetTask> selectRetTaskList(RetTask retTask);

    /**
     * 新增退货任务
     *
     * @param retTask 退货任务
     * @return 结果
     */
    public int insertRetTask(RetTask retTask);

    /**
     * 修改退货任务
     *
     * @param retTask 退货任务
     * @return 结果
     */
    public int updateRetTask(RetTask retTask);

    /**
     * 批量删除退货任务
     *
     * @param ids 需要删除的退货任务主键集合
     * @return 结果
     */
    public int deleteRetTaskByIds(Long[] ids);

    /**
     * 删除退货任务信息
     *
     * @param id 退货任务主键
     * @return 结果
     */
    public int deleteRetTaskById(Long id);

    /**
     * pda端查询退货任务
     * @param retTaskDto 退货任务
     * @return 退货任务集合
     */
    List<RetTask>selectPdaRetTaskList(RetTaskDto retTaskDto);

    /**
     * pda退货任务提交
     * @param retTaskDto 退货任务
     * @return 结果
     */
    int submit(RetTaskDto retTaskDto) throws Exception;

    /**
     * 根据任务表中的单据明细id获取对应操作单位及操作单位数量
     * @param retTaskDto 退货任务
     * @return 结果
     */
    RetTaskDto getOperationInfo(RetTaskDto retTaskDto) throws Exception;

    /**
     * 关闭任务
     */
    int closeTask(Long[] ids);

    /**
     * 退货任务查询
     * @param id 退货任务id
     * @return 返回退货任务+物料类型
     */
    RetTaskVo getRetTaskByMaterialNoType(Long id);
}

package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.RetTask;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.domain.vo.RetTaskVo;
import com.weifu.cloud.domain.vo.TaskInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 退货任务Mapper接口
 *
 * @author zhanglei
 * @date 2023-03-13
 */
@Repository
public interface RetTaskMapper
{
    /**
     * 查询退货任务
     *
     * @param id 退货任务主键
     * @return 退货任务
     */
    public RetTaskVo selectRetTaskById(Long id);

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
     * 根据detailId与任务类型修改任务状态
     *
     * @param retTask 退货任务
     * @return 结果
     */
    public int updateRetTaskByDetailId(RetTask retTask);

    /**
     * 通过明细di列表批量修改退货任务状态
     *
     * @param detailIdList 退货任务
     * @return 结果
     */
    public int updateStatusByDetailList(List<Long> detailIdList);

    /**
     * 删除退货任务
     *
     * @param id 退货任务主键
     * @return 结果
     */
    public int deleteRetTaskById(Long id);

    /**
     * 批量删除退货任务
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRetTaskByIds(Long[] ids);

    /**
     * 删除退货任务信息
     *
     * @param stockinOrderNo 单据号
     * @return 结果
     */
    int deleteByStockinOrderNo(String stockinOrderNo);

    /**
     * 修改退货状态
     *
     * @param retTask 退货任务
     * @return 结果
     */
    public int updateRetTaskStatus(RetTask retTask);

    /**
     * pda退货任务查询
     * @param retTaskDto 退货任务
     * @return 结果
     */
    public List<RetTask> pdaRetTaskList(RetTaskDto retTaskDto);

    void updatePrinterStatus(TaskInfoVo taskInfoVo);

    List<RetTaskVo> costCenterTaskList(RetTaskDto retTaskDto);

    RetTaskVo selectCostCenterTaskDetail(Long id);

    List<RetTask> haveCenterReturnTask(List<String> stockInTaskNos);
}

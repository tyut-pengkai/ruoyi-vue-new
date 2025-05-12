package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.TaskInfo;
import com.easycode.cloud.domain.dto.TaskInfoDto;
import com.weifu.cloud.domain.StockInStdOrderDetail;
import com.weifu.cloud.domain.vo.TaskInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 仓管任务Mapper接口
 *
 * @author weifu
 * @date 2022-12-12
 */
@Repository
public interface TaskInfoMapper {
    /**
     * 查询仓管任务
     *
     * @param id 仓管任务主键
     * @return 仓管任务
     */
    TaskInfo selectTaskInfoById(Long id);

    /**
     * 查询仓管任务
     *
     * @param id 仓管任务主键
     * @return 仓管任务
     */
    TaskInfo selectTaskInfoVById(TaskInfoVo taskInfoVo);

    /**
     * 查询仓管任务列表
     *
     * @param taskInfo 仓管任务
     * @return 仓管任务集合
     */
    List<TaskInfoVo> selectTaskInfoList(TaskInfoVo taskInfo);


    /**
     * 查询板块任务列表
     * @param taskInfo
     * @return
     */
    List<TaskInfoVo> selectBoardTaskList(TaskInfoVo taskInfo);
    /**
     * 新增仓管任务
     *
     * @param taskInfo 仓管任务
     * @return 结果
     */
    int insertTaskInfo(TaskInfo taskInfo);

    /**
     * 修改仓管任务
     *
     * @param taskInfo 仓管任务
     * @return 结果
     */
    int updateTaskInfo(TaskInfo taskInfo);

    /**
     * 删除仓管任务
     *
     * @param id 仓管任务主键
     * @return 结果
     */
    int deleteTaskInfoById(Long id);

    /**
     * 批量删除仓管任务
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTaskInfoByIds(Long[] ids);

    /**
     * 查询成品收货列表
     *
     * @param taskInfoDto
     * @return
     */
    List<TaskInfoVo> selectTaskInfoFinList(TaskInfoDto taskInfoDto);

    /**
     * 批量添加任务
     *
     * @param taskInfoList
     * @return
     */
    int insertTaskInfoList(List<TaskInfo> taskInfoList);

    /**
     * 批量添加任务 token无tenantId
     *
     * @param taskInfoList
     * @return
     */
    int insertTaskInfoListNoTenantId(List<TaskInfo> taskInfoList);

    /**
     * 获取成品收货任务详细信息(关联成品收货明细)
     *
     * @param id
     * @return
     */
    List<TaskInfoVo> selectTaskFinInfoById(Long id);

    /**
     * 查询标准入库任务列表
     *
     * @param taskInfoVo
     * @return
     */
    List<TaskInfoVo> selectTaskInfoListByStd(TaskInfoVo taskInfoVo);

    /**
     * 查询标准入库任务入库明细
     *
     * @param taskInfoVo
     * @return
     */
    TaskInfoVo getStdOrderTaskDetail(TaskInfoVo taskInfoVo);

//    /**
//     * 查询标准入库任务入库明细
//     *
//     * @param id
//     * @return
//     */
//    TaskInfoVo getStdOrderTaskDetailByAsnNo(String id);

    /**
     * 查询成品收货单详细信息
     *
     * @param id
     * @return
     */
    TaskInfoVo selectFinDetailById(Long id);

    /**
     * 查询成品收货单详情列表
     *
     * @param taskInfo
     * @return
     */
    List<TaskInfoVo> selectFinDetailListList(TaskInfo taskInfo);

    /**
     * 关闭任务
     *
     * @return
     */
    int closeTaskInfoByNo(TaskInfo taskInfo);


    /**
     * 未关闭任务
     *
     * @param taskInfo
     * @return
     */
    List<TaskInfo> openTask(TaskInfo taskInfo);

    /**
     * 获取收货任务
     *
     * @return
     */
    List<TaskInfoVo> getTaskInfoListByOrderNo(StockInStdOrderDetail stockInStdOrderDetail);

    /**
     * 获取收货任务
     *
     * @return
     */
    List<TaskInfoVo> getTaskInfoListByDetailId(StockInStdOrderDetail stockInStdOrderDetail);

    /**
     * 根据入库单据号删除入库任务
     *
     * @param stockInOrderNo 入库单据号
     * @param detailIds      对应的明细ids
     * @return 结果
     */
    int deleteTaskInfoByStockInOrderNo(@Param("stockInOrderNo") String stockInOrderNo, @Param("detailIds") Long[] detailIds);

    /**
     * 查询半成品收货任务列表
     * @param taskInfoDto 入库任务dto
     * @return 入库任务vo集合
     */
    List<TaskInfoVo> getSemiFinTaskList(TaskInfoDto taskInfoDto);

    /**
     * 获取半成品收货任务(pda)
     * @param id 半成品收货任务id
     * @return 入库任务vo
     */
    TaskInfoVo getSemiFinTaskById(Long id);

    /**
     * 查询部门与库存地点关系
     * @param locationCode 库存地点
     * @return 部门id集合
     */

    List<String> getDeptStorageLocation(String locationCode);

    TaskInfo selectTaskInfoByDetailId(TaskInfo taskInfo);

    void updatePrinterStatus(TaskInfoVo taskInfoVo);

    /**
     * 根据任务类型获取任务列表
     */
    List<TaskInfoVo> getTaskListByTaskType(TaskInfoDto taskInfoDto);

    /**
     * 根据ID获取任务详情
     * @param id
     * @return
     */
    TaskInfoVo getTaskById(Long id);

    /**
     * 根据任务号获取数据
     * @param taskNo
     * @return
     */
    TaskInfo getTaskInfoListByTaskNo(String taskNo);

    /**
     * 查询标准入库任务列表
     *
     * @param taskInfoVo
     * @return
     */
    List<TaskInfoVo> selectStdTaskInfoList(TaskInfoVo taskInfoVo);

    /**
     * 交货单出库任务删除
     * @param taskInfo
     * @return
     */
    int deleteByOrderNoAndType(TaskInfo taskInfo);
}

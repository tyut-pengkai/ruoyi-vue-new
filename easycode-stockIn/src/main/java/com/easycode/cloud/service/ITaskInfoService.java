package com.easycode.cloud.service;

import com.alibaba.fastjson.JSONObject;
import com.easycode.cloud.domain.TaskInfo;
import com.easycode.cloud.domain.WmsKbAndSwTaskLog;
import com.easycode.cloud.domain.dto.TaskInfoDto;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.domain.StockInStdOrderDetail;
import com.weifu.cloud.domain.dto.StockInStdOrderDetailDto;
import com.weifu.cloud.domain.vo.TaskInfoVo;
import com.weifu.cloud.domian.dto.PrintTOParamsDto;

import java.util.List;

/**
 * 仓管任务Service接口
 *
 * @author weifu
 * @date 2022-12-12
 */
public interface ITaskInfoService {
    /**
     * 查询仓管任务
     *
     * @param id 仓管任务主键
     * @return 仓管任务
     */
    TaskInfo selectTaskInfoById(Long id);

    /**
     * 查询仓管任务列表
     *
     * @param taskInfo 仓管任务
     * @return 仓管任务集合
     */
    List<TaskInfoVo> selectTaskInfoList(TaskInfoVo taskInfo);

    /**
     * 查询标准入库任务列表
     *
     * @param taskInfo 仓管任务
     * @return 仓管任务集合
     */
    List<TaskInfoVo> selectStdTaskInfoList(TaskInfoVo taskInfo);

    /**
     * 更改打印状态和次数
     *
     * @param taskInfoVo 更改打印状态和次数
     * @return 更改打印状态和次数
     */
    void updatePrinterStatus(TaskInfoVo taskInfoVo);

    /**
     * 查询看板任务列表
     * @param taskInfo
     * @return
     */
    List<TaskInfoVo> listBoardTask(TaskInfoVo taskInfo);

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
    int updateTaskInfo(TaskInfo taskInfo) throws Exception;

    /**
     * 修改看板领料任务
     *
     * @param taskInfo 仓管任务
     * @return 结果
     */
    int updateBoardTaskInfo(TaskInfo taskInfo);
    /**
     * 批量删除仓管任务
     *
     * @param ids 需要删除的仓管任务主键集合
     * @return 结果
     */
    int deleteTaskInfoByIds(Long[] ids);

    /**
     * 删除仓管任务信息
     *
     * @param id 仓管任务主键
     * @return 结果
     */
    int deleteTaskInfoById(Long id);

    /**
     * 查询成品收货列表
     * @param taskInfoDto
     * @return
     */
    List<TaskInfoVo> selectTaskInfoFinList(TaskInfoDto taskInfoDto);

    /**
     * 获取成品收货任务详细信息(关联成品收货明细)
     * @param id
     * @return
     */
    List<TaskInfoVo> selectTaskFinInfoById(Long id);

    /**
     * 查询标准入库任务列表
     * @param taskInfoVo
     * @return
     */
    List<TaskInfoVo> selectTaskInfoListByStd(TaskInfoVo taskInfoVo);

    /**
     * 查询标准入库任务入库明细
     * @param taskInfoVo
     * @return
     */
    TaskInfoVo getStdOrderTaskDetail(TaskInfoVo taskInfoVo);

    /**
     * 成品收货任务提交
     */
    int submitFinOrderTask(TaskInfoDto taskInfoDto) throws Exception;

    /**
     * 获取成品收货单详细信息
     */
    TaskInfoVo selectFinDetailById(Long id);

    /**
     * 查询成品收货单详情列表
     */
    List<TaskInfoVo> selectFinDetailListList(TaskInfo taskInfo);

    /**
     * 获取标准收货任务
     */
    List<TaskInfoVo> getTaskInfoListByOrderNo(StockInStdOrderDetail stockInStdOrderDetail);

    /**
     * 从mom获取容器信息
     * @param containerNo 容器号
     * @return
     */
    TaskInfoDto mesToWmsContainer(String containerNo);

    /**
     * 成品收货提交
     *
     * @return
     */
    int submitByMom(TaskInfoDto taskInfoDto) throws Exception;

    /**
     * 同步激活成品收货单、明细、任务(pda进入成品收货页面后触发)
     * @param id 任务id
     * @return 结果
     */
    int activeFin(Long id);

    /**
     * 查询半成品收货任务列表
     * @param taskInfoDto 入库任务dto
     * @return 入库任务vo集合
     */
    List<TaskInfoVo> getSemiFinTaskList(TaskInfoDto taskInfoDto);

    /**
     * 同步激活半成品收货单(pda进入半成品收货页面后触发)
     * @param id 任务id
     * @return 结果
     */
    int activeSemiFinTask(Long id);

    /**
     * 获取半成品收货任务(pda)
     * @param id 半成品收货任务id
     * @return 入库任务vo
     */
    TaskInfoVo getSemiFinTaskById(Long id);

    /**
     * 半成品收货任务提交(pda)
     * @param taskInfoDto 入库任务dto
     * @return 结果
     */
    int submitSemiFinOrderTask(TaskInfoDto taskInfoDto) throws Exception;

    /**
     * 提交ASN任务(pda)
     * @param taskInfoDto 入库任务dto
     * @return 结果
     */
    int submitStdAsnOrderTask(TaskInfoDto taskInfoDto) throws Exception;

    /**
     * 根据任务类型获取任务列表
     */
    List<TaskInfoVo> getTaskListByTaskType(TaskInfoDto taskInfoDto);

    /**
     * 成品/半成品出库任务确认
     */
    int submitTask(TaskInfoDto taskInfoDto) throws Exception;

    /**
     * 根据ID获取任务详情
     * @param id
     * @return
     */
    TaskInfoVo getTaskById(Long id) throws Exception;


    TaskInfo getTaskInfoListByTastNo(String taskNo);


    /**
     * 新增水位日志
     * @param wmsKbAndSwTaskLog
     * @return
     */
    int addKbAndSwTaskLog(WmsKbAndSwTaskLog wmsKbAndSwTaskLog);

    /**
     * 交货单出库任务删除
     * @param taskInfo
     * @return
     */
    int deleteByOrderNoAndType(TaskInfo taskInfo);


    TaskInfo selectTaskInfoVById(TaskInfoVo taskInfoVo);

    int updateTaskInfoById(TaskInfo taskInfo);

    JSONObject printInfo(TaskInfoVo taskInfo);

    void reSendInspectOrder(StockInStdOrderDetailDto stockInStdOrderDetailDto) throws Exception;

    /**
     * 多选打印
     * @param taskInfo
     * @return
     */
    List<JSONObject> printInfoAll(TaskInfoVo taskInfo);
}

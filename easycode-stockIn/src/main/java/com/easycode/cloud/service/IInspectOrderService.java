package com.easycode.cloud.service;

import com.alibaba.fastjson.JSONObject;
import com.easycode.cloud.domain.InspectOrder;
import com.easycode.cloud.domain.dto.InspectOrderDto;
import com.easycode.cloud.domain.vo.InspectOrderVo;
import com.easycode.cloud.domain.vo.StockInDetailPrintVo;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.domain.dto.SendInspectResultDto;
import com.weifu.cloud.domain.dto.SendSealedResultIQCDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 送检单Service接口
 *
 * @author weifu
 * @date 2023-03-29
 */
public interface IInspectOrderService
{
    /**
     * 查询送检单
     *
     * @param id 送检单主键
     * @return 送检单
     */
    public InspectOrder selectInspectOrderById(Long id);

    /**
     * 查询送检单列表
     *
     * @param inspectOrder 送检单
     * @return 送检单集合
     */
    public List<InspectOrderVo> selectInspectOrderList(InspectOrderVo inspectOrder);

    public StockInDetailPrintVo selectInspectOrderByTaskNo(String taskNo);

    /**
     * 新增送检单
     *
     * @param inspectOrder 送检单
     * @return 结果
     */
    public int insertInspectOrder(InspectOrder inspectOrder);

    /**
     * 修改送检单
     *
     * @param inspectOrderDto 送检单dto
     * @return 结果
     */
    public int updateInspectOrder(InspectOrderDto inspectOrderDto);

    /**
     * 批量删除送检单
     *
     * @param ids 需要删除的送检单主键集合
     * @return 结果
     */
    public int deleteInspectOrderByIds(Long[] ids);

    /**
     * 上传附件
     *
     * @param file
     * @return 结果
     */
    public int upload(MultipartFile file);

    /**
     * 删除送检单信息
     *
     * @param id 送检单主键
     * @return 结果
     */
    public int deleteInspectOrderById(Long id);


    /**
     * 发送检验结果
     *
     * @param  detailDto
     * @return 结果
     */
    int sendInspectResult(SendInspectResultDto detailDto) throws Exception;


    /**
     * 后处理结果IQC
     *
     * @param  resultIQCDto
     * @return 结果
     */
    int sendSealedResultIQC(SendSealedResultIQCDto resultIQCDto) throws Exception;

    /**
     * 生成质检任务
     * @param inspectOrderDto
     * @return
     */
    int generateInspectTask(InspectOrderDto inspectOrderDto) throws Exception;


    /**
     * 取消质检任务
     * @param taskCode
     * @return
     */
    int cancelInspectTask(String taskCode);


    /**
     * 物料反冲销
     * @param jsonObject
     * @return
     */
    int materialReversal(JSONObject jsonObject);


    /**
     * 打印检验单
     * @param inspectOrderDto
     * @return
     */
    int printInspectTask(InspectOrderDto inspectOrderDto);

    /**
     * 手动批量释放--- 挑选后处理使用

     * @param inspectOrderDto
     * @return
     */
    AjaxResult releaseManual(InspectOrderDto inspectOrderDto);
}

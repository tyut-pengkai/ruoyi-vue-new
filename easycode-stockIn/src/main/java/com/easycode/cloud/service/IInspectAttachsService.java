package com.easycode.cloud.service;

import com.easycode.cloud.domain.InspectAttachs;
import com.easycode.cloud.domain.vo.InspectAttachsVo;

import java.util.List;

/**
 * 送检单附件Service接口
 *
 * @author fangshucheng
 * @date 2023-03-31
 */
public interface IInspectAttachsService
{
    /**
     * 查询送检单附件
     *
     * @param id 送检单附件主键
     * @return 送检单附件
     */
    public InspectAttachs selectInspectAttachsById(Long id);

    /**
     * 查询送检单附件列表
     *
     * @param inspectAttachs 送检单附件
     * @return 送检单附件集合
     */
    public List<InspectAttachs> selectInspectAttachsList(InspectAttachs inspectAttachs);

    /**
     * 新增送检单附件
     *
     * @param inspectAttachs 送检单附件
     * @return 结果
     */
    public int insertInspectAttachs(InspectAttachs inspectAttachs);

    /**
     * 批次新增送检单附件
     *
     * @param inspectAttachsVo 送检单附件
     * @return 结果
     */
    public int batchInsertInspectAttachs(InspectAttachsVo inspectAttachsVo);

    /**
     * 修改送检单附件
     *
     * @param inspectAttachs 送检单附件
     * @return 结果
     */
    public int updateInspectAttachs(InspectAttachs inspectAttachs);

    /**
     * 批量删除送检单附件
     *
     * @param ids 需要删除的送检单附件主键集合
     * @return 结果
     */
    public int deleteInspectAttachsByIds(Long[] ids);

    /**
     * 删除送检单附件信息
     *
     * @param id 送检单附件主键
     * @return 结果
     */
    public int deleteInspectAttachsById(Long id);
}

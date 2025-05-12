package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.InspectAttachs;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 送检单附件Mapper接口
 *
 * @author fangshucheng
 * @date 2023-03-31
 */
@Repository
public interface InspectAttachsMapper
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
     * 修改送检单附件
     *
     * @param inspectAttachs 送检单附件
     * @return 结果
     */
    public int updateInspectAttachs(InspectAttachs inspectAttachs);

    /**
     * 删除送检单附件
     *
     * @param id 送检单附件主键
     * @return 结果
     */
    public int deleteInspectAttachsById(Long id);

    /**
     * 批量删除送检单附件
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInspectAttachsByIds(Long[] ids);
}


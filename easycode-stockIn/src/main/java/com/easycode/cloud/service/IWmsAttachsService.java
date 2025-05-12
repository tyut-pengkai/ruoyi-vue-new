package com.easycode.cloud.service;

import com.easycode.cloud.domain.WmsAttachs;
import com.easycode.cloud.domain.vo.WmsAttachsVo;

import java.util.List;

/**
 * 送货单附件Service接口
 *
 * @author fsc
 * @date 2023-05-18
 */
public interface IWmsAttachsService
{
    /**
     * 查询送货单附件
     *
     * @param id 送货单附件主键
     * @return 送货单附件
     */
    public WmsAttachs selectWmsAttachsById(Long id);

    /**
     * 查询送货单附件列表
     *
     * @param wmsAttachs 送货单附件
     * @return 送货单附件集合
     */
    public List<WmsAttachs> selectWmsAttachsList(WmsAttachs wmsAttachs);

    /**
     * 新增送货单附件
     *
     * @param wmsAttachs 送货单附件
     * @return 结果
     */
    public int insertWmsAttachs(WmsAttachs wmsAttachs);

    /**
     * 修改送货单附件
     *
     * @param wmsAttachs 送货单附件
     * @return 结果
     */
    public int updateWmsAttachs(WmsAttachs wmsAttachs);

    /**
     * 批量删除送货单附件
     *
     * @param ids 需要删除的送货单附件主键集合
     * @return 结果
     */
    public int deleteWmsAttachsByIds(Long[] ids);

    /**
     * 批量上传
     *
     * @param wmsAttachsVo 附件上传Vo
     * @return 结果
     */
    public int batchInsertAttachs(WmsAttachsVo wmsAttachsVo);

    /**
     * 删除送货单附件信息
     *
     * @param id 送货单附件主键
     * @return 结果
     */
    public int deleteWmsAttachsById(Long id);

    /**
     * 删除送货单附件信息
     *
     * @param id 送货单附件主键
     * @return 结果
     */
    public int deleteWmsAttachsByDetailId(Long id);
}

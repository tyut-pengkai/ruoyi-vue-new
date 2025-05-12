package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.WmsAttachs;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 送货单附件Mapper接口
 *
 * @author fsc
 * @date 2023-05-18
 */
@Repository
public interface WmsAttachsMapper
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
     * 删除送货单附件
     *
     * @param id 送货单附件主键
     * @return 结果
     */
    public int deleteWmsAttachsById(Long id);


    /**
     * 删除送货单附件
     *
     * @param id 送货单附件主键
     * @return 结果
     */
    public int deleteWmsAttachsByDetailId(Long id);

    /**
     * 批量删除送货单附件
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsAttachsByIds(Long[] ids);
}

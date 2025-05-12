package com.easycode.cloud.service.impl;

import com.easycode.cloud.domain.vo.WmsAttachsVo;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.easycode.cloud.domain.WmsAttachs;
import com.weifu.cloud.domain.vo.FileVo;
import com.easycode.cloud.mapper.WmsAttachsMapper;
import com.easycode.cloud.service.IWmsAttachsService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 送货单附件Service业务层处理
 *
 * @author fsc
 * @date 2023-05-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WmsAttachsServiceImpl implements IWmsAttachsService
{
    @Autowired
    private WmsAttachsMapper wmsAttachsMapper;

    /**
     * 查询送货单附件
     *
     * @param id 送货单附件主键
     * @return 送货单附件
     */
    @Override
    public WmsAttachs selectWmsAttachsById(Long id)
    {
        return wmsAttachsMapper.selectWmsAttachsById(id);
    }

    /**
     * 查询送货单附件列表
     *
     * @param wmsAttachs 送货单附件
     * @return 送货单附件
     */
    @Override
    public List<WmsAttachs> selectWmsAttachsList(WmsAttachs wmsAttachs)
    {
        return wmsAttachsMapper.selectWmsAttachsList(wmsAttachs);
    }

    /**
     * 新增送货单附件
     *
     * @param wmsAttachs 送货单附件
     * @return 结果
     */
    @Override
    public int insertWmsAttachs(WmsAttachs wmsAttachs)
    {
        wmsAttachs.setCreateTime(DateUtils.getNowDate());
        return wmsAttachsMapper.insertWmsAttachs(wmsAttachs);
    }

    /**
     * 修改送货单附件
     *
     * @param wmsAttachs 送货单附件
     * @return 结果
     */
    @Override
    public int updateWmsAttachs(WmsAttachs wmsAttachs)
    {
        wmsAttachs.setUpdateTime(DateUtils.getNowDate());
        return wmsAttachsMapper.updateWmsAttachs(wmsAttachs);
    }

    /**
     * 批量删除送货单附件
     *
     * @param ids 需要删除的送货单附件主键
     * @return 结果
     */
    @Override
    public int deleteWmsAttachsByIds(Long[] ids)
    {
        return wmsAttachsMapper.deleteWmsAttachsByIds(ids);
    }

    /**
     * 批量上传附件
     *
     * @param wmsAttachsVo 附件上传Vo
     * @return 结果
     */
    @Override
    public int batchInsertAttachs(WmsAttachsVo wmsAttachsVo)
    {
        WmsAttachs inspectAttachs = null;
        List<FileVo> fileList = Optional.ofNullable(wmsAttachsVo.getFileList()).orElse(new ArrayList<FileVo>());
        for (FileVo fileDto : fileList){
            inspectAttachs = new WmsAttachs();
            inspectAttachs.setOrderId(wmsAttachsVo.getId());
            inspectAttachs.setName(fileDto.getRealName());
            inspectAttachs.setUrl(fileDto.getUrl());
            inspectAttachs.setStatus("0");
            inspectAttachs.setCreateTime(DateUtils.getNowDate());
            inspectAttachs.setCreateBy(SecurityUtils.getUsername());
            wmsAttachsMapper.insertWmsAttachs(inspectAttachs);
        }
        return HttpStatus.SC_OK;
    }

    /**
     * 删除送货单附件信息
     *
     * @param id 送货单附件主键
     * @return 结果
     */
    @Override
    public int deleteWmsAttachsById(Long id)
    {
        return wmsAttachsMapper.deleteWmsAttachsById(id);
    }

    @Override
    public int deleteWmsAttachsByDetailId(Long id)
    {
        return wmsAttachsMapper.deleteWmsAttachsByDetailId(id);
    }
}

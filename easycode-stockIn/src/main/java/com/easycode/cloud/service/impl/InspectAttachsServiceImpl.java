package com.easycode.cloud.service.impl;

import com.easycode.cloud.domain.vo.InspectAttachsVo;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.easycode.cloud.domain.InspectAttachs;
import com.weifu.cloud.domain.dto.FileDto;
import com.easycode.cloud.mapper.InspectAttachsMapper;
import com.easycode.cloud.service.IInspectAttachsService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 送检单附件Service业务层处理
 *
 * @author fangshucheng
 * @date 2023-03-31
 */
@Service
public class InspectAttachsServiceImpl implements IInspectAttachsService
{
    @Autowired
    private InspectAttachsMapper inspectAttachsMapper;

    /**
     * 查询送检单附件
     *
     * @param id 送检单附件主键
     * @return 送检单附件
     */
    @Override
    public InspectAttachs selectInspectAttachsById(Long id)
    {
        return inspectAttachsMapper.selectInspectAttachsById(id);
    }

    /**
     * 查询送检单附件列表
     *
     * @param inspectAttachs 送检单附件
     * @return 送检单附件
     */
    @Override
    public List<InspectAttachs> selectInspectAttachsList(InspectAttachs inspectAttachs)
    {
        return inspectAttachsMapper.selectInspectAttachsList(inspectAttachs);
    }

    /**
     * 新增送检单附件
     *
     * @param inspectAttachs 送检单附件
     * @return 结果
     */
    @Override
    public int insertInspectAttachs(InspectAttachs inspectAttachs)
    {
        inspectAttachs.setCreateTime(DateUtils.getNowDate());
        return inspectAttachsMapper.insertInspectAttachs(inspectAttachs);
    }

    /**
     * 批次新增送检单附件
     *
     * @param inspectAttachsVo 送检单附件
     * @return 结果
     */
    @Override
    public int batchInsertInspectAttachs(InspectAttachsVo inspectAttachsVo)
    {

        InspectAttachs inspectAttachs = null;
        List<FileDto> fileList = inspectAttachsVo.getFileList();
        for (FileDto fileDto : fileList){
            inspectAttachs = new InspectAttachs();
            inspectAttachs.setInspectOrderId(inspectAttachsVo.getId());
            inspectAttachs.setName(fileDto.getRealName());
            inspectAttachs.setUrl(fileDto.getUrl());
            inspectAttachs.setStatus("0");
            inspectAttachs.setCreateTime(DateUtils.getNowDate());
            inspectAttachs.setCreateBy(SecurityUtils.getUsername());
            inspectAttachsMapper.insertInspectAttachs(inspectAttachs);
        }
        return HttpStatus.SC_OK;
    }

    /**
     * 修改送检单附件
     *
     * @param inspectAttachs 送检单附件
     * @return 结果
     */
    @Override
    public int updateInspectAttachs(InspectAttachs inspectAttachs)
    {
        inspectAttachs.setUpdateTime(DateUtils.getNowDate());
        return inspectAttachsMapper.updateInspectAttachs(inspectAttachs);
    }

    /**
     * 批量删除送检单附件
     *
     * @param ids 需要删除的送检单附件主键
     * @return 结果
     */
    @Override
    public int deleteInspectAttachsByIds(Long[] ids)
    {
        return inspectAttachsMapper.deleteInspectAttachsByIds(ids);
    }

    /**
     * 删除送检单附件信息
     *
     * @param id 送检单附件主键
     * @return 结果
     */
    @Override
    public int deleteInspectAttachsById(Long id)
    {
        return inspectAttachsMapper.deleteInspectAttachsById(id);
    }
}

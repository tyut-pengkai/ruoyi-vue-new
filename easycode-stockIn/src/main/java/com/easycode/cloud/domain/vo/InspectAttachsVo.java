package com.easycode.cloud.domain.vo;

import com.weifu.cloud.common.core.web.domain.BaseEntity;
import com.weifu.cloud.domain.dto.FileDto;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 送检单附件对象 wms_inspect_attachs
 *
 * @author fangshucheng
 * @date 2023-03-31
 */
@Alias("InspectAttachsVo")
public class InspectAttachsVo extends BaseEntity
{
    /** 主键 */
    private Long id;

    List<FileDto> fileList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<FileDto> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileDto> fileList) {
        this.fileList = fileList;
    }
}

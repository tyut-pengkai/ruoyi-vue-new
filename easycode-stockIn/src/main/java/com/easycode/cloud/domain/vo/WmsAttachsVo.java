package com.easycode.cloud.domain.vo;

import com.easycode.cloud.domain.WmsAttachs;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 送货单附件对象 wms_attachs
 *
 * @author fsc
 * @date 2023-05-18
 */
@Alias("WmsAttachsVo")
public class WmsAttachsVo extends WmsAttachs
{
    private static final long serialVersionUID = 1L;

    List<FileVo> fileList;

    public List<FileVo> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileVo> fileList) {
        this.fileList = fileList;
    }
}

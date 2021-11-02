package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 软件管理表
 *
 * @author zwgu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysAppVer extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    @Excel(name = "参数主键", cellType = Excel.ColumnType.NUMERIC)
    private Long appVerId;
    /**
     * 所属软件ID
     */
    private Long appId;
    /**
     * 版本名，如v1.0.0
     */
    private String verName;
    /**
     * 版本号，如20201031010001
     */
    private Long verNo;
    /**
     * 更新日志
     */
    private String updateLog;
    /**
     * 新版本下载地址
     */
    private String downloadUrl;
    /**
     * 软件自身的md5
     */
    private String md5;
}

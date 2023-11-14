package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用户数量统计 sys_app_user_count
 *
 * @author zwgu
 * @date 2023-11-14
 */
@Data
public class SysAppUserCount {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 软件ID
     */
    private Long appId;

    /**
     * 额度
     */
    private Long totalNum;
    private Long loginNum;
    private Long vipNum;
    private Long maxOnlineNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}

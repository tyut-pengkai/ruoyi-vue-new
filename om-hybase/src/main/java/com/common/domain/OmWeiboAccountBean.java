package com.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author TRS
 * @date 2023-11-30
 */
@ToString
@Getter
@Setter
public class OmWeiboAccountBean extends HybaseBean implements Serializable {

    private String sid;
    // uid
    private Long uid;
    // 用户名
    private String irScreenName;
    // 描述
    private String irDescription;
    // 性别
    private String gender;
    // 好友数/关注数
    private Long friends;
    // 粉丝数
    private Long fans;
    // 发文数
    private Long statusCount;
    // 认证状态
    private String verified;
    // 入库时间
    private Date loadTime;
    // 注册时间
    private Date registerTime;
}


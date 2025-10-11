package com.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author TRS
 * @date 2023-11-30
 */
@ToString
@Getter
@Setter
public class OmAccountBean extends HybaseBean implements Serializable {

    private String sid;
    /**
     * uid
     */
    private Long uid;
    /**
     * 微信id
     */
    private String irWeiXinId;
    /**
     * 头条id
     */
    private String irMid;
    /**
     * social用户id
     */
    private String irUserId;
    /**
     * 用户名
     */
    private String irScreenName;
    /**
     * 描述
     */
    private String irDescription;
    /**
     * 性别
     */
    private String gender;
    /**
     * 好友数/关注数
     */
    private Long friends;
    /**
     * 粉丝数
     */
    private Long fans;
    /**
     * 发文数
     */
    private Long statusCount;
    /**
     * 认证状态
     */
    private String verified;
    /**
     * 入库时间
     */
    private Date loadTime;
    /**
     * 注册时间
     */
    private Date registerTime;
    /**
     * 用户图像路径
     */
    private String irProfileImageUrl;
    /**
     * 是否认证
     */
    private String irIsVerified;
    /**
     * 认证信息
     */
    private String irVerifyInfo;
    /**
     * 认证日期
     */
    private String irVerifyDate;
    /**
     * 网址域名
     */
    private String irDomain;


    /**
     * 用户中文名称
     */
    private String irUserNameZH;
    /**
     * 用户英文名称
     */
    private String irUserNameEN;
    /**
     * 用户路径
     */
    private String irUserUrl;
    /**
     * 用户类型(facebook/twitter等)
     */
    private String irUserInfoType;

    private Integer irFollowCount;
    private Integer irLikeCount;
    private Integer irViewCount;
    /**
     * 国家
     */
    private String irCountry;
    /**
     * 城市
     */
    private String irCity;
    /**
     * 经度
     */
    private Double syLongitude;
    /**
     * 纬度
     */
    private Double syLatitude;
    /**
     * 工作单位
     */
    private String irWorkUnit;
    /**
     * 工作
     */
    private String irWorkJob;
    /**
     * 描述
     */
    private String irDescribe;
    /**
     * 语言
     */
    private String syLanguage;
    /**
     * 用户图片
     */
    private String irUserImg;
    /**
     * 用户头像文件名
     */
    private String syUserImgFileName;
    private String irPhone;
    /**
     * 电话号码
     */
    private String irTelPhone;
    /**
     * 邮箱
     */
    private String irEmail;
    private String irAuThen;
    private String irAuThenDes;
    /**
     * 用户籍贯
     */
    private String irHomeTown;
    /**
     * 生日
     */
    private String irBirthday;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date irLastTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Integer syUpdateCount;
    private String syIsOverSeas;
    private Date syExtDate1;
    private Date syExtDate2;
    /**
     * 用户学校
     */
    private String irUserSchool;
    /**
     * 用户学位
     */
    private String irUserDegree;
    /**
     * 用户主修专业
     */
    private String irUserMajor;
    /**
     * 用户公司
     */
    private String irUserCompany;
    /**
     * 工作区间
     */
    private String irUserJobDuration;
    /**
     * 用户MD5加密
     */
    private String irUserMD5;
    /**
     * 对华态度
     */
    private String syToChinaAttitude;

    /**
     * 政治身份
     */
    private String syPoliticalIdent;
    /**
     * 创建时间
     */
    private Date irCreateDate;
    /**
     * 账户等级
     */
    private Integer syAccountGrade;

}


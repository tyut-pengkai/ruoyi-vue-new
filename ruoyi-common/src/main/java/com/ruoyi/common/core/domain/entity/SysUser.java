package com.ruoyi.common.core.domain.entity;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.annotation.Excel.Type;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.xss.Xss;

/**
 * 用户对象 sys_user
 * 
 * @author ruoyi
 */
@Data
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    //@Excel(name = "用户序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC, prompt = "用户编号")
    private Long userId;

    /** 部门ID */
    //@Excel(name = "部门编号", type = Type.IMPORT)
    private Long deptId;

    /** 用户账号 */
    //@Excel(name = "登录名称")
    private String userName;

    /** 用户昵称 */
    @Excel(name = "姓名")
    private String nickName;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idcard;

    /** 用户邮箱 */
    //@Excel(name = "用户邮箱")
    private String email;

    /** 手机号码 */
    @Excel(name = "电话号码", cellType = ColumnType.TEXT)
    private String phonenumber;

    /** 用户性别 */
    //@Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 密码 */
    private String password;
    /** 劳务关系 */
    @Excel(name = "劳务关系")
    private String lwgx;

    /** 所属部门 */
    @Excel(name = "所属部门")
    private String ssbm;

    /** 岗位 */
    @Excel(name = "岗位")
    private String gw;

    /** 人员性质 */
    @Excel(name = "人员性质")
    private String ryxz;

    /** 学历 */
    @Excel(name = "学历")
    private String xl;

    /** 职称 */
    @Excel(name = "职称")
    private String zc;

    /** 入职时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入职时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date rzsj;

    /** 帐号状态（0正常 1停用） */
    @Excel(name = "启用状态", readConverterExp = "0=启用,1=禁用",type = Type.EXPORT)
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 最后登录IP */
    //@Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;

    /** 最后登录时间 */
    //@Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /** 部门对象 */
    /*@Excels({
        @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
        @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })*/
    private SysDept dept;

    /** 角色对象 */
    private List<SysRole> roles;

    /** 角色组 */
    private Long[] roleIds;

    /** 岗位组 */
    private Long[] postIds;

    /** 角色ID */
    private Long roleId;

    /** 微信唯一标识 */
    //@Excel(name = "微信唯一标识")
    private String openid;





    /** 打卡编号 */
    //@Excel(name = "打卡编号")
    private String dkbh;

    /** 社会统一信用代码 */
    //@Excel(name = "社会统一信用代码")
    private String shtydm;

    public SysUser()
    {

    }

    public SysUser(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    @NotBlank(message = "手机号码不能为空")
    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getLoginIp()
    {
        return loginIp;
    }

    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp;
    }

    public Date getLoginDate()
    {
        return loginDate;
    }

    public void setLoginDate(Date loginDate)
    {
        this.loginDate = loginDate;
    }

    public SysDept getDept()
    {
        return dept;
    }

    public void setDept(SysDept dept)
    {
        this.dept = dept;
    }

    public List<SysRole> getRoles()
    {
        return roles;
    }

    public void setRoles(List<SysRole> roles)
    {
        this.roles = roles;
    }

    public Long[] getRoleIds()
    {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds)
    {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds()
    {
        return postIds;
    }

    public void setPostIds(Long[] postIds)
    {
        this.postIds = postIds;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @NotBlank(message = "身份证号不能为空")
    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    @NotBlank(message = "劳务关系不能为空")
    public String getLwgx() {
        return lwgx;
    }

    public void setLwgx(String lwgx) {
        this.lwgx = lwgx;
    }

    @NotBlank(message = "所属部门不能为空")
    public String getSsbm() {
        return ssbm;
    }

    public void setSsbm(String ssbm) {
        this.ssbm = ssbm;
    }

    @NotBlank(message = "岗位不能为空")
    public String getGw() {
        return gw;
    }

    public void setGw(String gw) {
        this.gw = gw;
    }

    @NotBlank(message = "人员性质不能为空")
    public String getRyxz() {
        return ryxz;
    }

    public void setRyxz(String ryxz) {
        this.ryxz = ryxz;
    }

    @NotBlank(message = "学历不能为空")
    public String getXl() {
        return xl;
    }

    public void setXl(String xl) {
        this.xl = xl;
    }

    @NotBlank(message = "职称不能为空")
    public String getZc() {
        return zc;
    }

    public void setZc(String zc) {
        this.zc = zc;
    }

    @NotBlank(message = "入职时间不能为空")
    public Date getRzsj() {
        return rzsj;
    }

    public void setRzsj(Date rzsj) {
        this.rzsj = rzsj;
    }

    public String getDkbh() {
        return dkbh;
    }

    public void setDkbh(String dkbh) {
        this.dkbh = dkbh;
    }

    public String getShtydm() {
        return shtydm;
    }

    public void setShtydm(String shtydm) {
        this.shtydm = shtydm;
    }
}

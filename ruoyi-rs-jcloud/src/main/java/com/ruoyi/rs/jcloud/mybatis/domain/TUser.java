/**
 * @Copyright: lyj  All rights reserved.
 */
package com.ruoyi.rs.jcloud.mybatis.domain;

import java.util.Date;
import java.io.Serializable;

public class TUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String username;
    private String password;
    private Date registerTime;
    private Date licenseTime;

    public Integer getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Date getRegisterTime() {
        return registerTime;
    }
    public Date getLicenseTime() {
        return licenseTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
    public void setLicenseTime(Date licenseTime) {
        this.licenseTime = licenseTime;
    }
}
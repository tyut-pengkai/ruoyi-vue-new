package com.ruoyi.common.core.domain.model;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 抽象层的登录用户
 * 登录用户不止只有PC端，而且只有移动端或者其他端的，为了可以兼容其他用户共同使用SpringSecurity
 * 特建此类，和SpringSecurity框架交互的用户就用此抽象用户
 * @Author juincen_lee
 * @Date 2025-04-01
 */
public abstract class AbstractLoginUser implements UserDetails {

}

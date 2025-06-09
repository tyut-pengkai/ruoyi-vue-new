package com.ruoyi.rs.jcloud.model;

import java.util.Collection;
import java.util.Date;
import com.ruoyi.common.core.domain.model.AbstractLoginUser;
import com.ruoyi.rs.jcloud.mybatis.domain.TUser;
import org.springframework.security.core.GrantedAuthority;

/**
 * 微信小程序用户
 */
public class WechatLoginUser extends AbstractLoginUser {

	private TUser user;

	public WechatLoginUser(TUser user){
		this.user = user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return user.getLicenseTime().getTime() >= System.currentTimeMillis();
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	public String getUserId() {
		return user.getId().toString();
	}

	public Date getLicenseTime(){
		return user.getLicenseTime();
	}
}

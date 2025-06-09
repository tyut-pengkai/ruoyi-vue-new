package com.ruoyi.rs.jcloud.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.sign.Md5Utils;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.rs.jcloud.model.WechatLoginUser;
import com.ruoyi.rs.jcloud.mybatis.domain.TUser;
import com.ruoyi.rs.jcloud.mybatis.domain.TUserExample;
import com.ruoyi.rs.jcloud.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MobileUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private EntityService entityService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		TUserExample ex = new TUserExample();
		ex.createCriteria().andUsernameEqualTo(username);
		List<TUser> users = entityService.selectByExample(TUser.class, ex);
		if (users.isEmpty()) {
			throw new ServiceException(String.format("用户名[%s]不存在", username));
		}

		//获取用户输入的密码
		Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
		String password = usernamePasswordAuthenticationToken.getCredentials().toString();

		TUser user = users.get(0);
		if (!user.getPassword().equalsIgnoreCase(Md5Utils.hash(password))) {
			throw new ServiceException("密码错误");
		}
		String licenseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(user.getLicenseTime());
		if (user.getLicenseTime().getTime() < System.currentTimeMillis()) {
			throw new ServiceException(String.format("账号已过期，请联系管理员，许可时间：%s", licenseTime));
		}
		return new WechatLoginUser(user);
	}
}

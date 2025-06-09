package com.ruoyi.rs.jcloud.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sign.Md5Utils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.rs.jcloud.model.WechatLoginUser;
import com.ruoyi.rs.jcloud.mybatis.domain.TUser;
import com.ruoyi.rs.jcloud.mybatis.domain.TUserExample;
import com.ruoyi.rs.jcloud.service.EntityService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserService {

	@Autowired
	@Qualifier("mobileAuthenticationManager")
	private AuthenticationManager authenticationManager;

	// 令牌自定义标识
	@Value("${token.app-header}")
	private String header;

	// 令牌秘钥
	@Value("${token.app-secret}")
	private String secret;

	// 令牌有效期（默认30分钟）
	@Value("${token.app-expireTime}")
	private int expireTime;

	public Map<String, Object> login(String username, String password){
		// 登录前置校验
		loginPreCheck(username, password);
		// 用户验证
		Authentication authentication = null;
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
			AuthenticationContextHolder.setContext(authenticationToken);
			// 该方法会去调用UserDetailsServiceImpl.loadUserByUsername  (实现是：MobileUserDetailsServiceImpl)
			authentication = authenticationManager.authenticate(authenticationToken);
		} catch (Exception e){
			throw new ServiceException(e.getMessage(), e);
		} finally {
			AuthenticationContextHolder.clearContext();
		}

		WechatLoginUser user = (WechatLoginUser) authentication.getPrincipal();
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", username);
		claims.put("userId", user.getUserId());
		claims.put("randomid", IdUtils.fastUUID());
		claims.put("timestamp", System.currentTimeMillis());
		String token = Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, secret).compact();

		Map<String, Object> result = new HashMap<>();
		result.put("token", token);
		Map<String, String> userMap = new HashMap<>();
		userMap.put("username", user.getUsername());
		userMap.put("licenseTime", DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", user.getLicenseTime()));
		result.put("user", userMap);
		return result;
	}

	private void loginPreCheck(String username, String password) {
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
			throw new ServiceException(String.format("用户名或密码不能为空", username));
		}
	}

}

package com.ruoyi.web.controller.jcloud.user;

import java.util.Map;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rs.jcloud.mybatis.domain.TUser;
import com.ruoyi.rs.jcloud.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @Anonymous
    public AjaxResult login(@RequestBody TUser data) {

        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        Map<String, Object> result = userService.login(data.getUsername(), data.getPassword());
        ajax.put("data", result);
        return ajax;
    }

    @RequestMapping(value = "verifyToken", method = RequestMethod.GET)
    @Anonymous
    public AjaxResult verifyToken(String token) {
//        boolean verify = JWTUtil.verify(token);
        return null;
    }
}

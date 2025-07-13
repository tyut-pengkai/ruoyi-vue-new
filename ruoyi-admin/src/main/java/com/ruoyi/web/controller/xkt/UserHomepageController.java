package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.userHomepage.UserHomepageOverviewVO;
import com.ruoyi.xkt.service.IUserHomepageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户首页
 *
 * @author ruoyi
 */
@Api(tags = "用户首页")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/user/homepage")
public class UserHomepageController extends BaseController {

    final IUserHomepageService userHomeService;

    @ApiOperation(value = "顶部数据统计概览", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/overview")
    public R<UserHomepageOverviewVO> overview() {
        return R.ok(BeanUtil.toBean(userHomeService.overview(), UserHomepageOverviewVO.class));
    }

}

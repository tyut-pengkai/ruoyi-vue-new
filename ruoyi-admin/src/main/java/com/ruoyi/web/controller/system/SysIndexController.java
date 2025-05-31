package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.store.StoreResVO;
import com.ruoyi.web.controller.xkt.vo.userIndex.UserOverallResVO;
import com.ruoyi.xkt.service.IStoreService;
import com.ruoyi.xkt.service.IUserIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 电商卖家首页
 *
 * @author ruoyi
 */
@Api(tags = "电商卖家首页")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/sys/user/index")
public class SysIndexController extends XktBaseController {

    final IUserIndexService userIndexService;

    @ApiOperation(value = "获取用户数据总览", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/overall")
    public R<UserOverallResVO> getOverall() {
        return R.ok(BeanUtil.toBean(userIndexService.getOverall(), UserOverallResVO.class));
    }

}

package com.ruoyi.web.controller.xkt.healthCheck;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rest/v1/health-check")
public class HealthCheckController extends BaseController {

    @GetMapping
    public R<String> healthCheck() {
        return R.ok();
    }

}

package com.ruoyi.web.controller.xkt;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.web.controller.xkt.vo.express.ZtoPrintOrderReqVO;
import com.ruoyi.web.controller.xkt.vo.express.ZtoPrintOrderRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author liangyq
 * @date 2025-04-17 14:18
 */
@Api(tags = "物流回调接口")
@RestController
@RequestMapping("/rest/v1/express-callback")
public class ExpressCallbackController extends XktBaseController {

    @Autowired
    private RedisCache redisCache;

    /**
     * 中通-生成面单图片/PDF回推
     * <p>
     * 采用http协议，字符集编码为UTF-8，ContentType："application/json"，请求方式：POST
     * 开放平台会根据三方返回的status字段判断此次推送是否成功
     * 若三方返回false，或触达出现异常，开放平台侧会进行推送重试，重试最大次数为16次
     *
     * @param vo
     * @return
     */
    @ApiOperation("中通-生成面单图片/PDF回推")
    @PostMapping("zto/print-order")
    public ZtoPrintOrderRespVO ztoPrintOrder(@RequestBody ZtoPrintOrderReqVO vo) {
        if (StrUtil.isNotEmpty(vo.getBillCode())
                && StrUtil.isNotEmpty(vo.getResult())) {
            //缓存30分钟
            redisCache.setCacheObject("ZTO-"+vo.getBillCode(),vo.getResult(),30, TimeUnit.MINUTES);
            return ZtoPrintOrderRespVO.success();
        }
        return ZtoPrintOrderRespVO.failure();
    }


}

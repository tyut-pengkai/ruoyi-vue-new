package com.ruoyi.web.controller.xkt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.web.controller.xkt.vo.express.ZtoPrintOrderReqVO;
import com.ruoyi.web.controller.xkt.vo.express.ZtoPrintOrderRespVO;
import com.ruoyi.xkt.dto.order.StoreOrderExpressTrackAddDTO;
import com.ruoyi.xkt.enums.EExpressChannel;
import com.ruoyi.xkt.enums.EExpressStatus;
import com.ruoyi.xkt.service.IStoreOrderService;
import com.ruoyi.xkt.thirdpart.yto.YtoSignUtil;
import com.ruoyi.xkt.thirdpart.yto.YtoTrackObj;
import com.ruoyi.xkt.thirdpart.zto.ZopDigestUtil;
import com.ruoyi.xkt.thirdpart.zto.ZtoTrackObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author liangyq
 * @date 2025-04-17 14:18
 */
@Api(tags = "物流回调接口")
@RestController
@RequestMapping("/rest/v1/express-callback")
public class ExpressCallbackController extends XktBaseController {

    @Value("${yto.appSecret2:}")
    private String ytoAppSecret2;
    @Value("${zto.appSecret:}")
    private String ztoAppSecret;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IStoreOrderService storeOrderService;

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
            redisCache.setCacheObject("ZTO-" + vo.getBillCode(), vo.getResult(), 30, TimeUnit.MINUTES);
            return ZtoPrintOrderRespVO.success();
        }
        return ZtoPrintOrderRespVO.failure();
    }


    @ApiOperation("圆通-轨迹推送")
    @RequestMapping(value = "yto/track")
    public String ytoTrack(HttpServletRequest servletRequest) {
        YtoTrackObj.Request request = new YtoTrackObj.Request(servletRequest);
        if (StrUtil.isNotBlank(request.getLogistics_interface()) &&
                //验签
                YtoSignUtil.verify(request.getData_digest(), request.getLogistics_interface(), ytoAppSecret2)) {
            logger.info("圆通-轨迹推送数据处理: {}", request);
            Document dom = XmlUtil.parseXml(request.getLogistics_interface());
            YtoTrackObj.Info obj = XmlUtil.xmlToBean(dom, YtoTrackObj.class).getUpdateInfo();
            StoreOrderExpressTrackAddDTO trackAddDTO = trans2OrderTrack(obj);
            storeOrderService.addTrack(trackAddDTO);
            return YtoTrackObj.Response.builder()
                    .success(true)
                    .logisticProviderID(obj.getLogisticProviderID())
                    .txLogisticID(obj.getTxLogisticID())
                    .build()
                    .toXmlStr();
        }
        logger.warn("圆通-轨迹推送数据异常: {}", request);
        return YtoTrackObj.Response.builder()
                .success(false)
                .build()
                .toXmlStr();
    }

    @ApiOperation("中通-轨迹推送")
    @PostMapping(value = "zto/track")
    public String ztoTrack(@RequestBody ZtoTrackObj.Request request) {
        if (StrUtil.isNotBlank(request.getData()) &&
                //验签
                ZopDigestUtil.verify(request.getData_digest(), request.getData(), ztoAppSecret)) {
            logger.info("中通-轨迹推送数据处理: {}", request);
            ZtoTrackObj.Info obj = JSONUtil.toBean(request.getData(), ZtoTrackObj.Info.class);
            StoreOrderExpressTrackAddDTO trackAddDTO = trans2OrderTrack(obj);
            storeOrderService.addTrack(trackAddDTO);
            return ZtoTrackObj.Response.builder()
                    .status(true)
                    .build()
                    .toJsonStr();
        }
        logger.warn("中通-轨迹推送数据异常: {}", request);
        return ZtoTrackObj.Response.builder()
                .status(false)
                .build()
                .toJsonStr();
    }

    private StoreOrderExpressTrackAddDTO trans2OrderTrack(ZtoTrackObj.Info ztTrack) {
        StoreOrderExpressTrackAddDTO dto = new StoreOrderExpressTrackAddDTO();
        dto.setExpressWaybillNo(ztTrack.getBillCode());
        dto.setAction(ztTrack.getAction());
        dto.setDescription(ztTrack.getActionTime() + " " + ztTrack.getRemark());
        dto.setExpressId(EExpressChannel.ZTO.getExpressId());
        /**
         * 事件类型
         * <p>
         *  GOT	                收件	    网点揽收
         *  DEPARTURE	        发件	    从网点或分拨中心发出
         *  ARRIVAL	            到件	    到达网点或分拨中心
         *  DISPATCH	        派件	    业务员派送
         *  RETURN_SCAN	        退件	    发生退回或改地址，具体类型见退改类型（returnType）字段
         *  RETURN_SIGNED	    退件签收	    已经退回至寄件客户
         *  INBOUND	            入站	    放入快递超市/自提柜/第三方代理点等
         *  HANDOVERSCAN_SIGNED 第三方妥投	入站后妥投成功
         *  DEPARTURE_SIGNED    出站签收	    客户从快递超市/自提柜/第三方代理点等取出
         *  SIGNED	            签收	    客户正常签收
         *  PROBLEM	            问题件	    网点或中心登记的问题件，问题件类型在问题件编码（problemCode）字段体现
         */
        switch (ztTrack.getAction()) {
            case "GOT":
                dto.setExpressStatus(EExpressStatus.PICKED_UP);
                break;
            case "SIGNED":
                dto.setExpressStatus(EExpressStatus.COMPLETED);
                break;
        }
        return dto;
    }

    private StoreOrderExpressTrackAddDTO trans2OrderTrack(YtoTrackObj.Info ytTrack) {
        StoreOrderExpressTrackAddDTO dto = new StoreOrderExpressTrackAddDTO();
        dto.setExpressWaybillNo(ytTrack.getMailNo());
        dto.setAction(ytTrack.getInfoContent());
        dto.setDescription(DateUtil.formatDateTime(ytTrack.getAcceptTime()) + " " + ytTrack.getRemark());
        dto.setExpressId(EExpressChannel.YTO.getExpressId());
        switch (ytTrack.getInfoContent()) {
            case "GOT":
                dto.setExpressStatus(EExpressStatus.PICKED_UP);
                break;
            case "SIGNED":
                dto.setExpressStatus(EExpressStatus.COMPLETED);
                break;
        }
        return dto;
    }

}

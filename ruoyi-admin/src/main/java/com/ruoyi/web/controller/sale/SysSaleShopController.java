package com.ruoyi.web.controller.sale;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.NoticeType;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.payment.constants.PaymentDefine;
import com.ruoyi.payment.domain.Payment;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import com.ruoyi.sale.domain.SysSaleOrderItemGoods;
import com.ruoyi.sale.domain.vo.SysCardVo;
import com.ruoyi.sale.domain.vo.SysLoginCodeVo;
import com.ruoyi.sale.domain.vo.SysSaleOrderItemVo;
import com.ruoyi.sale.service.ISysSaleOrderItemGoodsService;
import com.ruoyi.sale.service.ISysSaleOrderService;
import com.ruoyi.sale.service.ISysSaleShopService;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.web.controller.sale.vo.SaleAppVo;
import com.ruoyi.web.controller.sale.vo.SaleCardTemplateVo;
import com.ruoyi.web.controller.sale.vo.SaleOrderVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 销售Controller
 *
 * @author zwgu
 * @date 2022-02-21
 */
@Slf4j
@RestController
@RequestMapping("/sale/shop")
public class SysSaleShopController extends BaseController {
    @Resource
    private SysAppMapper sysAppMapper;
    @Resource
    private SysCardTemplateMapper sysCardTemplateMapper;
    @Resource
    private SysLoginCodeTemplateMapper sysLoginCodeTemplateMapper;
    @Resource
    private ISysSaleShopService sysSaleShopService;
    @Resource
    private SysCardMapper sysCardMapper;
    @Resource
    private SysLoginCodeMapper sysLoginCodeMapper;
    @Resource
    private ISysSaleOrderService sysSaleOrderService;
    @Resource
    private ISysSaleOrderItemGoodsService sysSaleOrderItemGoodsService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ISysNoticeService sysNoticeService;

    /**
     * 查询软件列表
     */
    @GetMapping("/appList")
    public TableDataInfo appList() {
        List<SaleAppVo> saleAppVoList = new ArrayList<>();
        List<SysApp> appList = sysAppMapper.selectSysAppList(new SysApp());
        for (SysApp app : appList) {
            if (app.getAuthType().equals(AuthType.ACCOUNT)) {
                SysCardTemplate ct = new SysCardTemplate();
                ct.setAppId(app.getAppId());
                ct.setOnSale(UserConstants.YES);
                saleAppVoList.add(new SaleAppVo(app.getAppId(), app.getAppName(), sysCardTemplateMapper.selectSysCardTemplateList(ct).size()));
            } else {
                SysLoginCodeTemplate ct = new SysLoginCodeTemplate();
                ct.setAppId(app.getAppId());
                ct.setOnSale(UserConstants.YES);
                saleAppVoList.add(new SaleAppVo(app.getAppId(), app.getAppName(), sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateList(ct).size()));
            }

        }
        return getDataTable(saleAppVoList);
    }

    /**
     * 查询卡密模板列表
     */
    @GetMapping("/listCategory")
    public TableDataInfo cardTemplateList(Long appId) {
        SysApp app = sysAppMapper.selectSysAppByAppId(appId);
        List<SaleCardTemplateVo> saleCardTemplateVoList = new ArrayList<>();
        if (app.getAuthType() == AuthType.ACCOUNT) {
            SysCardTemplate sysCardTemplate = new SysCardTemplate();
            sysCardTemplate.setAppId(appId);
            List<SysCardTemplate> list = sysCardTemplateMapper.selectSysCardTemplateList(sysCardTemplate);
            for (SysCardTemplate ct : list) {
                int cardCount;
                if (UserConstants.YES.equals(ct.getEnableAutoGen())) {
                    cardCount = 1000;
                } else {
                    cardCount = sysSaleShopService.getSaleableCard(ct.getTemplateId()).size();
                    if (cardCount > 1000) {
                        cardCount = 1000;
                    }
                }
                saleCardTemplateVoList.add(new SaleCardTemplateVo(ct.getTemplateId(), ct.getCardName(), ct.getPrice(), cardCount));
            }
        } else if (app.getAuthType() == AuthType.LOGIN_CODE) {
            SysLoginCodeTemplate sysLoginCodeTemplate = new SysLoginCodeTemplate();
            sysLoginCodeTemplate.setAppId(appId);
            List<SysLoginCodeTemplate> list = sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateList(sysLoginCodeTemplate);
            for (SysLoginCodeTemplate ct : list) {
                int cardCount;
                if (UserConstants.YES.equals(ct.getEnableAutoGen())) {
                    cardCount = 1000;
                } else {
                    cardCount = sysSaleShopService.getSaleableLoginCode(ct.getTemplateId()).size();
                    if (cardCount > 1000) {
                        cardCount = 1000;
                    }
                }
                saleCardTemplateVoList.add(new SaleCardTemplateVo(ct.getTemplateId(), ct.getCardName(), ct.getPrice(), cardCount));
            }
        }
        return getDataTable(saleCardTemplateVoList);
    }

    @PostMapping("/checkStock")
    public AjaxResult checkStock(@RequestBody SaleOrderVo saleOrderVo) {

        Payment payment = PaymentDefine.paymentMap.get(saleOrderVo.getPayMode());
        if (payment == null) {
            throw new ServiceException("支付方式有误，下单失败", 400);
        }
        if (saleOrderVo.getBuyNum() <= 0) {
            throw new ServiceException("购买数量有误，下单失败", 400);
        }
        if (saleOrderVo.getTemplateId() == null || saleOrderVo.getAppId() == null) {
            throw new ServiceException("提交数据有误，下单失败", 400);
        }

        // 检查库存
        SysApp app = sysAppMapper.selectSysAppByAppId(saleOrderVo.getAppId());
        if (app.getAuthType() == AuthType.ACCOUNT) {
            SysCardTemplate tpl = sysCardTemplateMapper.selectSysCardTemplateByTemplateId(saleOrderVo.getTemplateId());
            if (!UserConstants.YES.equals(tpl.getOnSale())) {
                throw new ServiceException("商品已下架", 400);
            }
            if (!UserConstants.YES.equals(tpl.getEnableAutoGen())) { // 非自动制卡
                if (sysSaleShopService.getSaleableCard(tpl.getTemplateId()).size() < saleOrderVo.getBuyNum()) {
                    throw new ServiceException("库存不足，请稍后再试", 400);
                }
            }
        } else if (app.getAuthType() == AuthType.LOGIN_CODE) {
            SysLoginCodeTemplate tpl = sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateByTemplateId(saleOrderVo.getTemplateId());
            if (!UserConstants.YES.equals(tpl.getOnSale())) {
                throw new ServiceException("商品已下架", 400);
            }
            if (!UserConstants.YES.equals(tpl.getEnableAutoGen())) { // 非自动制卡
                if (sysSaleShopService.getSaleableLoginCode(tpl.getTemplateId()).size() < saleOrderVo.getBuyNum()) {
                    throw new ServiceException("库存不足，请稍后再试", 400);
                }
            }
        }
        return AjaxResult.success();
    }

    @PostMapping("/createSaleOrder")
    public AjaxResult createSaleOrder(@RequestBody SaleOrderVo saleOrderVo) {

        Payment payment = PaymentDefine.paymentMap.get(saleOrderVo.getPayMode());
        if (payment == null) {
            throw new ServiceException("支付方式有误，下单失败", 400);
        }
        if (saleOrderVo.getBuyNum() <= 0) {
            throw new ServiceException("购买数量有误，下单失败", 400);
        }
        if (saleOrderVo.getTemplateId() == null || saleOrderVo.getAppId() == null) {
            throw new ServiceException("提交数据有误，下单失败", 400);
        }

        SysApp app = sysAppMapper.selectSysAppByAppId(saleOrderVo.getAppId());
        // 1.检查库存
        checkStock(saleOrderVo);
        // 2.订单生成
        String orderNo = genOrderNo(saleOrderVo);
        SysSaleOrder sso = new SysSaleOrder();
        SysSaleOrderItem ssoi = new SysSaleOrderItem();
        sso.setOrderNo(orderNo);
        sso.setUserId(null);
        // 3.计算金额
        BigDecimal unitPrice = null;
        String templateType = null;
        String goodsName = null;
        if (app.getAuthType() == AuthType.ACCOUNT) {
            SysCardTemplate sct = sysCardTemplateMapper.selectSysCardTemplateByTemplateId(saleOrderVo.getTemplateId());
            unitPrice = sct.getPrice();
            templateType = "1";
            goodsName = sct.getCardName();
        } else if (app.getAuthType() == AuthType.LOGIN_CODE) {
            SysLoginCodeTemplate sct = sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateByTemplateId(saleOrderVo.getTemplateId());
            unitPrice = sct.getPrice();
            templateType = "2";
            goodsName = sct.getCardName();
        }
        if (unitPrice == null) {
            throw new ServiceException("商品未设定价格，下单失败", 400);
        }
        BigDecimal totalFee = unitPrice.multiply(BigDecimal.valueOf(saleOrderVo.getBuyNum()));
        sso.setTotalFee(totalFee);
        // 4.计算优惠
        sso.setDiscountRule(null);
        sso.setDiscountFee(null);
        // 5.计算要支付的金额
        sso.setActualFee(totalFee);
        sso.setPayMode(saleOrderVo.getPayMode());
        sso.setStatus(SaleOrderStatus.WAIT_PAY);
        sso.setContact(saleOrderVo.getContact());
        sso.setQueryPass(saleOrderVo.getQueryPass());
        sso.setCreateBy(null);
        sso.setPaymentTime(null);
        sso.setDeliveryTime(null);
        sso.setFinishTime(null);
        sso.setCloseTime(null);
        sso.setRemark(null);
        sso.setExpireTime(new Date(DateUtils.getNowDate().getTime() + 1000 * 60 * 5));
        sso.setUpdateBy(null);
        sso.setUpdateTime(null);
        // 6.订单详情
        ssoi.setTemplateType(templateType); // 1卡类 2登录码类
        ssoi.setTemplateId(saleOrderVo.getTemplateId());
        ssoi.setNum(saleOrderVo.getBuyNum());
        ssoi.setTitle("[" + app.getAppName() + "]" + goodsName);
        ssoi.setPrice(unitPrice);
        ssoi.setTotalFee(totalFee);
        ssoi.setDiscountRule(null);
        ssoi.setDiscountFee(null);
        ssoi.setActualFee(totalFee);
        List<SysSaleOrderItem> itemList = new ArrayList<>();
        itemList.add(ssoi);
        sso.setSysSaleOrderItemList(itemList);
        sysSaleOrderService.insertSysSaleOrder(sso);
        redisCache.redisTemplate.opsForZSet().add(Constants.SALE_ORDER_EXPIRE_KEY, sso.getPayMode() + "|" + orderNo, sso.getExpireTime().getTime());
        return AjaxResult.success().put("orderNo", orderNo);
    }

    private synchronized String genOrderNo(SaleOrderVo saleOrderVo) {
        String appId = String.format("%04d", saleOrderVo.getAppId());
        appId = appId.substring(appId.length() - 4, appId.length());
        String tplId = String.format("%04d", saleOrderVo.getTemplateId());
        tplId = tplId.substring(tplId.length() - 4, tplId.length());
        return DateUtils.dateTimeNow("yyMMddHHmmssSSS") + appId + tplId + RandomStringUtils.randomNumeric(2);
    }

    @GetMapping("/paySaleOrder")
    public AjaxResult paySaleOrder(String orderNo) {
        if (orderNo == null) {
            throw new ServiceException("订单不存在", 400);
        }
        SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderNo(orderNo);
        if (sso == null) {
            throw new ServiceException("订单不存在", 400);
        }
        if (sso.getStatus() != SaleOrderStatus.WAIT_PAY) {
            throw new ServiceException("该订单非待支付订单或已过期", 400);
        }
        // 调第三方支付平台
        String payMode = sso.getPayMode();
        if (StringUtils.isBlank(payMode)) {
            throw new ServiceException("未指定支付方式或支付方式有误", 400);
        }
        Payment payment = PaymentDefine.paymentMap.get(payMode);
        if (payment == null || !payment.getEnable()) {
            throw new ServiceException("暂不支持该支付方式", 400);
        }
        Object payResponse = payment.pay(sso);
        return AjaxResult.success(payResponse).put("showType", payment.getShowType()) // qr：扫码，html：渲染，forward：跳转
                .put("actualFee", sso.getActualFee()).put("payMode", payment.getName());
    }

    /**
     * 支付完成但是发货失败，调用此接口重新发货
     *
     * @param orderNo
     * @return
     */
    @GetMapping("/fetchGoods")
    public AjaxResult fetchGoods(String orderNo) {
        if (orderNo == null) {
            throw new ServiceException("订单不存在", 400);
        }
        SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderNo(orderNo);
        if (sso == null) {
            throw new ServiceException("订单不存在", 400);
        }
        sysSaleShopService.deliveryGoods(sso);
        return AjaxResult.success();
    }

    @RequestMapping("/notify/{payMode}")
    public void notify_alipay(HttpServletRequest request, HttpServletResponse response, @PathVariable("payMode") String payMode) {
        PaymentDefine.paymentMap.get(payMode).notify(request, response);
    }

    @GetMapping("/getCardList")
    public AjaxResult getCardList(@RequestParam("orderNo") String orderNo, @RequestParam("queryPass") String queryPass) {
        if (orderNo == null) {
            throw new ServiceException("订单不存在", 400);
        }
        SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderNo(orderNo);
        if (sso == null) {
            throw new ServiceException("订单不存在", 400);
        }
        if (sso.getStatus() == null || sso.getStatus() == SaleOrderStatus.WAIT_PAY || sso.getStatus() == SaleOrderStatus.TRADE_CLOSED) {
            throw new ServiceException("订单未支付或已过期", 400);
        }
        if (!sso.getQueryPass().equals(queryPass)) {
            throw new ServiceException("查询密码有误", 400);
        }

        List<SysSaleOrderItem> itemList = sso.getSysSaleOrderItemList();
        List<SysSaleOrderItemVo> itemVoList = new ArrayList<>();
        for (SysSaleOrderItem item : itemList) {
            item.setGoodsList(new ArrayList<>());
            List<SysSaleOrderItemGoods> goodsList = sysSaleOrderItemGoodsService.selectSysSaleOrderItemGoodsByItemId(item.getItemId());
            if (goodsList != null && goodsList.size() > 0) {
                Long[] ids = goodsList.stream().map(SysSaleOrderItemGoods::getCardId).toArray(Long[]::new);
                if ("1".equals(item.getTemplateType())) { // 充值卡
                    List<SysCard> cardList = sysCardMapper.selectSysCardByCardIds(ids);
                    for (SysCard card : cardList) {
                        item.getGoodsList().add(new SysCardVo(card));
                    }
                } else if ("2".equals(item.getTemplateType())) { // 登录码
                    List<SysLoginCode> cardList = sysLoginCodeMapper.selectSysLoginCodeByCardIds(ids);
                    for (SysLoginCode card : cardList) {
                        item.getGoodsList().add(new SysLoginCodeVo(card));
                    }
                }
            }
            itemVoList.add(new SysSaleOrderItemVo(item));
        }

        return AjaxResult.success().put("itemList", itemVoList);
    }

    /**
     * 查询销售订单列表，订单查询调用
     */
    @GetMapping("/querySaleOrderByContact")
    public TableDataInfo querySaleOrderByContact(SysSaleOrder sysSaleOrder) {
        List<SysSaleOrder> list = sysSaleOrderService.selectSysSaleOrderQueryLimit5(sysSaleOrder);
        return getDataTable(list);
    }

    /**
     * 获取商店配置
     *
     * @return
     */
    @GetMapping("/getShopConfig")
    public AjaxResult getShopConfig() {
        Map<String, Object> map = new HashMap<>();
        // 公告
        SysNotice sysNotice = new SysNotice();
        sysNotice.setNoticeType(NoticeType.FRONTEND.getCode());
        sysNotice.setStatus(UserConstants.NORMAL);
        SysNotice latestNotice = sysNoticeService.selectLatestNotice(sysNotice);
        if (latestNotice != null) {
            map.put("saleShopNotice", latestNotice.getNoticeContent());
        }
        // 支付方式
        List<Map<String, String>> payModeList = new ArrayList<>();
        for (Payment payment : PaymentDefine.paymentMap.values()) {
            if (payment.getEnable()) {
                Map<String, String> payModeMap = new HashMap<>();
                payModeMap.put("code", payment.getCode());
                payModeMap.put("name", payment.getName());
                payModeMap.put("img", payment.getIcon());
                payModeList.add(payModeMap);
            }
        }
        map.put("payModeList", payModeList);
        return AjaxResult.success(map);
    }
}

package com.ruoyi.web.controller.sale;

import com.ruoyi.api.v1.api.noAuth.code.RechargeLoginCode;
import com.ruoyi.api.v1.api.noAuth.user.RechargeCard;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.vo.SysAppUserDeviceCodeVo;
import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.tree.TreeUtil;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
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
import com.ruoyi.system.domain.vo.CountVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.*;
import com.ruoyi.web.controller.sale.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private ISysPaymentService sysPaymentService;
    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysAppUserDeviceCodeService sysAppUserDeviceCodeService;
    @Resource
    private ISysDeviceCodeService sysDeviceCodeService;
    @Resource
    private ISysConfigWebsiteService sysConfigWebsiteService;
    @Resource
    private ISysNavigationService sysNavigationService;

    private static final SnowflakeIdWorker sf = new SnowflakeIdWorker();

    /**
     * 查询软件列表
     */
    @GetMapping("/appList")
    @RateLimiter(limitType = LimitType.IP)
    public TableDataInfo appList(SysApp sysApp) {
        List<SaleAppVo> saleAppVoList = new ArrayList<>();
        List<SysApp> appList = new ArrayList<>();
        if(StringUtils.isBlank(sysApp.getCardUrl())) {
            appList = sysAppMapper.selectSysAppList(sysApp);
        } else {
            SysCardTemplate ct = new SysCardTemplate();
            ct.setOnSale(UserConstants.YES);
            ct.setShopUrl(sysApp.getCardUrl());
            List<SysCardTemplate> cardTemplateList = sysCardTemplateMapper.selectSysCardTemplateList(ct);
            SysLoginCodeTemplate ct2 = new SysLoginCodeTemplate();
            ct2.setOnSale(UserConstants.YES);
            ct2.setShopUrl(sysApp.getCardUrl());
            List<SysLoginCodeTemplate> loginCodeTemplateList = sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateList(ct2);
            if(!cardTemplateList.isEmpty()) {
                for (SysCardTemplate cardTemplate : cardTemplateList) {
                    appList.add(sysAppMapper.selectSysAppByAppId(cardTemplate.getAppId()));
                }
            }
            if(!loginCodeTemplateList.isEmpty()) {
                for (SysLoginCodeTemplate loginCodeTemplate : loginCodeTemplateList) {
                    appList.add(sysAppMapper.selectSysAppByAppId(loginCodeTemplate.getAppId()));
                }
            }
        }
        Map<Long, CountVo> cardTemplateCountMap = sysCardTemplateMapper.selectSysCardTemplateOnSaleCountGroupByAppId();
        Map<Long, CountVo> loginCodeTemplateCountMap = sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateOnSaleCountGroupByAppId();
        for (SysApp app : appList) {
            if (app.getAuthType().equals(AuthType.ACCOUNT)) {
                saleAppVoList.add(new SaleAppVo(app.getAppId(), app.getAppName(), cardTemplateCountMap.getOrDefault(app.getAppId(), new CountVo()).getCount()));
            } else {
                saleAppVoList.add(new SaleAppVo(app.getAppId(), app.getAppName(), loginCodeTemplateCountMap.getOrDefault(app.getAppId(), new CountVo()).getCount()));
            }
        }
        return getDataTable(saleAppVoList);
    }

    /**
     * 查询卡密模板列表
     */
    @GetMapping("/listCategory")
    @RateLimiter(limitType = LimitType.IP)
    public TableDataInfo cardTemplateList(Long appId, String shopUrl) {
        SysApp app = sysAppMapper.selectSysAppByAppId(appId);
        List<SaleCardTemplateVo> saleCardTemplateVoList = new ArrayList<>();
        if (app.getAuthType() == AuthType.ACCOUNT) {
            SysCardTemplate sysCardTemplate = new SysCardTemplate();
            sysCardTemplate.setAppId(appId);
            sysCardTemplate.setShopUrl(shopUrl);
            sysCardTemplate.setOnSale(UserConstants.YES);
            List<SysCardTemplate> list = sysCardTemplateMapper.selectSysCardTemplateList(sysCardTemplate);
            Map<Long, Long> cardSize = sysSaleShopService.getSaleableCardSizeAll();
            list.parallelStream().map(
                ct -> {
                    long cardCount = 0;
                    if (UserConstants.YES.equals(ct.getEnableAutoGen())) {
                        cardCount = 1000;
                    } else {
//                        cardCount = sysSaleShopService.getSaleableCardSize(ct.getTemplateId());
                        if(cardSize.containsKey(ct.getTemplateId())) {
                            cardCount = cardSize.get(ct.getTemplateId());
                            if (cardCount > 1000) {
                                cardCount = 1000;
                            }
                        }
                    }
                    saleCardTemplateVoList.add(new SaleCardTemplateVo(ct.getTemplateId(), ct.getCardName(), ct.getPrice(), cardCount, ct.getMinBuyNum()));
                    return null;
                }
            ).collect(Collectors.toList());
//            for (SysCardTemplate ct : list) {
//                int cardCount;
//                if (UserConstants.YES.equals(ct.getEnableAutoGen())) {
//                    cardCount = 1000;
//                } else {
//                    cardCount = sysSaleShopService.getSaleableCardSize(ct.getTemplateId());
//                    if (cardCount > 1000) {
//                        cardCount = 1000;
//                    }
//                }
//                saleCardTemplateVoList.add(new SaleCardTemplateVo(ct.getTemplateId(), ct.getCardName(), ct.getPrice(), cardCount, ct.getMinBuyNum()));
//            }
        } else if (app.getAuthType() == AuthType.LOGIN_CODE) {
            SysLoginCodeTemplate sysLoginCodeTemplate = new SysLoginCodeTemplate();
            sysLoginCodeTemplate.setAppId(appId);
            sysLoginCodeTemplate.setShopUrl(shopUrl);
            sysLoginCodeTemplate.setOnSale(UserConstants.YES);
            List<SysLoginCodeTemplate> list = sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateList(sysLoginCodeTemplate);
            Map<Long, Long> cardSize = sysSaleShopService.getSaleableLoginCodeSizeAll();
            list.parallelStream().map(ct -> {
                long cardCount = 0;
                if (UserConstants.YES.equals(ct.getEnableAutoGen())) {
                    cardCount = 1000;
                } else {
//                    cardCount = sysSaleShopService.getSaleableLoginCodeSize(ct.getTemplateId());
                    if(cardSize.containsKey(ct.getTemplateId())) {
                        cardCount = cardSize.get(ct.getTemplateId());
                        if (cardCount > 1000) {
                            cardCount = 1000;
                        }
                    }
                }
                saleCardTemplateVoList.add(new SaleCardTemplateVo(ct.getTemplateId(), ct.getCardName(), ct.getPrice(), cardCount, ct.getMinBuyNum()));
                return null;
            }).collect(Collectors.toList());
//            for (SysLoginCodeTemplate ct : list) {
//                int cardCount;
//                if (UserConstants.YES.equals(ct.getEnableAutoGen())) {
//                    cardCount = 1000;
//                } else {
//                    cardCount = sysSaleShopService.getSaleableLoginCodeSize(ct.getTemplateId());
//                    if (cardCount > 1000) {
//                        cardCount = 1000;
//                    }
//                }
//                saleCardTemplateVoList.add(new SaleCardTemplateVo(ct.getTemplateId(), ct.getCardName(), ct.getPrice(), cardCount, ct.getMinBuyNum()));
//            }
        }
        List<SaleCardTemplateVo> collect = saleCardTemplateVoList.stream().sorted(Comparator.comparing(SaleCardTemplateVo::getTemplateId)).collect(Collectors.toList());
        return getDataTable(collect);
    }

    @PostMapping("/checkStock")
    @RateLimiter(limitType = LimitType.IP)
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
            if(saleOrderVo.getBuyNum() < tpl.getMinBuyNum()) {
                throw new ServiceException("不满足最少购买数量：" + tpl.getMinBuyNum() + "，请增加购买数量", 400);
            }
            if (!UserConstants.YES.equals(tpl.getEnableAutoGen())) { // 非自动制卡
                if (sysSaleShopService.getSaleableCardSize(tpl.getTemplateId()) < saleOrderVo.getBuyNum()) {
                    throw new ServiceException("库存不足，请稍后再试", 400);
                }
            }
        } else if (app.getAuthType() == AuthType.LOGIN_CODE) {
            SysLoginCodeTemplate tpl = sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateByTemplateId(saleOrderVo.getTemplateId());
            if (!UserConstants.YES.equals(tpl.getOnSale())) {
                throw new ServiceException("商品已下架", 400);
            }
            if(saleOrderVo.getBuyNum() < tpl.getMinBuyNum()) {
                throw new ServiceException("不满足最少购买数量：" + tpl.getMinBuyNum() + "，请增加购买数量", 400);
            }
            if (!UserConstants.YES.equals(tpl.getEnableAutoGen())) { // 非自动制卡
                if (sysSaleShopService.getSaleableLoginCodeSize(tpl.getTemplateId()) < saleOrderVo.getBuyNum()) {
                    throw new ServiceException("库存不足，请稍后再试", 400);
                }
            }
        }
        return AjaxResult.success();
    }

    @PostMapping("/createSaleOrder")
    @RateLimiter(limitType = LimitType.IP)
    public AjaxResult createSaleOrder(@RequestBody SaleOrderVo saleOrderVo) {

//        if (StringUtils.isAnyBlank(saleOrderVo.getContact(), saleOrderVo.getQueryPass())) {
//            throw new ServiceException("联系方式或查询密码不能为空", 400);
//        }
        if (StringUtils.isBlank(saleOrderVo.getContact())) {
            throw new ServiceException("联系方式不能为空", 400);
        }
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
        String orderNo = genOrderNo();
        SysSaleOrder sso = new SysSaleOrder();
        SysSaleOrderItem ssoi = new SysSaleOrderItem();
        sso.setOrderNo(orderNo);
        sso.setOrderType(OrderType.SALE);
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
//        sso.setQueryPass(saleOrderVo.getQueryPass());
        sso.setCreateBy(null);
        sso.setPaymentTime(null);
        sso.setDeliveryTime(null);
        sso.setManualDelivery('0');
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
        redisCache.redisTemplate.opsForZSet().add(CacheConstants.SALE_ORDER_EXPIRE_KEY, sso.getPayMode() + "|" + orderNo, sso.getExpireTime().getTime());
        return AjaxResult.success().put("orderNo", orderNo);
    }

    @PostMapping("/createChargeOrder")
    @RateLimiter(limitType = LimitType.IP)
    public AjaxResult createChargeOrder(@RequestBody ChargeOrderVo chargeOrderVo) {
        Payment payment = PaymentDefine.paymentMap.get(chargeOrderVo.getPayMode());
        if (payment == null) {
            throw new ServiceException("支付方式有误，下单失败", 400);
        }
        if (chargeOrderVo.getAmount() == null || BigDecimal.ZERO.compareTo(chargeOrderVo.getAmount()) >= 0) {
            throw new ServiceException("充值金额有误，下单失败", 400);
        }
        // 2.订单生成
        String orderNo = genOrderNo();
        SysSaleOrder sso = new SysSaleOrder();
        SysSaleOrderItem ssoi = new SysSaleOrderItem();
        sso.setOrderNo(orderNo);
        sso.setOrderType(OrderType.CHARGE);
        sso.setUserId(getUserId());
        // 3.计算金额
        BigDecimal totalFee = chargeOrderVo.getAmount();
        sso.setTotalFee(totalFee);
        // 4.计算优惠
        sso.setDiscountRule(null);
        sso.setDiscountFee(null);
        // 5.计算要支付的金额
        sso.setActualFee(totalFee);
        sso.setPayMode(chargeOrderVo.getPayMode());
        sso.setStatus(SaleOrderStatus.WAIT_PAY);
        sso.setContact(null);
        sso.setQueryPass(null);
        sso.setCreateBy(getUsername());
        sso.setPaymentTime(null);
        sso.setDeliveryTime(null);
        sso.setManualDelivery('0');
        sso.setFinishTime(null);
        sso.setCloseTime(null);
        sso.setRemark(null);
        sso.setExpireTime(new Date(DateUtils.getNowDate().getTime() + 1000 * 60 * 5));
        sso.setUpdateBy(null);
        sso.setUpdateTime(null);
        // 6.订单详情
        ssoi.setTemplateType(null); // 1卡类 2登录码类
        ssoi.setTemplateId(null);
        ssoi.setNum(1);
        String prefix = "";
        SysConfigWebsite website = sysConfigWebsiteService.getById(1);
        if (website != null && website.getShortName() != null) {
            prefix = website.getShortName() + "-";
        }
        ssoi.setTitle("[" + prefix + "余额充值]账号：" + getUsername() + "，" + chargeOrderVo.getAmount() + "元");
        ssoi.setPrice(chargeOrderVo.getAmount());
        ssoi.setTotalFee(totalFee);
        ssoi.setDiscountRule(null);
        ssoi.setDiscountFee(null);
        ssoi.setActualFee(totalFee);
        List<SysSaleOrderItem> itemList = new ArrayList<>();
        itemList.add(ssoi);
        sso.setSysSaleOrderItemList(itemList);
        sysSaleOrderService.insertSysSaleOrder(sso);
        redisCache.redisTemplate.opsForZSet().add(CacheConstants.SALE_ORDER_EXPIRE_KEY, sso.getPayMode() + "|" + orderNo, sso.getExpireTime().getTime());
        return AjaxResult.success().put("orderNo", orderNo);
    }

    private String genOrderNo() {
        return String.valueOf(sf.nextId());
    }

    @GetMapping("/paySaleOrder")
    @RateLimiter(limitType = LimitType.IP)
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
        SysPayment payment1 = sysPaymentService.selectSysPaymentByPayCode(payMode);
        if (payment == null || payment1 == null || !UserConstants.NORMAL.equals(payment1.getStatus())) {
            throw new ServiceException("暂不支持该支付方式", 400);
        }
        if (BigDecimal.ZERO.compareTo(sso.getActualFee()) < 0) {
            Object payResponse = payment.pay(sso);
            return AjaxResult.success(payResponse).put("showType", payment.getShowType()) // qr：扫码，html：渲染，forward：跳转
                    .put("actualFee", sso.getActualFee()).put("payMode", payment.getName());
        } else {
            // 0元订单
            SaleOrderStatus status = sso.getStatus();
            try {
                sso.setStatus(SaleOrderStatus.PAID);
                sysSaleShopService.deliveryGoods(sso);
                sysSaleOrderService.updateSysSaleOrder(sso);
            } catch (Exception e) {
                sso.setStatus(status);
                sysSaleOrderService.updateSysSaleOrder(sso);
                throw e;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            return AjaxResult.success(map).put("showType", "free") // qr：扫码，html：渲染，forward：跳转
                    .put("actualFee", sso.getActualFee()).put("payMode", payment.getName());
        }
    }

    /**
     * 支付完成但是发货失败，调用此接口重新发货
     *
     * @param orderNo
     * @return
     */
    @GetMapping("/fetchGoods")
    @RateLimiter(limitType = LimitType.IP)
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
    public void notify(HttpServletRequest request, HttpServletResponse response, @PathVariable("payMode") String payMode) {
        PaymentDefine.paymentMap.get(payMode).notify(request, response);
    }

    @GetMapping("/getCardList")
    @RateLimiter(limitType = LimitType.IP)
    public AjaxResult getCardList(@RequestParam("orderNo") String orderNo, @RequestParam(value = "queryPass", required = false) String queryPass) {
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
//        if (!sso.getQueryPass().equals(queryPass)) {
//            throw new ServiceException("查询密码有误", 400);
//        }

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
    @RateLimiter(limitType = LimitType.IP)
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
    @RateLimiter(limitType = LimitType.IP)
    public AjaxResult getShopConfig() {
        Map<String, Object> map = new HashMap<>();
        // 公告
        SysNotice sysNotice = new SysNotice();
        sysNotice.setNoticeType(NoticeType.FRONTEND.getCode());
        sysNotice.setStatus(UserConstants.NORMAL);
        SysNotice latestNotice = sysNoticeService.selectLatestNotice(sysNotice);
        if (latestNotice != null) {
            map.put("saleShopNotice", latestNotice.getNoticeContent());
            map.put("saleShopNoticeTitle", latestNotice.getNoticeTitle());
        }
        // 支付方式
        List<Map<String, String>> payModeList = new ArrayList<>();
        for (Payment payment : PaymentDefine.paymentMap.values()) {
            SysPayment payment1 = sysPaymentService.selectSysPaymentByPayCode(payment.getCode());
            if (payment1 != null && UserConstants.NORMAL.equals(payment1.getStatus())) {
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

    /**
     * 获取订单是否已支付
     *
     * @param orderNo
     * @return
     */
    @GetMapping("/getPayStatus")
    @RateLimiter(limitType = LimitType.IP)
    public AjaxResult getPayStatus(String orderNo) {
        SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderNo(orderNo);
        if (SaleOrderStatus.PAID == sso.getStatus() || SaleOrderStatus.TRADE_SUCCESS == sso.getStatus() || SaleOrderStatus.TRADE_FINISHED == sso.getStatus()) {
            return AjaxResult.success("1");
        }
        return AjaxResult.success("0");
    }

    /**
     * 获取卡密信息
     *
     * @param cardNo
     * @param cardPass
     * @param queryType 1卡密 2单码
     * @return
     */
    @GetMapping("/queryCard")
    @RateLimiter(limitType = LimitType.IP)
    public AjaxResult queryCard(@RequestParam("cardNo") String cardNo, @RequestParam(value = "cardPass", required = false) String cardPass, @RequestParam("queryType") int queryType) {
        if (queryType == 1) {
            SysCard sysCard = sysCardMapper.selectSysCardByCardNo(cardNo);
            if (sysCard == null) {
                throw new ServiceException("卡号或密码有误", 400);
            }
            if (!(StringUtils.isBlank(sysCard.getCardPass()) && StringUtils.isBlank(cardPass)) && !sysCard.getCardPass().equals(cardPass)) {
                throw new ServiceException("卡号或密码有误", 400);
            }
            return AjaxResult.success(new SysCardVo(sysCard));
        } else if (queryType == 2) {
            SysLoginCode loginCode = sysLoginCodeMapper.selectSysLoginCodeByCardNo(cardNo);
            if (loginCode == null) {
                throw new ServiceException("查询的登录码不存在", 400);
            }
            SysLoginCodeVo loginCodeVo = new SysLoginCodeVo(loginCode);
            if(UserConstants.YES.equals(loginCode.getIsCharged())) {
                SysAppUser appUser = appUserService.selectSysAppUserByAppIdAndLoginCode(loginCode.getAppId(), cardNo);
                if(appUser != null) {
                    loginCodeVo.setUserExpireTime(appUser.getExpireTime());
                }
            }
            return AjaxResult.success(loginCodeVo);
        }
        throw new ServiceException("未指定查询卡密类型", 400);
    }

    @GetMapping("/chargeCard")
    @RateLimiter(limitType = LimitType.IP)
    public AjaxResult chargeCard(@RequestParam("appId") long appId, @RequestParam("username") String username, @RequestParam(value = "password", required = false, defaultValue = "") String password,
                                 @RequestParam("validPassword") String validPassword, @RequestParam("cardNo") String cardNo, @RequestParam(value = "cardPass", required = false) String cardPass,
                                 @RequestParam("queryType") int queryType) {
        SysApp app = sysAppMapper.selectSysAppByAppId(appId);
        if (app == null) {
            throw new ServiceException("软件不存在");
        }
        Function func = null;
        if (queryType == 1) {
            if ("1".equals(validPassword) && StringUtils.isBlank(password)) {
                throw new ServiceException("您选择了校验密码但是未提供用户账户密码");
            }
            func = new RechargeCard();
            func.setApp(app);
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);
            params.put("validPassword", validPassword);
            params.put("cardNo", cardNo);
            params.put("cardPassword", cardPass);
            func.setParams(params);
        } else if (queryType == 2) {
            func = new RechargeLoginCode();
            func.setApp(app);
            Map<String, String> params = new HashMap<>();
            params.put("loginCode", username);
            params.put("newLoginCode", cardNo);
            func.setParams(params);
        }
        if (func == null) {
            throw new ServiceException("未指定充值续费类型", 400);
        }
        try {
            String result = func.handle().toString();
            if (app.getBillType() == BillType.TIME) {
                return AjaxResult.success("充值成功，新的到期时间为：" + result);
            } else if (app.getBillType() == BillType.POINT) {
                return AjaxResult.success("充值成功，新的点数余额为：" + result);
            } else {
                throw new ServiceException("获取软件计费方式出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    @GetMapping("/queryBindDevice")
    @RateLimiter(limitType = LimitType.IP)
    public AjaxResult queryBindDevice(@RequestParam("appId") long appId, @RequestParam("username") String username,
                                      @RequestParam(value = "password", required = false, defaultValue = "") String password, @RequestParam("queryType") int queryType) {
        SysApp app = sysAppMapper.selectSysAppByAppId(appId);
        if (app == null) {
            throw new ServiceException("软件不存在");
        }
        List<SysAppUserDeviceCode> deviceCodeList = new ArrayList<>();
        SysAppUser appUser = null;
        if (queryType == 1) {
            SysUser user = sysUserService.selectUserByUserName(username);
            if (user == null) {
                throw new ServiceException("账号不存在");
            }
            if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
                throw new ServiceException("账号或密码有误");
            }
            appUser = appUserService.selectSysAppUserByAppIdAndUserId(appId, user.getUserId());
            if (appUser == null) {
                throw new ServiceException("软件用户不存在");
            }
            deviceCodeList = sysAppUserDeviceCodeService.selectSysAppUserDeviceCodeByAppUserId(appUser.getAppUserId());
        } else if (queryType == 2) {
            appUser = appUserService.selectSysAppUserByAppIdAndLoginCode(appId, username);
            if (appUser == null) {
                throw new ServiceException("软件用户不存在");
            }
            deviceCodeList = sysAppUserDeviceCodeService.selectSysAppUserDeviceCodeByAppUserId(appUser.getAppUserId());
        }
        List<SysAppUserDeviceCodeVo> result = deviceCodeList.stream().map(item -> {
            item.setDeviceCode(sysDeviceCodeService.selectSysDeviceCodeByDeviceCodeId(item.getDeviceCodeId()));
            return new SysAppUserDeviceCodeVo(item);
        }).collect(Collectors.toList());
        assert appUser != null;
        return AjaxResult.success(result).put("billType", app.getBillType()).put("reduceQuotaUnbind", app.getReduceQuotaUnbind())
                .put("unbindTimes", appUser.getUnbindTimes()).put("enableUnbindByQuota", app.getEnableUnbindByQuota());
    }

    @GetMapping("/unbindDevice")
    @RateLimiter(limitType = LimitType.IP)
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult unbindDevice(@RequestParam("id") long id) {
        SysAppUserDeviceCode appUserDeviceCode = sysAppUserDeviceCodeService.selectSysAppUserDeviceCodeById(id);
        if (appUserDeviceCode == null) {
            throw new ServiceException("当前用户未绑定该设备码");
        }
        Long appUserId = appUserDeviceCode.getAppUserId();
        SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(appUserId);
        if (appUser == null) {
            throw new ServiceException("软件用户不存在");
        }
        SysApp app = sysAppMapper.selectSysAppByAppId(appUser.getAppId());
        if (app == null) {
            throw new ServiceException("软件不存在");
        }
        // 软件是否开启解绑
        Boolean enableUnbind = Convert.toBool(app.getEnableUnbind(), false);
        if (!enableUnbind) {
            throw new ServiceException("该软件解绑功能未开启");
        }
        // 卡类是否开启解绑
        if (appUser.getLastChargeTemplateId() != null) {
            if (app.getAuthType() == AuthType.ACCOUNT) {
                SysCardTemplate template = sysCardTemplateMapper.selectSysCardTemplateByTemplateId(appUser.getLastChargeTemplateId());
                if (template != null && UserConstants.NO.equals(template.getEnableUnbind())) {
                    throw new ApiException(ErrorCode.ERROR_CARD_UNBIND_NOT_ENABLE);
                }
            } else if (app.getAuthType() == AuthType.LOGIN_CODE) {
                SysLoginCodeTemplate template = sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateByTemplateId(appUser.getLastChargeTemplateId());
                if (template != null && UserConstants.NO.equals(template.getEnableUnbind())) {
                    throw new ApiException(ErrorCode.ERROR_CARD_UNBIND_NOT_ENABLE);
                }
            }
        }
        SysDeviceCode deviceCode = sysDeviceCodeService.selectSysDeviceCodeByDeviceCodeId(appUserDeviceCode.getDeviceCodeId());
        // 解绑日志
        SysUnbindLog unbindLog = new SysUnbindLog();
        unbindLog.setAppUserId(appUser.getAppUserId());
        unbindLog.setAppId(app.getAppId());
        unbindLog.setFirstLoginTime(appUserDeviceCode.getCreateTime());
        unbindLog.setLastLoginTime(appUserDeviceCode.getLastLoginTime());
        unbindLog.setLoginTimes(appUserDeviceCode.getLoginTimes());
        unbindLog.setUnbindType(UnbindType.FRONTEND_UNBIND);
        unbindLog.setUnbindDesc("用户前台解绑");
        unbindLog.setDeviceCode(deviceCode.getDeviceCode());
        unbindLog.setDeviceCodeId(appUserDeviceCode.getDeviceCodeId());
        unbindLog.setChangeAmount(0L);
        if (app.getBillType() == BillType.TIME) {
            unbindLog.setExpireTimeAfter(appUser.getExpireTime());
            unbindLog.setExpireTimeBefore(appUser.getExpireTime());
        }
        if (app.getBillType() == BillType.POINT) {
            unbindLog.setPointAfter(appUser.getPoint());
            unbindLog.setPointBefore(appUser.getPoint());
        }
        // 扣减解绑次数
        if (appUser.getUnbindTimes() > 0) {
            appUser.setUnbindTimes(appUser.getUnbindTimes() - 1);
            appUserService.updateSysAppUser(appUser);
        } else {
            if (Convert.toBool(app.getEnableUnbindByQuota(), true)) {
                Long p = app.getReduceQuotaUnbind();
                if (p != null && p > 0) {
                    boolean enableNegative = UserConstants.YES.equals(app.getEnableNegative());
                    SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
                    if (app.getBillType() == BillType.TIME) {
                        expireLog.setExpireTimeBefore(appUser.getExpireTime());
                        Date newExpiredTime = MyUtils.getNewExpiredTimeSub(appUser.getExpireTime(), p);
                        Date nowDate = DateUtils.getNowDate();
                        if ((appUser.getExpireTime().after(nowDate) && newExpiredTime.after(nowDate)) || enableNegative) {
                            appUser.setExpireTime(newExpiredTime);
                            appUserService.updateSysAppUser(appUser);
                            expireLog.setExpireTimeAfter(newExpiredTime);
                        } else {
                            throw new ServiceException("软件用户剩余时间不足");
                        }
                    } else if (app.getBillType() == BillType.POINT) {
                        expireLog.setPointBefore(appUser.getPoint());
                        BigDecimal point = BigDecimal.valueOf(p);
                        BigDecimal newPoint = appUser.getPoint().subtract(point);
                        if (appUser.getPoint().compareTo(point) >= 0 || enableNegative) {
                            appUser.setPoint(newPoint);
                            appUserService.updateSysAppUser(appUser);
                            expireLog.setPointAfter(newPoint);
                        } else {
                            throw new ServiceException("软件用户点数不足");
                        }
                    }
                    // 记录用户时长变更日志
                    expireLog.setAppUserId(appUser.getAppUserId());
                    expireLog.setTemplateId(null);
                    expireLog.setCardId(null);
                    expireLog.setChangeDesc("前台解绑");
                    expireLog.setChangeType(AppUserExpireChangeType.UNBIND);
                    expireLog.setChangeAmount(-p);
                    expireLog.setCardNo(null);
                    expireLog.setAppId(app.getAppId());
                    AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
                    // 记录解绑日志
                    unbindLog.setChangeAmount(-p);
                    unbindLog.setExpireTimeAfter(expireLog.getExpireTimeAfter());
                    unbindLog.setExpireTimeBefore(expireLog.getExpireTimeBefore());
                    unbindLog.setPointAfter(expireLog.getPointAfter());
                    unbindLog.setPointBefore(expireLog.getPointBefore());
                }
            } else {
                throw new ServiceException("解绑次数已用尽，无法继续解绑");
            }
        }
        sysAppUserDeviceCodeService.deleteSysAppUserDeviceCodeById(id);
        AsyncManager.me().execute(AsyncFactory.recordDeviceUnbind(unbindLog));
        return AjaxResult.success();
    }

    @GetMapping("/getNavInfo")
    @RateLimiter(limitType = LimitType.IP)
    public AjaxResult getNavInfo() {
        SysNavigation nav = new SysNavigation();
        nav.setVisible(UserConstants.NORMAL);
        List<SysNavigation> navList = sysNavigationService.selectSysNavigationList(nav);
        List<NavVo> navVoList = new ArrayList<>();
        for (SysNavigation item : navList) {
            NavVo vo = new NavVo();
            NavMetaVo mvo = new NavMetaVo();
            mvo.setTitle(item.getNavName());
            vo.setMeta(mvo);
            vo.setPath(item.getPath());
            vo.setHidden(false);
            vo.setId(item.getNavId());
            vo.setParentId(item.getParentId());
            vo.setIndex(UserConstants.YES.equals(item.getIsIndex()));
            navVoList.add(vo);
        }
        List<NavVo> tree = TreeUtil.build(navVoList);
        return AjaxResult.success(tree);
    }

}

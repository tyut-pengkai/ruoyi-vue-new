package com.easycode.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.easycode.cloud.domain.*;
import com.easycode.cloud.domain.dto.PurchaseDto;
import com.easycode.cloud.domain.dto.PurchaseOrderDetailRawDto;
import com.easycode.cloud.domain.vo.PurchaseVo;
import com.easycode.cloud.domain.vo.WmsMaterialBasicVo;
import com.easycode.cloud.mapper.*;
import com.easycode.cloud.service.IPurchaseService;
import com.easycode.cloud.service.IWmsPurchaseOrderPlanqtyRecordService;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.text.Convert;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.SpringUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.PageDomain;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.core.web.page.TableSupport;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.*;
import com.easycode.cloud.constants.PlanqtyRecordEnum;
import com.weifu.cloud.domain.*;
import com.weifu.cloud.domain.dto.*;
import com.weifu.cloud.domain.vo.TaskInfoVo;
import com.weifu.cloud.domian.ExchangeOrder;
import com.weifu.cloud.domian.GoodsSourceDef;
import com.weifu.cloud.domian.dto.MaterialUnitDefDto;
import com.weifu.cloud.domian.vo.PopupBoxVo;
import com.weifu.cloud.mapper.*;
import com.weifu.cloud.service.*;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.weifu.cloud.util.PopupBoxUtils.processingFormat;

/**
 * 采购单据处理service层
 *
 * @author hbh
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PurchaseServiceImpl implements IPurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    @Autowired
    private StockInStdOrderDetailMapper stockInStdOrderDetailMapper;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private StockInStdOrderMapper stockInStdOrderMapper;

    @Autowired
    private PurchaseOrderDetailMapper purchaseOrderDetailMapper;

    @Autowired
    private WmsPurchaseOrderRawMapper purchaseOrderRawMapper;

    @Autowired
    private WmsPurchaseOrderDetailRawMapper purchaseOrderDetailRawMapper;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private PurchaseOrderDetailPlanMapper purchaseOrderDetailPlanMapper;

    @Autowired
    private DeliveryOrderMapper deliveryOrderMapper;

    @Resource
    private IWmsPurchaseOrderPlanqtyRecordService wmsPurchaseRecordService;

    @Autowired
    private IMainDataService mainDataService;
    @Autowired
    private EsbSendCommonMapper esbSendCommonMapper;

    @Autowired
    private RemoteEsbSendService remoteEsbSendService;

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private ICodeRuleService iCodeRuleService;

    @Autowired
    private IStockOutService stockOutService;

    @Autowired
    private StockInItemBomMapper stockInItemBomMapper;

    @Override
    public List<PurchaseDto> selectWmsPurchaseOrderList(PurchaseVo purchaseVo) {
        return purchaseMapper.selectWmsPurchaseOrderList(purchaseVo);
    }

    /**
     * 查询采购单明细
     *
     * @param id 采购单明细主键
     * @return 采购单明细
     */
    @Override
    public PurchaseOrderDetail selectWmsPurchaseOrderDetailById(Long id) {
        return purchaseMapper.selectWmsPurchaseOrderDetailById(id);
    }

    /**
     * 新增采购单明细
     *
     * @param wmsPurchaseOrderDetail
     * @return 采购单明细
     */
    @Override
    public int insertWmsPurchaseOrderDetail(PurchaseOrderDetail wmsPurchaseOrderDetail) {
        wmsPurchaseOrderDetail.setCreateTime(DateUtils.getNowDate());
        wmsPurchaseOrderDetail.setCreateBy(SecurityUtils.getUsername());
        return purchaseMapper.insertWmsPurchaseOrderDetail(wmsPurchaseOrderDetail);
    }

    /**
     * 修改采购单明细
     *
     * @param wmsPurchaseOrderDetail
     * @return 采购单明细
     */
    @Override
    public int updateWmsPurchaseOrderDetail(PurchaseOrderDetail wmsPurchaseOrderDetail) {
        wmsPurchaseOrderDetail.setUpdateTime(DateUtils.getNowDate());
        wmsPurchaseOrderDetail.setUpdateBy(SecurityUtils.getUsername());
        return purchaseMapper.updateWmsPurchaseOrderDetail(wmsPurchaseOrderDetail);
    }

    /**
     * 批量删除采购单明细
     *
     * @param ids
     * @return 采购单明细
     */
    @Override
    public int deleteWmsPurchaseOrderDetailByIds(Long[] ids) {
        return purchaseMapper.deleteWmsPurchaseOrderDetailByIds(ids);
    }

    /**
     * 删除采购单明细
     *
     * @param id
     * @return 采购单明细
     */
    @Override
    public int deleteWmsPurchaseOrderDetailById(Long id) {
        return purchaseMapper.deleteWmsPurchaseOrderDetailById(id);
    }

    /**
     * 根据采购单号和采购单行号 获取采购单明细
     *
     * @param purchaseOrderDetailDto
     * @return
     */
    @Override
    public List<PurchaseOrderDetail> getPurchaseOrderDetailByNoAndLine(PurchaseOrderDetailDto purchaseOrderDetailDto) {
        List<PurchaseOrderDetail> list = purchaseOrderDetailDto.getPurchaseOrderDetailList();
        if (ObjectUtils.isEmpty(list)) {
            throw new ServiceException("数据不存在！");
        }
        return purchaseMapper.selectPurchaseOrderDetailByNoAndLine(list);
    }

    @Override
    public int updatePurchaseOrderDetail(List<PurchaseOrderDetail> purchaseOrderDetailList) {
        if (ObjectUtils.isEmpty(purchaseOrderDetailList)) {
            throw new ServiceException("数据不存在！");
        }
        purchaseOrderDetailList.forEach(purchaseOrderDetail -> {
            if (purchaseMapper.updateWmsPurchaseOrderDetail(purchaseOrderDetail) <= 0) {
                throw new ServiceException("修改采购单明细失败！");
            }
        });
        return 1;
    }

    /**
     * 回填采购明细收货数量(收货)
     *
     * @param purchaseOrderDetailDto 采购单明细dto
     * @return 结果
     */
    @Override
    @GlobalTransactional
    public int updatePurchaseReceivedQty(PurchaseOrderDetailDto purchaseOrderDetailDto) {
        PurchaseOrderDetail orderDetail = new PurchaseOrderDetail();
        orderDetail.setPurchaseOrderNo(purchaseOrderDetailDto.getPurchaseOrderNo());
        orderDetail.setPurchaseLineNo(purchaseOrderDetailDto.getPurchaseLineNo());
        orderDetail.setDelFlag(CommonYesOrNo.NO);
        List<PurchaseOrderDetail> purchaseOrderDetails = purchaseMapper.selectOrderDetail(orderDetail);
        if (ObjectUtils.isEmpty(purchaseOrderDetails)) {
            throw new ServiceException("采购单明细不存在");
        }
        // 收货数量
        BigDecimal receivedQty = purchaseOrderDetailDto.getReceivedQty();
        // 为采购协议时 需要逐一写入收货数量
        if (StockInOrderConstant.ORDER_TYPE_AGREEMENT.equals(purchaseOrderDetailDto.getDetailType())) {
            // 根据采购单号和采购单行号 获取采购单计划行
            PurchaseOrderDetailPlan detailPlan = new PurchaseOrderDetailPlan();
            detailPlan.setPurchaseOrderNo(purchaseOrderDetailDto.getPurchaseOrderNo());
            detailPlan.setPurchaseLineNo(purchaseOrderDetailDto.getPurchaseLineNo());
            detailPlan.setDelFlag(CommonYesOrNo.NO);
            List<PurchaseOrderDetailPlan> planList = purchaseOrderDetailPlanMapper.selectPurchaseOrderDetailPlanList(detailPlan);
            //过滤已完成收货的计划行并根据计划交货日期 正序排序(日期从小到大)
            List<PurchaseOrderDetailPlan> collect = planList.stream()
                    .filter(p -> p.getPlanQty().compareTo(p.getSapReceivedQty()) > 0)
                    .sorted(Comparator.comparing(PurchaseOrderDetailPlan::getDeliveryDate, Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(PurchaseOrderDetailPlan::getDeliveryLineNo,Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
            for (PurchaseOrderDetailPlan plan : collect) {
                //制单数
                BigDecimal madeQty = plan.getMadeQty();
                //需求数量
                BigDecimal planQty = plan.getPlanQty();
                //sap已收货数量
                BigDecimal sapReceivedQty = plan.getSapReceivedQty();
                //实际可以收数量 需求数量小于制单数量时 按照需求数量收货（此情况仅可能出现在已建单后，sap把计划行需求数量改小了）
                BigDecimal receivableQty = planQty.compareTo(madeQty) < 0 ? planQty.subtract(sapReceivedQty) : madeQty.subtract(sapReceivedQty);
                if (receivedQty.compareTo(receivableQty) > 0){
                    plan.setSapReceivedQty(sapReceivedQty.add(receivableQty));
                    receivedQty = receivedQty.subtract(receivableQty);
                } else {
                    plan.setSapReceivedQty(sapReceivedQty.add(receivedQty));
                    receivedQty = receivedQty.subtract(BigDecimal.ZERO);
                }
                PurchaseOrderDetailPlan orderDetailPlan = new PurchaseOrderDetailPlan();
                orderDetailPlan.setId(plan.getId());
                orderDetailPlan.setSapReceivedQty(plan.getSapReceivedQty());
                orderDetailPlan.setUpdateTime(DateUtils.getNowDate());
                orderDetailPlan.setUpdateBy(SecurityUtils.getUsername());
                if (purchaseOrderDetailPlanMapper.updatePurchaseOrderDetailPlan(orderDetailPlan) <= 0){
                    throw new ServiceException("更新采购单收货数量失败！");
                }
                if (receivedQty.compareTo(BigDecimal.ZERO) == 0){
                    break;
                }
            }
        }

        //非采购协议
        PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetails.get(0);
        //累计wms收货数量
        BigDecimal wmsReceivedQty = purchaseOrderDetail.getWmsReceivedQty().add(receivedQty);
        //累计sap收货数量
        BigDecimal sapReceivedQty = purchaseOrderDetail.getSapReceivedQty().add(receivedQty);

        purchaseOrderDetail.setWmsReceivedQty(wmsReceivedQty);
        purchaseOrderDetail.setSapReceivedQty(sapReceivedQty);
        purchaseOrderDetail.setUpdateBy(SecurityUtils.getUsername());
        purchaseOrderDetail.setUpdateTime(DateUtils.getNowDate());
        if (purchaseMapper.updateWmsPurchaseOrderDetail(purchaseOrderDetail) <= 0) {
            throw new ServiceException("更新采购单收货数量失败！");
        }

        // 更新需求数量、收货数量记录
        WmsPurchaseOrderPlanqtyRecord record = new WmsPurchaseOrderPlanqtyRecord();
        record.setMaterialNo(purchaseOrderDetail.getMaterialNo());
        record.setPurchaseOrderNo(purchaseOrderDetail.getPurchaseOrderNo());
        record.setPurchaseLineNo(purchaseOrderDetail.getPurchaseLineNo());
        record.setChangeType(PlanqtyRecordEnum.TYPE_WMS_FINISH_RECEIVING.getKey());
        record.setDeliveryQty(Double.parseDouble(Convert.toStr(purchaseOrderDetailDto.getReceivedQty())));
        record.setSupplierDeliveryQty(Double.parseDouble(Convert.toStr(purchaseOrderDetailDto.getReceivedQty())));
        wmsPurchaseRecordService.commonSave(record);
        return 1;
    }

    /**
     * 更新采购单wms收货数量、sap收货数量(退货)
     *
     * @param purchaseOrderDetailDto 采购单明细dto
     * @return 结果
     */
    @Override
    public int updatePurchaseReturnQty(PurchaseOrderDetailDto purchaseOrderDetailDto) {
        PurchaseOrderDetail orderDetail = new PurchaseOrderDetail();
        orderDetail.setPurchaseOrderNo(purchaseOrderDetailDto.getPurchaseOrderNo());
        orderDetail.setPurchaseLineNo(purchaseOrderDetailDto.getPurchaseLineNo());
        orderDetail.setDelFlag(CommonYesOrNo.NO);
        List<PurchaseOrderDetail> purchaseOrderDetails = purchaseMapper.selectOrderDetail(orderDetail);
        if (ObjectUtils.isEmpty(purchaseOrderDetails)) {
            throw new ServiceException("采购单明细不存在");
        }
        // 质检退货调用此方法，wms项目已删除vendor模块，所以在此处补充逻辑
        // 如果没有采购单类型参数，那么根据采购单号和行号再查询对应采购单类型
        if (!Optional.ofNullable(purchaseOrderDetailDto.getDetailType()).isPresent()) {
            PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetails.get(0);
            // 根据采购单明细中的采购单id查询采购单信息
            PurchaseOrder purchaseOrder = purchaseOrderMapper.selectPurchaseOrderById(purchaseOrderDetail.getPurchaseOrderId());
            purchaseOrderDetailDto.setDetailType(purchaseOrder.getOrderType());
        }
        // 退货数量
        BigDecimal returnQty = purchaseOrderDetailDto.getReturnQty();
        logger.info("原材料退货数量:{}", returnQty);
        // 为采购协议时 需要逐一扣减收货数量
        if (StockInOrderConstant.ORDER_TYPE_AGREEMENT.equals(purchaseOrderDetailDto.getDetailType())) {
            // 根据采购单号和采购单行号 获取采购单计划行
            PurchaseOrderDetailPlan purchaseOrderDetailPlan = new PurchaseOrderDetailPlan();
            purchaseOrderDetailPlan.setPurchaseOrderNo(purchaseOrderDetailDto.getPurchaseOrderNo());
            purchaseOrderDetailPlan.setPurchaseLineNo(purchaseOrderDetailDto.getPurchaseLineNo());
            purchaseOrderDetailPlan.setDelFlag(CommonYesOrNo.NO);
            List<PurchaseOrderDetailPlan> planList = purchaseOrderDetailPlanMapper.selectPurchaseOrderDetailPlanList(purchaseOrderDetailPlan);
            //过滤未完成收货的计划行并根据计划交货日期 反序排序(日期从大到小)
            List<PurchaseOrderDetailPlan> collect = planList.stream()
                    .filter(p -> p.getSapReceivedQty().compareTo(BigDecimal.ZERO) > 0)
                    .sorted(Comparator.comparing(PurchaseOrderDetailPlan::getDeliveryDate, Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(PurchaseOrderDetailPlan::getDeliveryLineNo,Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
            for (PurchaseOrderDetailPlan plan : collect) {
                //制单数
                BigDecimal madeQty = plan.getMadeQty();
                //sap已收货数量
                BigDecimal sapReceivedQty = plan.getSapReceivedQty();
                if (returnQty.compareTo(sapReceivedQty) > 0){
                    plan.setMadeQty(madeQty.subtract(sapReceivedQty));
                    plan.setSapReceivedQty(BigDecimal.ZERO);
                    returnQty = returnQty.subtract(sapReceivedQty);
                } else {
                    plan.setMadeQty(madeQty.subtract(returnQty));
                    plan.setSapReceivedQty(sapReceivedQty.add(returnQty));
                    returnQty = returnQty.subtract(BigDecimal.ZERO);
                }
                PurchaseOrderDetailPlan orderDetailPlan = new PurchaseOrderDetailPlan();
                orderDetailPlan.setId(plan.getId());
                orderDetailPlan.setSapReceivedQty(plan.getSapReceivedQty());
                orderDetailPlan.setUpdateTime(DateUtils.getNowDate());
                orderDetailPlan.setUpdateBy(SecurityUtils.getUsername());
                if (purchaseOrderDetailPlanMapper.updatePurchaseOrderDetailPlan(orderDetailPlan) <= 0){
                    throw new ServiceException("更新采购单收货数量失败！");
                }
                if (returnQty.compareTo(BigDecimal.ZERO) == 0){
                    break;
                }
            }
        }
        //非采购协议
        PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetails.get(0);
        //wms总需求数量
        BigDecimal totalPlanQty = purchaseOrderDetail.getTotalPlanQty();
        //wms收货数量 = 原wms收货数量 - 退货数量 >=0
        BigDecimal wmsReceivedQty = purchaseOrderDetail.getWmsReceivedQty().subtract(returnQty);
        //sap收货数量 = 原wms收货数量 - 退货数量 >= sap自收数量
        BigDecimal sapReceivedQty = purchaseOrderDetail.getSapReceivedQty().subtract(returnQty);
        //已制单数量 = 原已制单数量 - 退货数量 >=0
        BigDecimal madeQty = purchaseOrderDetail.getMadeQty().subtract(returnQty);
        //需求数量 = wms总需求数量 - 已制单数量
        BigDecimal planQty = totalPlanQty.subtract(madeQty);
        purchaseOrderDetail.setMadeQty(madeQty);
        purchaseOrderDetail.setWmsReceivedQty(wmsReceivedQty);
        purchaseOrderDetail.setSapReceivedQty(sapReceivedQty);
        purchaseOrderDetail.setPlanQty(planQty);
        purchaseOrderDetail.setUpdateBy(SecurityUtils.getUsername());
        purchaseOrderDetail.setUpdateTime(DateUtils.getNowDate());
        if (purchaseMapper.updateWmsPurchaseOrderDetail(purchaseOrderDetail) <= 0) {
            throw new ServiceException("更新采购单收货数量失败！");
        }
        return 1;
    }

    /**
     * 采购单列表开窗查询
     *
     * @param popupBoxVo
     * @return
     */
    @Override
    public List<PurchaseOrder> openQueryPurchaseOrder(PopupBoxVo popupBoxVo) {
        Map<String, Object> paramsMap = processingFormat(popupBoxVo);
        return purchaseMapper.openQueryPurchaseOrder(paramsMap);
    }

    /**
     * 采购单处理详情查询（除去采购协议）
     *
     * @param purchaseOrderDetail
     * @return
     */
    @Override
    public List<PurchaseOrderDetail> selectPurchaseOrderDetailList(PurchaseOrderDetail purchaseOrderDetail) {
        return purchaseMapper.selectPurchaseOrderDetail(purchaseOrderDetail);
    }

    @Override
    public int updatePurchaseOrderDetailBatch(List<PurchaseOrderDetail> list) {
        if (ObjectUtils.isEmpty(list)){
            throw new ServiceException("参数不存在！");
        }

        List<String> orderNoList = list.stream().map(PurchaseOrderDetail::getPurchaseOrderNo).distinct().collect(Collectors.toList());

        // 查询采购单类型
        List<PurchaseOrder> purchaseOrders = purchaseOrderMapper.selectOrderByOrderNo(orderNoList);

        Map<String, String> map = purchaseOrders.stream().collect(Collectors.toMap(PurchaseOrder::getOrderNo, PurchaseOrder::getOrderType, (k1, k2) -> k1));

        for (PurchaseOrderDetail item : list) {
            // 计算采购单明细数量 总需求数量、已制单数量、需求数量
            // 采购单据号
            String purchaseOrderNo = item.getPurchaseOrderNo();
            // 采购单据行号
            String purchaseLineNo = item.getPurchaseLineNo();
            // 未收数量
            BigDecimal unReceivedQty = item.getMadeQty();
            // 送货数量无变动时 直接返回
            if (unReceivedQty.compareTo(BigDecimal.ZERO) == 0){
                continue;
            }
            // 根据采购单号和采购单行号 可以确定唯一的采购明细
            PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
            purchaseOrderDetail.setPurchaseOrderNo(purchaseOrderNo);
            purchaseOrderDetail.setPurchaseLineNo(purchaseLineNo);
            purchaseOrderDetail.setDelFlag(CommonYesOrNo.NO);
            List<PurchaseOrderDetail> purchaseOrderDetailList = purchaseMapper.selectPurchaseOrderDetail(purchaseOrderDetail);
            if (ObjectUtils.isEmpty(purchaseOrderDetailList)) {
                throw new ServiceException("采购单明细不存在！");
            }

            PurchaseOrderDetail detail = purchaseOrderDetailList.get(0);

            // 总需求数量
            BigDecimal totalPlanQty = detail.getTotalPlanQty();
            // 已制单数量
            BigDecimal madeQty = detail.getMadeQty();
            if (unReceivedQty.compareTo(madeQty) >= 0) {
                // 送货数量>=已制单数量时
                madeQty = BigDecimal.ZERO;
            } else {
                // 送货数量<已制单数量时
                madeQty = madeQty.subtract(unReceivedQty);
            }
            //更新采购单明细需求数量与已制单数量
            SpringUtils.getBean(DeliveryOrderServiceImpl.class)
                    .updatePurchaseOrderDetailQty(detail.getId(), totalPlanQty, madeQty);

            // 如果是采购协议的话，需要修改plan的数量
            if (StockInOrderConstant.ORDER_TYPE_AGREEMENT.equals(map.get(purchaseOrderNo))){
                // 根据采购单号和采购单行号 获取采购单计划行
                PurchaseOrderDetailPlan purchaseOrderDetailPlan = new PurchaseOrderDetailPlan();
                purchaseOrderDetailPlan.setPurchaseOrderNo(purchaseOrderNo);
                purchaseOrderDetailPlan.setPurchaseLineNo(purchaseLineNo);
                purchaseOrderDetailPlan.setDelFlag(CommonYesOrNo.NO);
                List<PurchaseOrderDetailPlan> planList = purchaseOrderDetailPlanMapper.selectPurchaseOrderDetailPlanList(purchaseOrderDetailPlan);
                if (ObjectUtils.isEmpty(planList)) {
                    throw new ServiceException("采购单计划行不存在！");
                }
                //减少已制单数量使用集合  过滤物已制单数量的的计划行 并根据计划交货日期 倒序排序 计划交货日期相同时 根据计划行号 倒序排序
                List<PurchaseOrderDetailPlan> deduct = planList.stream()
                        .filter(p ->p.getMadeQty().compareTo(BigDecimal.ZERO) > 0)
                        .sorted(Comparator.comparing(PurchaseOrderDetailPlan::getDeliveryDate, Comparator.nullsLast(Comparator.reverseOrder()))
                                .thenComparing(PurchaseOrderDetailPlan::getDeliveryLineNo,Comparator.nullsLast(Comparator.reverseOrder())))
                        .collect(Collectors.toList());
                //更新采购单计划已制单数量
                for (PurchaseOrderDetailPlan plan : deduct) {
                    if (unReceivedQty.compareTo(plan.getMadeQty()) > 0) {
                        //未收数量>=已制单数量时
                        unReceivedQty = unReceivedQty.subtract(plan.getMadeQty());
                        plan.setMadeQty(BigDecimal.ZERO);
                    } else {
                        //未收数量<已制单数量时
                        plan.setMadeQty(plan.getMadeQty().subtract(unReceivedQty));
                        unReceivedQty = BigDecimal.ZERO;
                    }
                    PurchaseOrderDetailPlan orderDetailPlan = new PurchaseOrderDetailPlan();
                    orderDetailPlan.setId(plan.getId());
                    orderDetailPlan.setMadeQty(plan.getMadeQty());
                    orderDetailPlan.setUpdateBy(SecurityUtils.getUsername());
                    orderDetailPlan.setUpdateTime(DateUtils.getNowDate());
                    purchaseOrderDetailPlanMapper.updatePurchaseOrderDetailPlan(orderDetailPlan);
                    if (unReceivedQty.compareTo(BigDecimal.ZERO) == 0){
                        break;
                    }
                }
            }
        }
        // 激活/部分完成 关闭 更新需求/收货数量
        wmsPurchaseRecordService.closeTaskUpdateReceiveQty(list);
        return 1;
    }

    /**
     * 获取wms 已经制单但未收货的数量
     *
     * @param purchaseOrderNo 采购单号
     * @param lineNo          采购单行号
     * @return 已经制单但未收货的数量
     */
    @Override
    public BigDecimal getPurchaseWmsUnmadeQty(String purchaseOrderNo, Long lineNo) {
        // 已经制单但未收货的数量  = 送货单新增数量(新建状态) + 已经激活未完成的数量
        BigDecimal madeQty = purchaseMapper.getPurchaseWmsMmadeUnActiveQty(purchaseOrderNo, lineNo);
        // 获取已经激活未完成的数量
        BigDecimal activeQty = purchaseMapper.getPurchaseActiveUnReceivedQty(purchaseOrderNo, lineNo);
        return madeQty.add(activeQty);
    }

    /**
     * 根据采购订单号查询采购订单明细
     * @param purchaseVo 采购订单Vo
     * @date 2024/05/22
     * @author fsc
     * @return 采购订单明细集合
     */
    @Override
    public List<PurchaseDto> queryPurchaseDetailList(PurchaseVo purchaseVo) {
        return purchaseMapper.selectPurchaseOrderList(purchaseVo);
    }

    /**
     * 查询采购单据处理列表
     *
     * @param purchaseVo
     * @return
     */
    @Override
    public TableDataInfo selectWmsPurchaseOrderListShow(PurchaseVo purchaseVo) {
        if (ObjectUtils.isEmpty(purchaseVo)){
            throw new ServiceException("参数不存在!");
        }
        List<PurchaseDto> list = purchaseMapper.selectWmsPurchaseOrderList(purchaseVo);
        //查询物料货源信息
        GoodsSourceDef sourceDef = new GoodsSourceDef();
        // 根据用户所带工厂code查找
        sourceDef.setStatus("0");
        AjaxResult ajaxResult = mainDataService.selectGoodsSourceDefList(sourceDef);
        if (ajaxResult.isError()){
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<GoodsSourceDef> goodsSourceDefList = JSONObject.parseArray(ajaxResult.get("data").toString(), GoodsSourceDef.class);

        //根据物料货源信息过滤
        List<PurchaseDto> resultList = list.stream().map(p -> {
            p.setIsFilter(CommonYesOrNo.NO);
            goodsSourceDefList.stream().filter(g-> g.getSupplierPeriod().compareTo(new BigDecimal(0)) > 0)
                    .filter(g -> StringUtils.equals(p.getSupplierCode(), g.getSupplierCode()) && StringUtils.equals(p.getMaterialNo(), g.getMaterialNo()))
                    .forEach(g -> {
                        //当前时间 年月日
                        Date date = DateUtils.parseDate(DateUtils.getDate());
                        //时间计算
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);//设置需要计算的时间
                        calendar.add(Calendar.DATE, g.getSupplierPeriod().intValue());//+供应商时间
                        Date newDate = calendar.getTime();//获取计算结果 当前时间+供应商时间
                        //时间大小比较 当前时间 > 送货时间 ||  当前时间+供应商时间 <= 送货时间 添加过滤标识
                        if (date.compareTo(p.getDeliveryDate()) > 0 || newDate.compareTo(p.getDeliveryDate()) <= 0) {
                            p.setIsFilter(CommonYesOrNo.YES);
                        }
                    });
            return p;
        }).filter(purchaseDto -> CommonYesOrNo.NO.equals(purchaseDto.getIsFilter())).collect(Collectors.toList());
        //手动分页
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        int total = resultList.size();
        if (total > pageSize) {
            int toIndex = pageSize * pageNum;
            if (toIndex > total) {
                toIndex = total;
            }
            resultList = resultList.subList(pageSize * (pageNum - 1), toIndex);
        }

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setRows(resultList);
        rspData.setMsg("查询成功");
        rspData.setTotal(total);
        return rspData;
    }




    /**
     * 同步临时表
     * @param headList
     * @param itemList
     * @return
     */
    private int syncSaleDeliverOrderRaw(List<Map<String, Object>> headList, List<Map<String, Object>> itemList) {
        // head只有一个
        Map<String, Object> headInfo = headList.get(0);
        Long tenantId = 6L;
        // A：新增 B：修改
        // 操作标识
        String opFlag = Convert.toStr(headInfo.get("OP_FLAG"));
        // 交货编号
        String vbeln = Convert.toStr(headInfo.get("VBELN"));
        // 销售组织 工厂? 供应商?
        String vkorg = Convert.toStr(headInfo.get("VKORG"));
        // 创建对象的人员名称
        String ernam = Convert.toStr(headInfo.get("ERNAM"));
        // 记录创建日期
        String erdat = Convert.toStr(headInfo.get("ERDAT"));
        // 交货类型
        String lfart = Convert.toStr(headInfo.get("LFART"));
        // 客户编号
        String kunnr = Convert.toStr(headInfo.get("KUNNR"));
        // 名称
        String name1 = Convert.toStr(headInfo.get("NAME1"));
        // 装运类型
        String vsart = Convert.toStr(headInfo.get("VSART"));
        // 计划货物移动日期
        String wadat = Convert.toStr(headInfo.get("WADAT"));
        // 交货日期
        String lfdat = Convert.toStr(headInfo.get("LFDAT"));

        WmsPurchaseOrderRaw purchaseOrder = new WmsPurchaseOrderRaw();
        purchaseOrder.setOrderNo(vbeln);
        purchaseOrder.setOrderStatus(OrderStatusConstant.ORDER_STATUS_NEW);
        purchaseOrder.setOrderType(OrderConstant.PURCHASE);
        purchaseOrder.setBuyer(kunnr);
        purchaseOrder.setRemark(name1);
        purchaseOrder.setDeliverDate(DateUtils.parseDate(lfdat));
        purchaseOrder.setProcessDate(DateUtils.parseDate(wadat));
        purchaseOrder.setTenantId(tenantId);
        purchaseOrder.setCreateBy(ernam);
        purchaseOrder.setCreateTime(DateUtils.parseDate(erdat));
        purchaseOrder.setUpdateBy(ernam);
        purchaseOrder.setUpdateTime(DateUtils.getNowDate());
//        purchaseOrder.setCompanyId(factoryInfo.get(0).getId());
//        purchaseOrder.setCompanyCode(factoryInfo.get(0).getFactoryCode());
//        purchaseOrder.setCompanyName(factoryInfo.get(0).getFactoryName());
        purchaseOrder.setBillType(lfart);

        PurchaseVo vo = new PurchaseVo();
        vo.setPurchaseOrderNo(vbeln);
        List<PurchaseDto> purchaseList = purchaseMapper.selectWmsPurchaseOrderList(vo);

//        if(StringUtils.equals("A", opFlag) && CollectionUtils.isEmpty(purchaseList)) {
//            purchaseMapper.insertPurchaseOrder(purchaseOrder);
//        } else if("A".equals(opFlag) && !ObjectUtils.isEmpty(purchaseList)) {
//            purchaseOrder.setId(purchaseList.get(0).getId());
//            purchaseOrderMapper.updatePurchaseOrder(purchaseOrder);
//            purchaseMapper.deleteWmsPurchaseOrderDetailByOrderNo(vbeln);
//        } else if("B".equals(opFlag)) {
//            purchaseMapper.insertPurchaseOrder(purchaseOrder);
//        }
        // A新增  B修改
        if (StringUtils.equals("A", opFlag) && CollectionUtils.isEmpty(purchaseList)) {
            purchaseOrderRawMapper.insertWmsPurchaseOrderRaw(purchaseOrder);
        } else if(StringUtils.equals("A", opFlag) && !CollectionUtils.isEmpty(purchaseList)) {
            purchaseOrder.setId(purchaseList.get(0).getId());
            purchaseOrderRawMapper.updateWmsPurchaseOrderRaw(purchaseOrder);
            purchaseOrderRawMapper.deleteWmsPurchaseOrderRawByOrderNo(vbeln);
        } else if("B".equals(opFlag)) {
            purchaseOrderRawMapper.insertWmsPurchaseOrderRaw(purchaseOrder);
        }
        List<WmsPurchaseOrderDetailRaw> detailList = new ArrayList<>();

        for (int i = 0; i < itemList.size(); i++) {

            Map<String, Object> itemMap = itemList.get(i);
            // 交货
            String vbeln2 = Convert.toStr(itemMap.get("VBELN"));
            // 交货项目
            String posnr = Convert.toStr(Convert.toStr(itemMap.get("POSNR")));
            // 销售凭证项目类别
            String pstyv = Convert.toStr(itemMap.get("PSTYV"));
            // 物料编号
            String matnr = Convert.toStr(itemMap.get("MATNR"));
            // 物料描述
            String maktx = Convert.toStr(itemMap.get("MAKTX"));
            // 交货数量
            String lfimg = Convert.toStr(itemMap.get("LFIMG"));
            // 基本计量单位
            String meins = Convert.toStr(itemMap.get("MEINS"));
            // 拣配状态
            String kostk = Convert.toStr(itemMap.get("KOSTK"));
            // 批次编号
            String charg = Convert.toStr(itemMap.get("CHARG"));
            // 工厂
            String werks = Convert.toStr(itemMap.get("WERKS"));
            // 存储地点
            String lgort = Convert.toStr(itemMap.get("LGORT"));
            // 移动类型
            String bwart = Convert.toStr(itemMap.get("BWART"));
            // 参考单据的单据编号
            String vgbel = Convert.toStr(itemMap.get("VGBEL"));
            // 参考项目的项目号
            String vgpos = Convert.toStr(itemMap.get("VGPOS"));
            // 库存类型
            String insmk = Convert.toStr(itemMap.get("INSMK"));


            List<FactoryCommonDto> factoryInfo = esbSendCommonMapper.getFactoryByCode(werks);
            if (factoryInfo.size() <= 0){
                throw new ServiceException(String.format("根据工厂code:%s未查询到相关信息", werks));
            }
            List<String> noList = new ArrayList<>();
            itemList.forEach(item -> noList.add(matnr));
            MaterialUnitDefDto materialUnitDefDto = new MaterialUnitDefDto();
            materialUnitDefDto.setMaterialNoList(noList);
            materialUnitDefDto.setStockoutEnable(CommonYesOrNo.YES);
            materialUnitDefDto.setFactoryCode(werks);
//            AjaxResult unitResult = mainDataService.batchMaterialUnitDef(materialUnitDefDto);
//            if (unitResult.isError()) {
//                throw new ServiceException(unitResult.get("msg").toString());
//            }
//            List<MaterialUnitDefDto> unitList = JSON.parseArray(unitResult.get("data").toString(), MaterialUnitDefDto.class);
//            if (ObjectUtils.isEmpty(unitList)) {
//                throw new ServiceException(String.format("不存在出库使用单位，请维护相关数据"));
//            }
            Long purchaseOrderId = purchaseOrder.getId();
            WmsPurchaseOrderDetailRaw purchaseOrderDetail = new WmsPurchaseOrderDetailRaw();
            purchaseOrderDetail.setPurchaseOrderId(purchaseOrderId);
            purchaseOrderDetail.setPurchaseOrderNo(vbeln2);
            purchaseOrderDetail.setFactoryId(factoryInfo.get(0).getId());
            purchaseOrderDetail.setFactoryName(factoryInfo.get(0).getFactoryName());
            purchaseOrderDetail.setFactoryCode(factoryInfo.get(0).getFactoryCode());
            purchaseOrderDetail.setMaterialNo(matnr);
            purchaseOrderDetail.setMaterialName(maktx);
            if(!ObjectUtils.isEmpty(lfimg)) {
                purchaseOrderDetail.setPlanQty(new BigDecimal(lfimg.trim()));
                purchaseOrderDetail.setTotalPlanQty(new BigDecimal(lfimg.trim()));
            }
            purchaseOrderDetail.setBatchNo(charg);
            purchaseOrderDetail.setDeliveryLineNo(charg);
//            purchaseOrderDetail.setNoDeliveryFlag();
            purchaseOrderDetail.setUnit(meins);
            purchaseOrderDetail.setStoLocation(lgort);
            purchaseOrderDetail.setMoveType(bwart);
            purchaseOrderDetail.setPickStatus(kostk);
            purchaseOrderDetail.setExcFinFlag("Y");

            // 根据交货单号和行号判断是否重复
            WmsPurchaseOrderDetailRaw orderDetailRaw = new PurchaseOrderDetailRawDto();
            orderDetailRaw.setPurchaseOrderNo(vbeln2);
            orderDetailRaw.setPurchaseLineNo(posnr);
            List<WmsPurchaseOrderDetailRaw> wmsPurchaseOrderDetailRaws = purchaseOrderDetailRawMapper.selectWmsPurchaseOrderDetailRawList(orderDetailRaw);

            if (CollectionUtils.isEmpty(wmsPurchaseOrderDetailRaws)) {
                purchaseOrderDetailRawMapper.insertWmsPurchaseOrderDetailRaw(purchaseOrderDetail);
            } else {
                for (WmsPurchaseOrderDetailRaw purchaseOrderDetailRaw : wmsPurchaseOrderDetailRaws) {
                    purchaseOrderDetail.setId(purchaseOrderDetailRaw.getId());
                    purchaseOrderDetailRawMapper.updateWmsPurchaseOrderDetailRaw(purchaseOrderDetail);
                }
            }

            detailList.add(purchaseOrderDetail);
        }
        List<TaskInfo> taskInfoList = new ArrayList();
        //添加入库任务集合
        for (WmsPurchaseOrderDetailRaw purchaseOrderDetail : detailList) {
            TaskInfo taskInfo = new TaskInfo();
            BeanUtils.copyProperties(purchaseOrderDetail, taskInfo);

            AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId(TaskNoTypeConstant.STOCK_IN_STD_TASK, Convert.toStr(tenantId));

            if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())){
                throw new ServiceException("入库任务号生成失败！");
            }
            taskInfo.setTaskNo(ajaxResult.get("data").toString());
            taskInfo.setDetailId(purchaseOrderDetail.getId());
            taskInfo.setTaskStatus(TaskStatusConstant.TASK_STATUS_NEW);
            taskInfo.setTaskType(TaskTypeConstant.UNFREEZE_TASK);
            taskInfo.setHandlerUserId(SecurityUtils.getUserId());
            taskInfo.setHandlerUserName(SecurityUtils.getUsername());
            taskInfo.setTenantId(tenantId);
            taskInfo.setStockInOrderNo(vbeln);
            taskInfo.setCreateBy(SecurityUtils.getUsername());
            taskInfo.setCreateTime(DateUtils.getNowDate());
            taskInfo.setLocationCode(purchaseOrderDetail.getStoLocation());
            taskInfoList.add(taskInfo);

            //更新标准入库单明细状态
            purchaseOrderDetail.setTenantId(tenantId);
            purchaseOrderDetail.setCreateBy(SecurityUtils.getUsername());
            purchaseOrderDetail.setCreateTime(DateUtils.getNowDate());
            purchaseOrderDetailRawMapper.updateWmsPurchaseOrderDetailRaw(purchaseOrderDetail);
        }

        //批量添加任务
        if (taskInfoMapper.insertTaskInfoListNoTenantId(taskInfoList) <= 0) {
            throw new ServiceException("任务生成失败！");
        }

        return 1;
    }

    /**
     * 采购订单,采购订单计划同步
     * @param headList
     * @param itemList
     * @return
     */
    @Override
    public String syncPurchasePlanOrder(List<Map<String, Object>> headList, List<Map<String, Object>> itemList) {
        logger.info("开始同步采购订单信息。");
        Long tenantId = MainDataConstant.TENANT_ID;
        String createBy = MainDataConstant.USER_SAP;
        // head只有一个
        Map<String, Object> headInfo = headList.get(0);
        // 操作标识
        String opFlag = Convert.toStr(headInfo.get("OP_FLAG"));
        // 采购凭证编号
        String orderNo = Convert.toStr(headInfo.get("EBELN"));
        // 采购凭证类别
        String purchaseVoucherType = Convert.toStr(headInfo.get("BSTYP"));
        // 订单类型（采购）
        String orderType = Convert.toStr(headInfo.get("BSART"));
        // 采购凭证类型的简短描述
        String batxt = Convert.toStr(headInfo.get("BATXT"));
        // 公司代码
        String companyCode = Convert.toStr(headInfo.get("BUKRS"));
        // 公司代码或公司的名称
        String companyName = Convert.toStr(headInfo.get("BUTXT"));
        // 供应商或债权人的帐号
        String supplierCode = Convert.toStr(headInfo.get("LIFNR"));
        // 名称
        String supplierName = Convert.toStr(headInfo.get("NAME1"));
        // 采购组织
        String purchaseOrg = Convert.toStr(headInfo.get("EKORG"));
        // 采购组织描述
        String purchaseOrgDesc = Convert.toStr(headInfo.get("EKOTX"));
        // 采购组
        String purchaseGroup = Convert.toStr(headInfo.get("EKGRP"));
        // 采购组的描述
        String purchaseName = Convert.toStr(headInfo.get("EKNAM"));
        // 最后更改的日期
        String updateDate = Convert.toStr(headInfo.get("AEDAT"));

        // 采购凭证状态
        String itemPstyp = Convert.toStr(itemList.get(0).get("PSTYP"));

        // 根据订单类型区分是否委外: 订单类型为Z50，且采购凭证状态为3时为委外订单
        boolean isOutSourcing = "3".equals(itemPstyp) && "Z50".equals(orderType);

        StockInStdOrderDto dto = new StockInStdOrderDto();
        dto.setOrderNo(orderNo);

        dto.setBusinessType(StockInOrderConstant.ORDER_TYPE_UNASN);
        if (isOutSourcing) {
            dto.setBusinessType(StockInOrderConstant.ORDER_TYPE_OUTSOURCING);
        }
        SupplierCommonDto supplierCommonDto = esbSendCommonMapper.getSupplierInfoByCode(supplierCode);

        if (ObjectUtils.isEmpty(supplierCommonDto)) {
            throw new ServiceException(String.format("根据供应商code:%s未查询到相关信息", supplierCode));
        }

        dto.setVendorId(supplierCommonDto.getId());
        dto.setVendorName(supplierCommonDto.getSupplierName());
        dto.setVendorCode(supplierCommonDto.getSupplierCode());
        List<FactoryCommonDto> factoryInfo = esbSendCommonMapper.getFactoryByCode(companyCode);
        if (ObjectUtils.isEmpty(factoryInfo)) {
            throw new ServiceException(String.format("根据工厂code:%s未查询到相关信息", companyCode));
        }
        dto.setFactoryId(factoryInfo.get(0).getId());
        dto.setFactoryCode(factoryInfo.get(0).getFactoryCode());
        dto.setFactoryName(factoryInfo.get(0).getFactoryName());
        dto.setOrderType(StockInOrderConstant.ORDER_TYPE_UNASN);
        dto.setOrderStatus(OrderStatusConstant.ORDER_STATUS_NEW);
        dto.setDeliveryDate(new Date());
        dto.setTenantId(tenantId);
        dto.setCreateBy(createBy);
        dto.setCreateTime(DateUtils.getNowDate());
        dto.setUpdateBy(createBy);
        dto.setUpdateTime(DateUtils.getNowDate());

        dto.setBstyp(purchaseVoucherType);
        dto.setBatxt(batxt);
        dto.setEkorg(purchaseOrg);
        dto.setEkotx(purchaseOrgDesc);
        dto.setEkgrp(purchaseGroup);
        dto.setEknam(purchaseName);
        dto.setAedat(DateUtils.parseDate(updateDate));
        dto.setBsart(orderType);

        // A新增 ASN号存在 新增  B修改
        StockInStdOrder stockInStdOrder = stockInStdOrderMapper.selectStockInStdOrderByNo(orderNo);
        if (ObjectUtils.isEmpty(stockInStdOrder) || ObjectUtils.isEmpty(stockInStdOrder.getId())) {
            logger.info("新增标准入库单据，订单号: {} ", orderNo);
            stockInStdOrderMapper.insertStockInStdOrder(dto);
        } else {
            logger.info("更新标准入库单据，订单号: {} ", orderNo);
            dto.setId(stockInStdOrder.getId());
            // 存在修改
            stockInStdOrderMapper.updateStockInStdOrder(dto);
        }

        // 添加入库单明细信息
        addStockInStdOrderDetail(isOutSourcing, itemList, dto, tenantId, createBy);

        // 添加委外单据的bom信息
        addOutSourcingBomInfo(isOutSourcing, dto, tenantId, createBy, supplierCode);

        if (!ObjectUtils.isEmpty(itemList)) {
            Map<String, Object> checkItemMap = itemList.get(0);
            // 原材料退货项目不需要生成标准入库单
            if ("X".equals(Convert.toStr(checkItemMap.get("RETPO")))) {
                stockInStdOrderMapper.deleteStockInStdOrderById(dto.getId());
            }
        }
        return "";
    }

    private void addStockInStdOrderDetail(boolean isOutSourcing, List<Map<String, Object>> itemList, StockInStdOrderDto dto, Long tenantId, String createBy) {
        String supplierCode = dto.getVendorCode();
        for (Map<String, Object> itemMap : itemList) {
            // 采购凭证编号
            String purchaseVoucherNo = Convert.toStr(itemMap.get("EBELN"));
            // 采购凭证的项目编号
            String lineNo = Convert.toStr(itemMap.get("EBELP"));
            // 物料编号
            String materialCode = Convert.toStr(itemMap.get("MATNR"));
            // 物料描述
            String materialName = Convert.toStr(itemMap.get("MAKTX"));
            // 数量
            String quantity = Convert.toStr(itemMap.get("MENGE"));
            // 工厂
            String factoryCode = Convert.toStr(itemMap.get("WERKS"));
            // 存储地点
            String locationCode = Convert.toStr(itemMap.get("LGORT"));
            // 基本计量单位
            String unit = Convert.toStr(itemMap.get("MEINS"));
            // 基本单位
            String baseUnit = Convert.toStr(itemMap.get("ZMEINS"));
            // 基本数量
            String baseQuantity = Convert.toStr(itemMap.get("ZMENGE"));
            // 项目交货日期
            String eindt = Convert.toStr(itemMap.get("EINDT"));
            // 库存类型
            String insmk = Convert.toStr(itemMap.get("INSMK"));
            // 采购凭证中的项目类别
            String pstyp = Convert.toStr(itemMap.get("PSTYP"));
            // 科目分配类别
            String knttp = Convert.toStr(itemMap.get("KNTTP"));
            // 工作分解结构元素 (WBS 元素)
            String psPspPnr = Convert.toStr(itemMap.get("PS_PSP_PNR"));
            // 成本中心
            String kostl = Convert.toStr(itemMap.get("KOSTL"));
            // 主要资产编号
            String anln1 = Convert.toStr(itemMap.get("ANLN1"));
            // 订单编号
            String aufnr = Convert.toStr(itemMap.get("AUFNR"));
            // 资产类别作删除标记
            String loekz = Convert.toStr(itemMap.get("LOEKZ"));
            // “交货已完成”标识
            String elikz = Convert.toStr(itemMap.get("ELIKZ"));
            // 退货项目
            String returnProject = Convert.toStr(itemMap.get("RETPO"));
            // 采购申请编号
            String banfn = Convert.toStr(itemMap.get("BANFN"));
            // 采购申请的项目编号
            String bnfpo = Convert.toStr(itemMap.get("BNFPO"));

            if("X".equals(returnProject)) {
                RwmReturnOrderDto returnOrderDto = new RwmReturnOrderDto();
                returnOrderDto.setOrderNo(purchaseVoucherNo);
                returnOrderDto.setLocationCode(locationCode);
                returnOrderDto.setFactoryCode(factoryCode);
                returnOrderDto.setSupplierCode(supplierCode);
                returnOrderDto.setIsShortage(CommonYesOrNo.NO);
                returnOrderDto.setBillStatus(OrderStatusConstant.ORDER_STATUS_NEW);
                returnOrderDto.setReturnMethod("161");
                returnOrderDto.setReason("0");
                if (ObjectUtils.isEmpty(SecurityUtils.getLoginUser())
                        || ObjectUtils.isEmpty(SecurityUtils.getLoginUser().getSysUser())) {
                    returnOrderDto.setTenantId(MainDataConstant.TENANT_ID);
                } else {
                    returnOrderDto.setTenantId(SecurityUtils.getLoginUser().getSysUser().getTenantId());
                }

                List<RwmReturnOrderDetailsDto> detailsDtoList = new ArrayList<>();
                RwmReturnOrderDetailsDto detailsDto = new RwmReturnOrderDetailsDto();
                detailsDto.setPurchaseOrderNo(purchaseVoucherNo);
                detailsDto.setLineNo(lineNo);
                detailsDto.setMaterialNo(materialCode);
                detailsDto.setMaterialCode(materialCode);
                detailsDto.setMaterialName(materialName);
                detailsDto.setFactoryCode(factoryCode);
                detailsDto.setUnit(unit);
                detailsDto.setOperationUnit(baseUnit);
                detailsDto.setOperationQty(new BigDecimal(quantity));
                detailsDtoList.add(detailsDto);
                returnOrderDto.setDetailList(detailsDtoList);
                stockOutService.addRwmOrderTask(returnOrderDto);
            } else {
                // 非ASN入库/委外入库
                // 如果存在就删掉
                if(!ObjectUtils.isEmpty(dto) && !ObjectUtils.isEmpty(dto.getId())) {
                    StockInStdOrderDetailDto stockInStdOrderDetail = new StockInStdOrderDetailDto();
                    stockInStdOrderDetail.setStdOrderId(dto.getId());
                    stockInStdOrderDetail.setPurchaseOrderNo(purchaseVoucherNo);
                    stockInStdOrderDetail.setPurchaseLineNo(lineNo);
                    List<StockInStdOrderDetail> detailList = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(stockInStdOrderDetail);
                    if (!ObjectUtils.isEmpty(detailList)) {
                        Long[] detailIds = detailList.stream().map(StockInStdOrderDetail::getId).toArray(Long[]::new);
                        stockInStdOrderDetailMapper.deleteStockInStdOrderDetailByIds(detailIds);
                    }
                }

                AjaxResult materialBasicInfo = mainDataService.getMaterialBasicInfo(materialCode);
                if(materialBasicInfo.isError()) {
                    throw new ServiceException(materialBasicInfo.get("msg").toString());
                }
                WmsMaterialBasicVo materialInfo = com.alibaba.fastjson2.JSONObject.parseObject(materialBasicInfo.get("data").toString(), WmsMaterialBasicVo.class);
                if(ObjectUtils.isEmpty(materialInfo)){
                    throw new ServiceException("不存在该物料号" + materialCode);
                }

                StockInStdOrderDetail stockInStdOrderDetail = new StockInStdOrderDetail();
                stockInStdOrderDetail.setStdOrderId(dto.getId());
                stockInStdOrderDetail.setPurchaseOrderNo(purchaseVoucherNo);
                stockInStdOrderDetail.setPurchaseLineNo(Convert.toStr(lineNo));
                stockInStdOrderDetail.setMaterialNo(materialCode);
                stockInStdOrderDetail.setMaterialName(materialName);
                stockInStdOrderDetail.setOldMaterialNo(materialInfo.getOldMaterialNo());
                if(isOutSourcing) {
                    stockInStdOrderDetail.setDetailType(StockInOrderConstant.ORDER_TYPE_OUTSOURCING);
                    stockInStdOrderDetail.setMoveType("101");
                } else {
                    stockInStdOrderDetail.setDetailType(StockInOrderConstant.ORDER_TYPE_UNASN);
                }
                BigDecimal receiveQty = ObjectUtils.isEmpty(quantity) ? BigDecimal.ZERO : new BigDecimal(quantity.trim());
                stockInStdOrderDetail.setDeliverQty(receiveQty);
                stockInStdOrderDetail.setAvailableQty(receiveQty);
                stockInStdOrderDetail.setReceivedQty(BigDecimal.ZERO);

                if (ObjectUtils.isEmpty(baseQuantity)) {
                    stockInStdOrderDetail.setPackagesQty(BigDecimal.ZERO);
                } else {
                    stockInStdOrderDetail.setPackagesQty(new BigDecimal(baseQuantity.trim()));
                }

                // 是否质检
                stockInStdOrderDetail.setIsQc("x".equalsIgnoreCase(insmk) || "2".equals(insmk) ? CommonYesOrNo.YES : CommonYesOrNo.NO);
                stockInStdOrderDetail.setIsConsign(CommonYesOrNo.NO);
                stockInStdOrderDetail.setUnit(unit);
                stockInStdOrderDetail.setOperationUnit(baseUnit);

                if(ObjectUtils.isEmpty(locationCode)) {
                    throw new ServiceException("LGORT库存地点不存在");
                }

                AjaxResult locationCodeCountAjaxResult = mainDataService.getLocationCodeCount(locationCode);

                if (locationCodeCountAjaxResult.isError()) {
                    throw new ServiceException(String.format("库存地点:%s, 不存在!", locationCode));
                }

                stockInStdOrderDetail.setLocationCode(locationCode);
                stockInStdOrderDetail.setThirdDeliveryOrderNo(aufnr);
                stockInStdOrderDetail.setThirdDeliveryLineNo(elikz);
                stockInStdOrderDetail.setLineNo(lineNo);
                stockInStdOrderDetail.setFinishFlag(elikz);
                stockInStdOrderDetail.setStatus(TaskStatusConstant.TASK_STATUS_NEW);
                stockInStdOrderDetail.setTenantId(tenantId);
                stockInStdOrderDetail.setCreateBy(createBy);
                stockInStdOrderDetail.setCreateTime(DateUtils.getNowDate());

                stockInStdOrderDetail.setWbsElement(psPspPnr);
                stockInStdOrderDetail.setFactoryCode(factoryCode);
                stockInStdOrderDetail.setEindt(eindt);
                stockInStdOrderDetail.setPstyp(pstyp);
                stockInStdOrderDetail.setKnttp(knttp);
                stockInStdOrderDetail.setCostCenterCode(kostl);
                stockInStdOrderDetail.setAnln1(anln1);
                stockInStdOrderDetail.setLoekz(loekz);
                stockInStdOrderDetail.setRetpo(returnProject);
                stockInStdOrderDetail.setBanfn(banfn);
                stockInStdOrderDetail.setBnfpo(bnfpo);

//                AjaxResult ajaxResult = iCodeRuleService.getSeqWithTenantId("LN", tenantId.toString());
//                if (ajaxResult.isError() || StringUtils.isEmpty(ajaxResult.get("data").toString())) {
//                    throw new ServiceException("入库批次号生成失败！");
//                }
//                stockInStdOrderDetail.setLotNo(Convert.toStr(ajaxResult.get("data")));
                stockInStdOrderDetailMapper.insertStockInStdOrderDetail(stockInStdOrderDetail);
            }
        }
    }

    private void addOutSourcingBomInfo(boolean isOutSourcing, StockInStdOrderDto dto, Long tenantId,
                                       String createBy, String supplierCode) {
        if(isOutSourcing) {
            try {
                String orderNo = dto.getOrderNo();
                IMsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
                reqMo.setReqValue("PURCHASEORDER", orderNo);
                byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_IWMS_INQUIRY, reqMo.toString().getBytes());

                MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
                List<GroupRecord> list = resMo.getResGroupRecord("IT_ITEM");

                // 如果有bom就删掉
                if(!ObjectUtils.isEmpty(list) && !ObjectUtils.isEmpty(dto)
                        && !ObjectUtils.isEmpty(dto.getId())) {
                    stockInItemBomMapper.deleteStockInItemBomDetails(dto.getId());
                }
                for (GroupRecord bomItem : list) {
                    // 采购订单编号
                    String poNumber = Convert.toStr(bomItem.getFieldValue("PO_NUMBER"));
                    // 采购凭证的项目编号
                    String poItem = Convert.toStr(bomItem.getFieldValue("PO_ITEM"));
                    // 预留/相关需求的项目编号
                    String itemNo = Convert.toStr(bomItem.getFieldValue("ITEM_NO"));
                    // 物料编号
                    String material = Convert.toStr(bomItem.getFieldValue("MATERIAL"));
                    // 组件的需求数量
                    String entryQuantity = Convert.toStr(bomItem.getFieldValue("ENTRY_QUANTITY"));
                    // 条目单位
                    String entryUom = Convert.toStr(bomItem.getFieldValue("ENTRY_UOM"));
                    // 工厂
                    String plant = Convert.toStr(bomItem.getFieldValue("PLANT"));
                    // 库存地点
                    String locationCode = Convert.toStr(bomItem.getFieldValue("LGORT"));

                    String materialNo = material.replaceFirst("^0+", "");
                    StockInItemBom stockInItemBom = new StockInItemBom();
                    stockInItemBom.setVbeln(poNumber);
                    stockInItemBom.setPosnr(poItem);
                    stockInItemBom.setMaktx(materialNo);
                    stockInItemBom.setBdmng(new BigDecimal(entryQuantity));
                    stockInItemBom.setLagme(entryUom);
                    stockInItemBom.setPlant(plant);
                    stockInItemBom.setStdOrderId(dto.getId());
                    stockInItemBom.setLocationCode(locationCode);
                    stockInItemBom.setSupplierCode(supplierCode);
                    stockInItemBom.setTenantId(tenantId);
                    stockInItemBom.setCreateBy(createBy);
                    stockInItemBom.setCreateTime(new Date());

                    stockInItemBomMapper.insertStockInItemBomDetails(stockInItemBom);
                }
            } catch (Exception e) {
                logger.error("获取委外入库单据的bom信息失败，错误信息: {}", e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        }
    }

    @Override
    public String noAsnStockIn(List<Map<String, Object>> headList, List<Map<String, Object>> itemList) {
        String status = "";
        Long tenantId = MainDataConstant.TENANT_ID;
        String createBy = MainDataConstant.USER_SAP;
        // head只有一个
        Map<String, Object> headInfo = headList.get(0);
        // 交货编号
        String deliveryNo = Convert.toStr(headInfo.get("VBELN"));
        // 操作标识
        String opFlag = Convert.toStr(headInfo.get("OP_FLAG"));
        // 名称
        String name1 = Convert.toStr(headInfo.get("NAME1"));
        // 销售组织 工厂? 供应商?
        String salesOrg = Convert.toStr(headInfo.get("VKORG"));
        // 客户编号
        String customNo = Convert.toStr(headInfo.get("KUNNR"));
        // 装运类型
        String shipmentType = Convert.toStr(headInfo.get("VSART"));
        // 计划货物移动日期
        String moveDate = Convert.toStr(headInfo.get("WADAT"));
        // 交货日期
        String deliveryDate = Convert.toStr(headInfo.get("LFDAT"));

        StockInStdOrder stdOrder = new StockInStdOrder();
        stdOrder.setOrderNo(deliveryNo);
        StockInStdOrder stockInStdOrder = stockInStdOrderMapper.selectStockInStdOrder(stdOrder);

        StockInStdOrderDto dto = new StockInStdOrderDto();
        dto.setOrderNo(deliveryNo);
        dto.setVendorName(name1);
        dto.setOrderType(StockInOrderConstant.ORDER_TYPE_UNASN);
        dto.setOrderStatus(OrderStatusConstant.ORDER_STATUS_NEW);
        dto.setTenantId(tenantId);
        dto.setCreateBy(createBy);
        dto.setCreateTime(DateUtils.getNowDate());
        dto.setUpdateBy(createBy);
        dto.setUpdateTime(DateUtils.getNowDate());

        dto.setVkorg(salesOrg);
        dto.setKunnr(customNo);
        dto.setVsart(shipmentType);
        dto.setWadat(DateUtils.parseDate(moveDate));
        dto.setDeliveryDate(DateUtils.parseDate(deliveryDate));
        dto.setBusinessType(StockInOrderConstant.ORDER_TYPE_UNASN);

        // A新增  B修改
        // 存在修改  不存在新增
        if("A".equals(opFlag) && ObjectUtils.isEmpty(stockInStdOrder)) {
            stockInStdOrderMapper.insertStockInStdOrder(dto);
        } else if("A".equals(opFlag) && !ObjectUtils.isEmpty(stockInStdOrder)) {
            dto.setId(stockInStdOrder.getId());
            stockInStdOrderMapper.updateStockInStdOrder(dto);
        } else if("B".equals(opFlag)) {
            if(!ObjectUtils.isEmpty(stockInStdOrder)) {
                dto.setId(stockInStdOrder.getId());
                stockInStdOrderMapper.updateStockInStdOrder(dto);
            } else {
                stockInStdOrderMapper.insertStockInStdOrder(dto);
            }
        }

        for (Map<String, Object> itemMap : itemList) {
            // 交货
            String itemDeliveryNo = Convert.toStr(itemMap.get("VBELN"));
            // 物料编号
            String materialNo = Convert.toStr(itemMap.get("MATNR"));
            // 物料描述
            String materialName = Convert.toStr(itemMap.get("MAKTX"));
            // 交货数量
            String deliveryQty = Convert.toStr(itemMap.get("LFIMG"));
            // 基本计量单位
            String unit = Convert.toStr(itemMap.get("MEINS"));
            // 库存类型
            String insmk = Convert.toStr(itemMap.get("INSMK"));
            // 存储地点
            String locationCode = Convert.toStr(itemMap.get("LGORT"));
            // 工厂
            String factoryCode = Convert.toStr(itemMap.get("WERKS"));
            // 批次编号
            String charg = Convert.toStr(itemMap.get("CHARG"));
            // 移动类型
            String bwart = Convert.toStr(itemMap.get("BWART"));
            // 交货项目
            String posnr = Convert.toStr(itemMap.get("POSNR"));
            // 拣配状态
            String kostk = Convert.toStr(itemMap.get("KOSTK"));

            StockInStdOrderDetail stockInStdOrderDetail = new StockInStdOrderDetail();
            if (!ObjectUtils.isEmpty(charg)) {
                stockInStdOrderDetail.setLotNo(charg);
            }
            stockInStdOrderDetail.setCharg(charg);
            stockInStdOrderDetail.setBwart(bwart);
            stockInStdOrderDetail.setPosnr(posnr);
            stockInStdOrderDetail.setKostk(kostk);
            stockInStdOrderDetail.setStdOrderId(dto.getId());
            stockInStdOrderDetail.setPurchaseOrderNo(itemDeliveryNo);
            stockInStdOrderDetail.setMaterialNo(materialNo);
            stockInStdOrderDetail.setMaterialName(materialName);
            stockInStdOrderDetail.setReceivedQty(BigDecimal.ZERO);
            stockInStdOrderDetail.setAvailableQty(BigDecimal.ZERO);
            if(ObjectUtils.isEmpty(deliveryQty)) {
                stockInStdOrderDetail.setDeliverQty(BigDecimal.ZERO);
            } else {
                stockInStdOrderDetail.setDeliverQty(new BigDecimal(deliveryQty.trim()));
                stockInStdOrderDetail.setAvailableQty(new BigDecimal(deliveryQty.trim()));
            }

            // 是否质检
            stockInStdOrderDetail.setIsQc("x".equalsIgnoreCase(insmk) || "2".equals(insmk) ? CommonYesOrNo.YES : CommonYesOrNo.NO);
            stockInStdOrderDetail.setIsConsign("1");
            stockInStdOrderDetail.setUnit(unit);

            if(ObjectUtils.isEmpty(locationCode)) {
                throw new ServiceException("LGORT库存地点不存在");
            }

            AjaxResult locationCodeCountAjaxResult = mainDataService.getLocationCodeCount(locationCode);

            if (locationCodeCountAjaxResult.isError()) {
                throw new ServiceException(String.format("库存地点:%s, 不存在!", locationCode));
            }

            stockInStdOrderDetail.setLocationCode(locationCode);
            stockInStdOrderDetail.setStatus(OrderStatusConstant.ORDER_STATUS_NEW);
            stockInStdOrderDetail.setTenantId(tenantId);
            stockInStdOrderDetail.setCreateBy(createBy);
            stockInStdOrderDetail.setCreateTime(DateUtils.getNowDate());
            stockInStdOrderDetail.setUpdateBy(createBy);
            stockInStdOrderDetail.setUpdateTime(DateUtils.getNowDate());
            stockInStdOrderDetail.setFactoryCode(factoryCode);
            stockInStdOrderDetail.setPurchaseLineNo(posnr);

            // 根据交货单号和行号判断是否重复
            StockInStdOrderDetailDto stdOrderDetail = new StockInStdOrderDetailDto();
            stdOrderDetail.setPurchaseOrderNo(itemDeliveryNo);
            stdOrderDetail.setPurchaseLineNo(posnr);
            List<StockInStdOrderDetail> stockInStdOrderDetails = stockInStdOrderDetailMapper.selectStockInStdOrderDetailList(stdOrderDetail);

            if (CollectionUtils.isEmpty(stockInStdOrderDetails)) {
                status = "I";
                stockInStdOrderDetailMapper.insertStockInStdOrderDetail(stockInStdOrderDetail);
            } else {
                status = "U";
                for (StockInStdOrderDetail orderDetail : stockInStdOrderDetails) {
                    stockInStdOrderDetail.setId(orderDetail.getId());
                    stockInStdOrderDetailMapper.updateStockInStdOrderDetail(stockInStdOrderDetail);
                }
            }

        }
        return status;
    }
}

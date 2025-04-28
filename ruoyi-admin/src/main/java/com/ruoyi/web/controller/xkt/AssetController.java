package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageVO;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import com.ruoyi.web.controller.xkt.vo.account.*;
import com.ruoyi.xkt.dto.account.*;
import com.ruoyi.xkt.enums.EAccountOwnerType;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.manager.impl.AliPaymentMangerImpl;
import com.ruoyi.xkt.service.IAssetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author liangyq
 * @date 2025-04-07 18:31
 */
@Api(tags = "资产管理")
@RestController
@RequestMapping("/rest/v1/asset")
public class AssetController extends XktBaseController {

    @Autowired
    private IAssetService assetService;
    @Autowired
    private AliPaymentMangerImpl aliPaymentManger;

    @PreAuthorize("@ss.hasPermi('system:asset:query')")
    @ApiOperation(value = "档口资产")
    @GetMapping(value = "store/current")
    public R<AssetInfoVO> getCurrentStoreAsset() {
        AssetInfoDTO dto = assetService.getStoreAssetInfo(SecurityUtils.getStoreId());
        return success(BeanUtil.toBean(dto, AssetInfoVO.class));
    }

    @PreAuthorize("@ss.hasPermi('system:asset:query')")
    @ApiOperation(value = "卖家资产")
    @GetMapping(value = "user/current")
    public R<AssetInfoVO> getCurrentUserAsset() {
        AssetInfoDTO dto = assetService.getUserAssetInfo(SecurityUtils.getUserId());
        return success(BeanUtil.toBean(dto, AssetInfoVO.class));
    }

    @PreAuthorize("@ss.hasPermi('system:asset:add')")
    @ApiOperation(value = "档口绑定支付宝")
    @PostMapping(value = "store/alipay/bind")
    public R<AssetInfoVO> bindStoreAlipay(@Validated @RequestBody AlipayStoreBindVO vo) {
        AlipayBindDTO dto = BeanUtil.toBean(vo, AlipayBindDTO.class);
        dto.setOwnerId(SecurityUtils.getStoreId());
        dto.setOwnerType(EAccountOwnerType.STORE.getValue());
        return success(BeanUtil.toBean(assetService.bindAlipay(dto), AssetInfoVO.class));
    }

    @PreAuthorize("@ss.hasPermi('system:asset:add')")
    @ApiOperation(value = "卖家绑定支付宝")
    @PostMapping(value = "user/alipay/bind")
    public R<AssetInfoVO> bindUserAlipay(@Validated @RequestBody AlipayUserBindVO vo) {
        AlipayBindDTO dto = BeanUtil.toBean(vo, AlipayBindDTO.class);
        dto.setOwnerId(SecurityUtils.getUserId());
        dto.setOwnerType(EAccountOwnerType.USER.getValue());
        return success(BeanUtil.toBean(assetService.bindAlipay(dto), AssetInfoVO.class));
    }

    @PreAuthorize("@ss.hasPermi('system:asset:add')")
    @ApiOperation(value = "档口设置交易密码")
    @PostMapping(value = "store/transaction-password/set")
    public R<AssetInfoVO> setTransactionPassword(@Validated @RequestBody TransactionPasswordSetVO vo) {
        TransactionPasswordSetDTO dto = BeanUtil.toBean(vo, TransactionPasswordSetDTO.class);
        dto.setStoreId(SecurityUtils.getStoreId());
        return success(BeanUtil.toBean(assetService.setTransactionPassword(dto), AssetInfoVO.class));
    }

    @PreAuthorize("@ss.hasPermi('system:asset:add')")
    @ApiOperation(value = "档口支付宝提现")
    @PostMapping(value = "store/alipay/withdraw")
    public R withdrawByAlipay(@Validated @RequestBody WithdrawReqVO vo) {
        //创建付款单
        WithdrawPrepareResult prepareResult = assetService.prepareWithdraw(SecurityUtils.getStoreId(), vo.getAmount(),
                vo.getTransactionPassword(), EPayChannel.ALI_PAY);
        //支付宝转账
        aliPaymentManger.transfer(prepareResult.getBillNo(), prepareResult.getAccountOwnerNumber(),
                prepareResult.getAccountOwnerName(), prepareResult.getAmount());
        //付款单到账
        assetService.withdrawSuccess(prepareResult.getFinanceBillId());
        return success();
    }

    @PreAuthorize("@ss.hasPermi('system:asset:list')")
    @ApiOperation(value = "档口交易明细")
    @PostMapping("store/trans-detail/page")
    public R<PageVO<TransDetailStorePageItemVO>> pageStoreTransDetail(@Validated @RequestBody BasePageVO vo) {
        TransDetailStoreQueryDTO queryDTO = BeanUtil.toBean(vo, TransDetailStoreQueryDTO.class);
        queryDTO.setStoreId(SecurityUtils.getStoreId());
        Page<TransDetailStorePageItemDTO> pageDTO = assetService.pageStoreTransDetail(queryDTO);
        return success(PageVO.of(pageDTO, TransDetailStorePageItemVO.class));
    }

    @PreAuthorize("@ss.hasPermi('system:asset:list')")
    @ApiOperation(value = "卖家交易明细")
    @PostMapping("user/trans-detail/page")
    public R<PageVO<TransDetailUserPageItemVO>> pageUserTransDetail(@Validated @RequestBody BasePageVO vo) {
        TransDetailUserQueryDTO queryDTO = BeanUtil.toBean(vo, TransDetailUserQueryDTO.class);
        queryDTO.setUserId(SecurityUtils.getUserId());
        Page<TransDetailUserPageItemDTO> pageDTO = assetService.pageUserTransDetail(queryDTO);
        return success(PageVO.of(pageDTO, TransDetailUserPageItemVO.class));
    }


}

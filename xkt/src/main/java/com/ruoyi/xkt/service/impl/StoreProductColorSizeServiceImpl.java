package com.ruoyi.xkt.service.impl;

import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdSnDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdSnResDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreSaleSnDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreSaleSnResDTO;
import com.ruoyi.xkt.mapper.StoreProductColorSizeMapper;
import com.ruoyi.xkt.mapper.StoreSaleDetailMapper;
import com.ruoyi.xkt.service.IStoreProductColorSizeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 档口商品颜色的尺码Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductColorSizeServiceImpl implements IStoreProductColorSizeService {

    final StoreProductColorSizeMapper prodColorSizeMapper;
    final StoreSaleDetailMapper saleDetailMapper;

    // 纯数字
    private static final Pattern POSITIVE_PATTERN = Pattern.compile("^\\d+$");

    /**
     * 查询条码 对应的商品信息
     *
     * @param snDTO 查询入参
     * @return StoreProdColorSizeBarcodeResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreSaleSnResDTO storeSaleSn(StoreSaleSnDTO snDTO) {
        // 非纯数字，则直接返回
        if (!POSITIVE_PATTERN.matcher(snDTO.getSn()).matches()) {
            return new StoreSaleSnResDTO().setSuccess(Boolean.FALSE).setSn(snDTO.getSn());
        }
        // 销售出库[退货]
        if (snDTO.getRefund()) {
            // 先查storeSaleDetail中的sns条码是否存在[可能同一个条码，被一个客户多次销售、退货，则取最近的一条]
            StoreSaleSnResDTO barcodeResDTO = this.saleDetailMapper.selectBySn(snDTO);
            if (ObjectUtils.isNotEmpty(barcodeResDTO)) {
                return barcodeResDTO.setSuccess(Boolean.TRUE);
            } else {
                // 若是没查询到数据，则走正常条码查询流程
                return this.getSnInfo(snDTO);
            }
            // 销售出库[销售] 正常条码查询流程
        } else {
            return this.getSnInfo(snDTO);
        }
    }

    /**
     * 商品入库、库存盘点查询库存
     *
     * @param snDTO 条码入参
     * @return StoreProdSnsResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdSnResDTO sn(StoreProdSnDTO snDTO) {
        // 非纯数字的条码
        List<String> failList = snDTO.getSnList().stream().filter(s -> !POSITIVE_PATTERN.matcher(s).matches()).collect(Collectors.toList());

        List<StoreProdSnResDTO.SPSDetailDTO> successList = new ArrayList<>();
        // 步橘网系统的条码
        List<String> snList = snDTO.getSnList().stream().filter(s -> POSITIVE_PATTERN.matcher(s).matches())
                .filter(x -> x.startsWith(snDTO.getStoreId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(snList)) {
            // 截取前13位 作为条码查询条件
            Set<String> pre13SnSet = snList.stream().map(x -> x.substring(0, 13)).collect(Collectors.toSet());
            List<StoreProdSnResDTO.SPSDetailDTO> existList = prodColorSizeMapper.selectSnList(new ArrayList<>(pre13SnSet), snDTO.getStoreId());
            Map<String, StoreProdSnResDTO.SPSDetailDTO> existMap = existList.stream().collect(Collectors.toMap(StoreProdSnResDTO.SPSDetailDTO::getSn, x -> x));
            snList.forEach(sn -> {
                String snPrefix = sn.substring(0, 13);
                StoreProdSnResDTO.SPSDetailDTO exist = existMap.get(snPrefix);
                if (ObjectUtils.isNotEmpty(exist)) {
                    successList.add( exist.setSn(sn));
                } else {
                    failList.add( sn);
                }
            });
        }
        // 其它系统的条码
        List<String> otherSnList = snDTO.getSnList().stream().filter(s -> POSITIVE_PATTERN.matcher(s).matches())
                .filter(x -> !x.startsWith(snDTO.getStoreId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(otherSnList)) {

            // 从系统设置中获取，根据系统迁移时的配置
            final Integer otherSysEndIndex = 9;

            // 截取前 xxx 位 作为条码查询条件
            Set<String> preIndexSnSet = snList.stream().map(x -> x.substring(0, otherSysEndIndex)).collect(Collectors.toSet());


        }





        return null;
    }

    /**
     * 普通销售流程获取条码对应的商品信息
     *
     * @param snDTO 条码入参
     * @return StoreSaleBarcodeResDTO
     */
    private StoreSaleSnResDTO getSnInfo(StoreSaleSnDTO snDTO) {
        StoreSaleSnResDTO barcodeResDTO;
        // 步橘网生成的条码
        if (snDTO.getSn().startsWith(snDTO.getStoreId())) {
            final String snsPrefix = snDTO.getSn().substring(0, 13);
            // 查询数据库 获取条码对应的商品信息
            barcodeResDTO = prodColorSizeMapper.selectSn(snsPrefix, snDTO.getStoreId(), snDTO.getStoreCusId());
        } else {

            // 从系统设置中获取，根据系统迁移时的配置
            final Integer otherSysEndIndex = 9;

            final String snsPrefix = snDTO.getSn().substring(0, otherSysEndIndex);
            // 查询数据库 获取条码对应的商品信息
            barcodeResDTO = prodColorSizeMapper.selectOtherSn(snsPrefix, snDTO.getStoreId(),  snDTO.getStoreCusId());
        }
        return ObjectUtils.isEmpty(barcodeResDTO) ? new StoreSaleSnResDTO().setSuccess(Boolean.FALSE).setSn(snDTO.getSn())
                : barcodeResDTO.setSuccess(Boolean.TRUE).setSn(snDTO.getSn());
    }


}

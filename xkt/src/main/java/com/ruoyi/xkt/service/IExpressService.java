package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.Express;
import com.ruoyi.xkt.domain.ExpressFeeConfig;
import com.ruoyi.xkt.domain.ExpressRegion;
import com.ruoyi.xkt.dto.express.*;
import com.ruoyi.xkt.manager.ExpressManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author liangyq
 * @date 2025-04-03 13:35
 */
public interface IExpressService {

    /**
     * 检查快递是否可用
     *
     * @param expressId
     */
    void checkExpress(Long expressId);

    /**
     * 获取物流
     *
     * @param expressId
     * @return
     */
    Express getById(Long expressId);

    /**
     * 获取所有物流
     *
     * @return
     */
    List<ExpressDTO> allExpress();

    /**
     * 快递费
     *
     * @param goodsQuantity
     * @param provinceCode
     * @param cityCode
     * @param countyCode
     * @return
     */
    List<ExpressFeeDTO> listExpressFee(Integer goodsQuantity, String provinceCode, String cityCode, String countyCode);

    /**
     * 获取快递费配置
     *
     * @param expressId
     * @param provinceCode
     * @param cityCode
     * @param countyCode
     * @return
     */
    ExpressFeeConfig getExpressFeeConfig(Long expressId, String provinceCode, String cityCode, String countyCode);

    /**
     * 获取档口联系方式
     *
     * @param storeId
     * @return
     */
    ExpressContactDTO getStoreContact(Long storeId);

    /**
     * 获取行政区划
     *
     * @param regionCodes
     * @return
     */
    List<ExpressRegion> listRegionByCode(Collection<String> regionCodes);

    /**
     * 获取缓存的行政编码
     *
     * @return
     */
    List<ExpressRegionDTO> getRegionListCache();

    /**
     * 获取缓存的行政编码
     *
     * @return
     */
    Map<String, ExpressRegionDTO> getRegionMapCache();

    /**
     * 获取缓存的行政编码
     *
     * @return
     */
    List<ExpressRegionTreeNodeDTO> getRegionTreeCache();

    /**
     * 智能解析地址信息
     *
     * @param str
     * @return
     */
    ExpressStructAddressDTO parseNamePhoneAddress(String str);

    /**
     * ExpressManager
     *
     * @param expressId
     * @return
     */
    ExpressManager getExpressManager(Long expressId);

    /**
     * 添加轨迹记录
     *
     * @param addDTO
     * @return
     */
    Long addTrackRecord(ExpressTrackRecordAddDTO addDTO);

    /**
     * 获取所有物流名称
     *
     * @return
     */
    Map<Long, String> getAllExpressNameMap();

    /**
     * 获取轨迹记录
     *
     * @param expressWaybillNos
     * @return
     */
    List<ExpressTrackRecordDTO> listTrackRecord(Collection<String> expressWaybillNos);
}

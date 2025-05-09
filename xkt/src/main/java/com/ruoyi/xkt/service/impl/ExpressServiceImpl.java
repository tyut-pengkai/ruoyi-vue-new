package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.SimpleEntity;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.express.*;
import com.ruoyi.xkt.manager.ExpressManager;
import com.ruoyi.xkt.manager.impl.ZtoExpressManagerImpl;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liangyq
 * @date 2025-04-03 13:37
 */
@Service
public class ExpressServiceImpl implements IExpressService {

    private ExpressRegionDTO emptyRegion = new ExpressRegionDTO();
    @Value("${express.default.province:510000}")
    private String expressDefaultProvince;
    @Value("${express.default.city:510100}")
    private String expressDefaultCity;
    @Value("${express.default.county:510114}")
    private String expressDefaultCounty;
    @Autowired
    private ExpressMapper expressMapper;
    @Autowired
    private ExpressFeeConfigMapper expressFeeConfigMapper;
    @Autowired
    private ExpressTrackRecordMapper expressTrackRecordMapper;
    @Autowired
    private ExpressRegionMapper expressRegionMapper;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ZtoExpressManagerImpl ztoExpressManager;
    @Autowired
    private List<ExpressManager> expressManagers;

    @Override
    public void checkExpress(Long expressId) {
        Assert.notNull(expressId);
        Express express = expressMapper.selectById(expressId);
        Assert.isTrue(BeanValidators.exists(express), "快递不存在");
        Assert.isTrue(express.getSystemDeliverAccess(), "快递不可用");
    }

    @Override
    public Express getById(Long expressId) {
        return expressMapper.selectById(expressId);
    }

    @Override
    public List<ExpressDTO> allExpress() {
        return BeanUtil.copyToList(expressMapper.selectList(Wrappers.lambdaQuery(Express.class)
                .eq(SimpleEntity::getDelFlag, Constants.UNDELETED)), ExpressDTO.class);
    }

    @Override
    public List<ExpressFeeDTO> listExpressFee(Integer goodsQuantity, String provinceCode, String cityCode, String countyCode) {
        Assert.notNull(goodsQuantity);
        Assert.isTrue(goodsQuantity > 0);
        List<Express> expresses = expressMapper.selectList(Wrappers.lambdaQuery(Express.class)
                .eq(Express::getSystemDeliverAccess, true)
                .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        return expresses.stream().map(e -> {
            ExpressFeeDTO dto = BeanUtil.toBean(e, ExpressFeeDTO.class);
            ExpressFeeConfig feeConfig = getExpressFeeConfig(e.getId(), provinceCode, cityCode, countyCode);
            Assert.notNull(feeConfig, "获取快递费用异常");
            BigDecimal fee;
            if (goodsQuantity == 1) {
                fee = feeConfig.getFirstItemAmount();
            } else {
                fee = feeConfig.getFirstItemAmount()
                        .add(feeConfig.getNextItemAmount().multiply(BigDecimal.valueOf(goodsQuantity - 1)));
            }
            dto.setExpressFee(fee);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ExpressFeeConfig getExpressFeeConfig(Long expressId, String provinceCode, String cityCode,
                                                String countyCode) {
        Assert.notNull(expressId);
        Assert.notEmpty(provinceCode);
        Assert.notEmpty(cityCode);
        Assert.notEmpty(countyCode);
        //TODO mock
//        Map<String, ExpressFeeConfig> map = expressFeeConfigMapper.selectList(Wrappers.lambdaQuery(ExpressFeeConfig.class)
//                .eq(ExpressFeeConfig::getExpressId, expressId)
//                .in(ExpressFeeConfig::getRegionCode, Arrays.asList(provinceCode, cityCode, countyCode)))
//                .stream()
//                //过滤掉已被删除的配置
//                .filter(BeanValidators::exists)
//                .collect(Collectors.toMap(o -> o.getRegionCode(), o -> o, (n, o) -> n));
//        ExpressFeeConfig expressFeeConfig = null;
//        if (CollUtil.isNotEmpty(map)) {
//            if (map.size() == 1) {
//                expressFeeConfig = CollUtil.getFirst(map.values());
//            } else {
//                expressFeeConfig = map.get(countyCode);
//                //按区市省从小到大去匹配
//                if (expressFeeConfig == null) {
//                    expressFeeConfig = map.get(cityCode);
//                    if (expressFeeConfig == null) {
//                        expressFeeConfig = map.get(provinceCode);
//                    }
//                }
//            }
//        }
        ExpressFeeConfig expressFeeConfig = new ExpressFeeConfig();
        expressFeeConfig.setExpressId(expressId);
        expressFeeConfig.setFirstItemAmount(BigDecimal.valueOf(5));
        expressFeeConfig.setNextItemAmount(BigDecimal.valueOf(5));
        return expressFeeConfig;
    }

    @Override
    public ExpressContactDTO getStoreContact(Long storeId) {
        Assert.notNull(storeId);
        ExpressContactDTO expressContactDTO = new ExpressContactDTO();
        expressContactDTO.setProvinceCode(expressDefaultProvince);
        expressContactDTO.setCityCode(expressDefaultCity);
        expressContactDTO.setCountyCode(expressDefaultCounty);
        Store store = storeMapper.selectById(storeId);
        Assert.notNull(store);
        expressContactDTO.setDetailAddress(store.getStoreAddress());
        expressContactDTO.setContactName(store.getContactName());
        expressContactDTO.setContactPhoneNumber(store.getContactPhone());
        return expressContactDTO;
    }

    @Override
    public List<ExpressRegion> listRegionByCode(Collection<String> regionCodes) {
        if (CollUtil.isEmpty(regionCodes)) {
            return ListUtil.empty();
        }
        return expressRegionMapper.selectList(Wrappers.lambdaQuery(ExpressRegion.class)
                .in(ExpressRegion::getRegionCode, regionCodes));
    }

    @Override
    public List<ExpressRegionDTO> getRegionListCache() {
        List<ExpressRegionDTO> regionList = redisCache.getCacheList(Constants.EXPRESS_REGION_LIST_CACHE_KEY);
        if (CollUtil.isEmpty(regionList)) {
            List<ExpressRegion> list = expressRegionMapper.selectList(Wrappers.emptyWrapper());
            regionList = BeanUtil.copyToList(list, ExpressRegionDTO.class);
            redisCache.setCacheList(Constants.EXPRESS_REGION_LIST_CACHE_KEY, regionList);
        }
        return regionList;
    }

    @Override
    public Map<String, ExpressRegionDTO> getRegionMapCache() {
        Map<String, ExpressRegionDTO> regionMap = redisCache.getCacheMap(Constants.EXPRESS_REGION_MAP_CACHE_KEY);
        if (regionMap == null || regionMap.isEmpty()) {
            regionMap = getRegionListCache().stream()
                    .collect(Collectors.toMap(ExpressRegionDTO::getRegionCode, Function.identity()));
            redisCache.setCacheMap(Constants.EXPRESS_REGION_MAP_CACHE_KEY, regionMap);
        }
        return regionMap;
    }

    @Override
    public List<ExpressRegionTreeNodeDTO> getRegionTreeCache() {
        List<ExpressRegionTreeNodeDTO> treeNodeList = redisCache.getCacheList(Constants.EXPRESS_REGION_TREE_CACHE_KEY);
        if (CollUtil.isEmpty(treeNodeList)) {
            List<ExpressRegionDTO> dtoList = getRegionListCache().stream()
                    .filter(o -> Constants.UNDELETED.equals(o.getDelFlag()))
                    .collect(Collectors.toList());
            List<ExpressRegionTreeNodeDTO> list = BeanUtil.copyToList(dtoList, ExpressRegionTreeNodeDTO.class);
            treeNodeList = CollUtil.newArrayList();
            // 按parentCode进行分组
            Map<String, List<ExpressRegionTreeNodeDTO>> treeNodeMap = list.stream()
                    .filter(region -> StrUtil.isNotBlank(region.getParentRegionCode()))
                    .collect(Collectors.groupingBy(ExpressRegionTreeNodeDTO::getParentRegionCode));
            for (ExpressRegionTreeNodeDTO treeNodeDTO : list) {
                // 如果没有父级, 设置为根节点
                if (StrUtil.isBlank(treeNodeDTO.getParentRegionCode())) {
                    treeNodeList.add(treeNodeDTO);
                }
                // 为当前节点添加子节点
                List<ExpressRegionTreeNodeDTO> subTreeNodeList = treeNodeMap.get(treeNodeDTO.getRegionCode());
                treeNodeDTO.setChildren(CollUtil.emptyIfNull(subTreeNodeList));
            }
            redisCache.setCacheList(Constants.EXPRESS_REGION_TREE_CACHE_KEY, treeNodeList);
        }
        return treeNodeList;
    }

    @Override
    public ExpressStructAddressDTO parseNamePhoneAddress(String str) {
        /**
         * {"address":{"province":"重庆","town":"","city":"重庆市","countyId":"500107","county":"九龙坡区",
         * "cityId":"500100","detail":"杨九路志龙·观江岭1号","provinceId":"500000"},"phone":"15888888888","name":"张三丰"}
         */
        JSONObject rtn = ztoExpressManager.structureNamePhoneAddress(str);
        JSONObject address = rtn.getJSONObject("address");
        Assert.notNull(address, "获取行政区划失败");
        String provinceCode = address.getStr("provinceId");
        String cityCode = address.getStr("cityId");
        String countyCode = address.getStr("countyId");
        String name = rtn.getStr("name");
        String phone = rtn.getStr("phone");
        String detailAddress = address.getStr("detail");
//        Assert.notEmpty(provinceCode, "获取省失败");
//        Assert.notEmpty(cityCode, "获取市失败");
//        Assert.notEmpty(countyCode, "获取区县失败");
//        Assert.notEmpty(detailAddress, "获取详细地址失败");
//        Assert.notEmpty(name, "获取联系人失败");
//        Assert.isTrue(PhoneUtil.isPhone(phone), "获取联系电话失败");
        Map<String, ExpressRegionDTO> regionMap = getRegionMapCache();
        return ExpressStructAddressDTO.builder()
                .contactName(name)
                .contactPhoneNumber(phone)
                .provinceCode(provinceCode)
                .provinceName(regionMap.getOrDefault(provinceCode, emptyRegion).getRegionName())
                .cityCode(cityCode)
                .cityName(regionMap.getOrDefault(cityCode, emptyRegion).getRegionName())
                .countyCode(countyCode)
                .countyName(regionMap.getOrDefault(countyCode, emptyRegion).getRegionName())
                .detailAddress(detailAddress)
                .build();
    }

    @Override
    public ExpressManager getExpressManager(Long expressId) {
        Assert.notNull(expressId);
        for (ExpressManager expressManager : expressManagers) {
            if (expressManager.channel().getExpressId().equals(expressId)) {
                return expressManager;
            }
        }
        throw new ServiceException("未知物流渠道" + expressId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long addTrackRecord(ExpressTrackRecordAddDTO addDTO) {
        Assert.notNull(addDTO);
        Assert.notEmpty(addDTO.getExpressWaybillNo());
        ExpressTrackRecord expressTrackRecord = new ExpressTrackRecord();
        expressTrackRecord.setExpressId(addDTO.getExpressId());
        expressTrackRecord.setExpressWaybillNo(addDTO.getExpressWaybillNo());
        expressTrackRecord.setSort(addDTO.getSort());
        expressTrackRecord.setAction(addDTO.getAction());
        expressTrackRecord.setDescription(addDTO.getDescription());
        expressTrackRecord.setRemark(addDTO.getRemark());
        expressTrackRecord.setDelFlag(Constants.UNDELETED);
        expressTrackRecordMapper.insert(expressTrackRecord);
        return expressTrackRecord.getId();
    }

    @Override
    public Map<Long, String> getAllExpressNameMap() {
        List<ExpressNameDTO> expressNames = expressMapper.listAllExpressName();
        return expressNames.stream().collect(Collectors.toMap(ExpressNameDTO::getId,
                ExpressNameDTO::getExpressName));
    }

    @Override
    public List<ExpressTrackRecordDTO> listTrackRecord(Collection<String> expressWaybillNos) {
        if (CollUtil.isEmpty(expressWaybillNos)) {
            return ListUtil.empty();
        }
        List<ExpressTrackRecord> expressTrackRecords = expressTrackRecordMapper.selectList(
                Wrappers.lambdaQuery(ExpressTrackRecord.class)
                        .in(ExpressTrackRecord::getExpressWaybillNo, expressWaybillNos)
                        .eq(SimpleEntity::getDelFlag, Constants.UNDELETED)
//                        .orderByAsc(SimpleEntity::getCreateTime)
        );
        return BeanUtil.copyToList(expressTrackRecords, ExpressTrackRecordDTO.class);
    }
}

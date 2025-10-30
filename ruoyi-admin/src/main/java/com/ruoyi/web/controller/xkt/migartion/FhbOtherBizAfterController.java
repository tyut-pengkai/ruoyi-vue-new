package com.ruoyi.web.controller.xkt.migartion;

import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.framework.notice.fs.FsNotice;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtCateVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtProdSkuVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gtOnly.GtOnlyInitVO;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdMinPriceDTO;
import com.ruoyi.xkt.enums.EProductStatus;
import com.ruoyi.xkt.enums.ListingType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IPictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.WEIGHT_DEFAULT_ZERO;

/**
 * 只有GT处理 相关
 *
 * @author ruoyi
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/fhb-other-biz/after")
public class FhbOtherBizAfterController extends BaseController {

    final RedisCache redisCache;
    final StoreProductColorMapper prodColorMapper;
    final StoreProductColorSizeMapper prodColorSizeMapper;
    final StoreProductMapper storeProdMapper;
    final StoreProductStockMapper prodStockMapper;
    final StoreColorMapper storeColorMapper;
    final StoreMapper storeMapper;
    final StoreProductServiceMapper prodSvcMapper;
    final StoreProductCategoryAttributeMapper prodCateAttrMapper;
    final SysProductCategoryMapper prodCateMapper;
    final EsClientWrapper esClientWrapper;
    final IPictureService pictureService;
    final FsNotice fsNotice;



}

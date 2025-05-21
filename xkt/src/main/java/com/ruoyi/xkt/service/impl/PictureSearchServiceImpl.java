package com.ruoyi.xkt.service.impl;

import cn.hutool.core.lang.Assert;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.xkt.domain.PictureSearch;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.picture.ProductMatchDTO;
import com.ruoyi.xkt.dto.picture.SearchRequestDTO;
import com.ruoyi.xkt.mapper.PictureSearchMapper;
import com.ruoyi.xkt.mapper.SysFileMapper;
import com.ruoyi.xkt.service.IPictureSearchService;
import com.ruoyi.xkt.service.IPictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 以图搜款Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Slf4j
@Service
public class PictureSearchServiceImpl implements IPictureSearchService {
    @Autowired
    private PictureSearchMapper pictureSearchMapper;
    @Autowired
    private SysFileMapper sysFileMapper;
    @Autowired
    private IPictureService pictureService;
    @Autowired
    private RedisCache redisCache;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ProductMatchDTO> searchProductByPic(SearchRequestDTO requestDTO) {
        Assert.notEmpty(requestDTO.getPicKey());
        SysFile sysFile = new SysFile();
        sysFile.setFileUrl(requestDTO.getPicKey());
        sysFile.setFileSize(requestDTO.getPicSize());
        sysFile.setVersion(0);
        sysFile.setDelFlag(Constants.UNDELETED);
        sysFileMapper.insert(sysFile);
        PictureSearch pictureSearch = new PictureSearch();
        pictureSearch.setSearchFileId(sysFile.getId());
        pictureSearch.setUserId(requestDTO.getUserId());
        pictureSearch.setVoucherDate(new Date());
        pictureSearch.setVersion(0);
        pictureSearch.setDelFlag(Constants.UNDELETED);
        pictureSearchMapper.insert(pictureSearch);
        //搜索
        List<ProductMatchDTO> results = pictureService.searchProductByPicKey(requestDTO.getPicKey(),
                //默认30条
                Optional.ofNullable(requestDTO.getNum()).orElse(30));
        for (ProductMatchDTO result : results) {
            //匹配次数+1
            redisCache.valueIncr(CacheConstants.IMG_SEARCH_STATISTICS + result.getStoreProductId());
        }
        return results;
    }
}

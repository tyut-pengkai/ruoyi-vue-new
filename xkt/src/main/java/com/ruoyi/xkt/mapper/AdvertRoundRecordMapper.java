package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.AdvertRoundRecord;
import com.ruoyi.xkt.dto.advertRoundRecord.AdvertRoundRecordPageDTO;
import com.ruoyi.xkt.dto.advertRoundRecord.AdvertRoundRecordPageResDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 推广营销轮次历史记录Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface AdvertRoundRecordMapper extends BaseMapper<AdvertRoundRecord> {

    /**
     * 获取竞价失败记录列表
     *
     * @param pageDTO 查询入参
     * @return
     */
    List<AdvertRoundRecordPageResDTO> selectRecordPage(AdvertRoundRecordPageDTO pageDTO);

}

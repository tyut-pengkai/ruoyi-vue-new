package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.AdvertRound;
import com.ruoyi.xkt.dto.adminAdvertRound.AdminAdRoundPageDTO;
import com.ruoyi.xkt.dto.adminAdvertRound.AdminAdRoundPageResDTO;
import com.ruoyi.xkt.dto.adminAdvertRound.AdminAdRoundStatusCountResDTO;
import com.ruoyi.xkt.dto.advertRound.AdvertRoundStorePageDTO;
import com.ruoyi.xkt.dto.advertRound.AdvertRoundStorePageResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 推广营销轮次Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface AdvertRoundMapper extends BaseMapper<AdvertRound> {

    /**
     * 获取档口已订购的推广列表
     *
     * @param pageDTO 列表查询入参
     * @return List<AdvertRoundStorePageResDTO>
     */
    List<AdvertRoundStorePageResDTO> selectStoreAdvertPage(AdvertRoundStorePageDTO pageDTO);

    /**
     * 获取档口已订购的推广列表
     *
     * @param pageDTO 列表查询入参
     * @return List<AdvertRoundStorePageResDTO>
     */
    List<AdminAdRoundPageResDTO> selectAdminAdvertPage(AdminAdRoundPageDTO pageDTO);

    /**
     * 将参数直接null
     *
     * @param advertRoundId advertRoundId
     */
    Integer updateAttrNull(Long advertRoundId);

    /**
     * 获取最受欢迎的8个推广位
     *
     * @return List<Long>
     */
    List<Long> selectMostPopulars();

    /**
     * 推广状态数量
     *
     * @param sixMonthAgo 6个月前
     * @param now         当前时间
     * @return AdminAdRoundStatusCountResDTO
     */
    AdminAdRoundStatusCountResDTO statusCount(@Param("sixMonthAgo") Date sixMonthAgo, @Param("now") Date now);

}


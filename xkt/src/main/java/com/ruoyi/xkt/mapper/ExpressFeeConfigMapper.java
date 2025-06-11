package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.ExpressFeeConfig;
import com.ruoyi.xkt.dto.express.ExpressFeeConfigDTO;
import com.ruoyi.xkt.dto.express.ExpressFeeConfigListItemDTO;
import com.ruoyi.xkt.dto.express.ExpressFeeConfigQueryDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 12:48
 */
@Repository
public interface ExpressFeeConfigMapper extends BaseMapper<ExpressFeeConfig> {

    List<ExpressFeeConfigListItemDTO> listFeeConfig(ExpressFeeConfigQueryDTO queryDTO);

}

package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.Express;
import com.ruoyi.xkt.dto.express.ExpressNameDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 12:48
 */
@Repository
public interface ExpressMapper extends BaseMapper<Express> {

    List<ExpressNameDTO> listAllExpressName();

}

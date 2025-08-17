package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.InternalAccount;
import com.ruoyi.xkt.dto.account.TransDetailQueryDTO;
import com.ruoyi.xkt.dto.account.TransDetailStorePageItemDTO;
import com.ruoyi.xkt.dto.account.TransDetailUserPageItemDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 12:48
 */
@Repository
public interface InternalAccountMapper extends BaseMapper<InternalAccount> {
    /**
     * 获取内部账户（带锁）
     *
     * @param id
     * @return
     */
    InternalAccount getForUpdate(@Param("id") Long id);

    /**
     * 获取内部账户列表（带锁）
     *
     * @param ids
     * @return
     */
    List<InternalAccount> listForUpdate(@Param("ids") Collection<Long> ids);

    /**
     * 档口交易明细
     *
     * @param queryDTO
     * @return
     */
    List<TransDetailStorePageItemDTO> listStoreTransDetailPageItem(TransDetailQueryDTO queryDTO);

    /**
     * 卖家交易明细
     *
     * @param queryDTO
     * @return
     */
    List<TransDetailUserPageItemDTO> listUserTransDetailPageItem(TransDetailQueryDTO queryDTO);
}

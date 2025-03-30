package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.VoucherSequence;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 单据编号Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface VoucherSequenceMapper extends BaseMapper<VoucherSequence> {

    // 根据档口ID及当前单据类型查询单据编号
    @Select("SELECT * FROM voucher_sequence WHERE store_id = #{storeId} AND type = #{type} FOR UPDATE")
    VoucherSequence queryByStoreIdAndType(@Param("storeId") Long storeId, @Param("type") String type);

    // 根据档口ID及当前单据类型更新下一个单据编号
    @Update("UPDATE voucher_sequence SET nextSequence = 1")
    int resetNextSequenceEveryDay();

}

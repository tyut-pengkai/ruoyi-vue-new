package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.ShoppingCart;
import com.ruoyi.xkt.dto.userShoppingCart.ShopCartPageDTO;
import com.ruoyi.xkt.dto.userShoppingCart.ShopCartPageResDTO;

import java.util.List;

/**
 * 用户进货车Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    /**
     * 查询用户进货车列表
     * @param pageDTO 进货单列表查询入参
     * @return List<ShopCartPageResDTO>
     */
    List<ShopCartPageResDTO> selectShopCartPage(ShopCartPageDTO pageDTO);

}

package com.ruoyi.sale.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.sale.domain.SysSaleOrderItemCard;
import com.ruoyi.sale.mapper.SysSaleOrderItemCardMapper;
import com.ruoyi.sale.service.ISysSaleOrderItemCardService;
import com.ruoyi.system.domain.SysCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单卡密Service业务层处理
 *
 * @author zwgu
 * @date 2022-02-26
 */
@Service
public class SysSaleOrderItemCardServiceImpl implements ISysSaleOrderItemCardService {
    @Autowired
    private SysSaleOrderItemCardMapper sysSaleOrderItemCardMapper;

    /**
     * 查询订单卡密
     *
     * @param id 订单卡密主键
     * @return 订单卡密
     */
    @Override
    public SysSaleOrderItemCard selectSysSaleOrderItemCardById(Long id) {
        return sysSaleOrderItemCardMapper.selectSysSaleOrderItemCardById(id);
    }

    /**
     * 查询订单卡密列表
     *
     * @param sysSaleOrderItemCard 订单卡密
     * @return 订单卡密
     */
    @Override
    public List<SysSaleOrderItemCard> selectSysSaleOrderItemCardList(SysSaleOrderItemCard sysSaleOrderItemCard) {
        return sysSaleOrderItemCardMapper.selectSysSaleOrderItemCardList(sysSaleOrderItemCard);
    }

    /**
     * 新增订单卡密
     *
     * @param sysSaleOrderItemCard 订单卡密
     * @return 结果
     */
    @Transactional
    @Override
    public int insertSysSaleOrderItemCard(SysSaleOrderItemCard sysSaleOrderItemCard) {
        int rows = sysSaleOrderItemCardMapper.insertSysSaleOrderItemCard(sysSaleOrderItemCard);
        insertSysCard(sysSaleOrderItemCard);
        return rows;
    }

    /**
     * 修改订单卡密
     *
     * @param sysSaleOrderItemCard 订单卡密
     * @return 结果
     */
    @Transactional
    @Override
    public int updateSysSaleOrderItemCard(SysSaleOrderItemCard sysSaleOrderItemCard) {
        sysSaleOrderItemCardMapper.deleteSysCardByCardId(sysSaleOrderItemCard.getId());
        insertSysCard(sysSaleOrderItemCard);
        return sysSaleOrderItemCardMapper.updateSysSaleOrderItemCard(sysSaleOrderItemCard);
    }

    /**
     * 批量删除订单卡密
     *
     * @param ids 需要删除的订单卡密主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSysSaleOrderItemCardByIds(Long[] ids) {
        sysSaleOrderItemCardMapper.deleteSysCardByCardIds(ids);
        return sysSaleOrderItemCardMapper.deleteSysSaleOrderItemCardByIds(ids);
    }

    /**
     * 删除订单卡密信息
     *
     * @param id 订单卡密主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSysSaleOrderItemCardById(Long id) {
        sysSaleOrderItemCardMapper.deleteSysCardByCardId(id);
        return sysSaleOrderItemCardMapper.deleteSysSaleOrderItemCardById(id);
    }

    /**
     * 新增卡密信息
     *
     * @param sysSaleOrderItemCard 订单卡密对象
     */
    public void insertSysCard(SysSaleOrderItemCard sysSaleOrderItemCard) {
        List<SysCard> sysCardList = sysSaleOrderItemCard.getSysCardList();
        Long id = sysSaleOrderItemCard.getId();
        if (StringUtils.isNotNull(sysCardList)) {
            List<SysCard> list = new ArrayList<SysCard>();
            for (SysCard sysCard : sysCardList) {
                sysCard.setCardId(id);
                list.add(sysCard);
            }
            if (list.size() > 0) {
                sysSaleOrderItemCardMapper.batchSysCard(list);
            }
        }
    }
}

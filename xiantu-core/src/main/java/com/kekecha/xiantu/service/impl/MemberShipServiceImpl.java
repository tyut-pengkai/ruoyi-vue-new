package com.kekecha.xiantu.service.impl;

import com.kekecha.xiantu.domain.MemberShip;
import com.kekecha.xiantu.mapper.MemberShipMapper;
import com.kekecha.xiantu.service.IMemberShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberShipServiceImpl implements IMemberShipService {
    @Autowired
    MemberShipMapper memberShipMapper;

    public List<MemberShip> select()
    {
        return memberShipMapper.select();
    }

    public int insert(MemberShip memberShip)
    {
        return memberShipMapper.insert(memberShip);
    }

    public int update(MemberShip memberShip)
    {
        return memberShipMapper.update(memberShip);
    }

    public int delete(int id)
    {
        return memberShipMapper.delete(id);
    }
}

package com.kekecha.xiantu.mapper;

import com.kekecha.xiantu.domain.MemberShip;

import java.util.List;

public interface MemberShipMapper {
    List<MemberShip> select();
    int insert(MemberShip memberShip);
    int update(MemberShip memberShip);
    int delete(int id);
}

package com.kekecha.xiantu.service;

import com.kekecha.xiantu.domain.MemberShip;

import java.util.List;

public interface IMemberShipService {
    List<MemberShip> select();
    int insert(MemberShip memberShip);
    int update(MemberShip memberShip);
    int delete(int id);
}

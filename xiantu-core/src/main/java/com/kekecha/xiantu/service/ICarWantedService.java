package com.kekecha.xiantu.service;

import com.kekecha.xiantu.domain.CarWanted;

import java.util.List;

public interface ICarWantedService {
    List<CarWanted> select();
    int insert(CarWanted CarWanted);
    int update(CarWanted CarWanted);
    int delete(int id);
}

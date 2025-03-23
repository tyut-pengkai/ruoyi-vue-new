package com.kekecha.xiantu.mapper;

import com.kekecha.xiantu.domain.CarWanted;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarWantedMapper {
    List<CarWanted> select();
    int insert(CarWanted CarWanted);
    int update(CarWanted CarWanted);
    int delete(int id);
}

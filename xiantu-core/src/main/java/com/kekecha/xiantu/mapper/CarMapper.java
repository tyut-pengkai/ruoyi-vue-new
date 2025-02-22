package com.kekecha.xiantu.mapper;

import java.util.List;

import com.kekecha.xiantu.domain.*;

/**
 * 车型Mapper接口
 *
 * @author kekecha
 * @date 2025-02-16
 */
public interface CarMapper
{
    /**
     * 查询车型列表
     *
     * @return 车型列表
     */
    public List<CarOverview> selectCarOverviewList();


    public Car selectCarDetail(String name);

    public int insertCar(Car detail);


    public int updateCar(Car detail);


    public int deleteCar(String name);


}

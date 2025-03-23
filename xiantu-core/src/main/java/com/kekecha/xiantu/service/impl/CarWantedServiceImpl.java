package com.kekecha.xiantu.service.impl;

import com.kekecha.xiantu.domain.CarWanted;
import com.kekecha.xiantu.mapper.CarWantedMapper;
import com.kekecha.xiantu.service.ICarWantedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarWantedServiceImpl implements ICarWantedService {
    @Autowired
    CarWantedMapper carWantedMapper;

    public List<CarWanted> select()
    {
        return carWantedMapper.select();
    }

    public int insert(CarWanted carWanted)
    {
        return carWantedMapper.insert(carWanted);
    }

    public int update(CarWanted carWanted)
    {
        return carWantedMapper.update(carWanted);
    }

    public int delete(int id)
    {
        return carWantedMapper.delete(id);
    }
}

package com.kekecha.car.service;

import com.kekecha.car.domain.*;
import com.ruoyi.common.config.RuoYiConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 车型Service接口
 *
 * @author kekecha
 * date  2025-02-16
 */

public interface ICarService {
    /**
     * 查询车型列表
     *
     * @return 车型集合
     */
    List<CarOverview> selectCarOverviewList();

    Car selectCarDetail(String name);
    /**
     * 插入一条车辆信息
     *
     * @param detail 车型完整信息
     * @return 结果
     */
    int insertCar(Car detail);
    int updateCar(Car detail);
    int deleteCar(String name);

    public int removeRealPathFile(String imageFilePath);
    public Car appendImageToCar(String imageUploadMaskPath, String name);
    public Car removeImageFromCar(String imageUploadMaskPath, String name);
}

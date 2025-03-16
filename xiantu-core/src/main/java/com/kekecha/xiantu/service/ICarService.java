package com.kekecha.xiantu.service;

import com.kekecha.xiantu.domain.*;

import java.util.List;
import java.util.Map;

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
    Car selectCarDetailByID(int id);
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

    int removeRealPathFile(String imageFilePath);
    Car appendImageToCar(String imageUploadMaskPath, String name);
    Car removeImageFromCar(String imageUploadMaskPath, String name);

    Car buildCarFromJson(Map<String, Object> jsonMap);
    Map<String, Object> ConverCarToJson(Car car);
}

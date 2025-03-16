package com.kekecha.xiantu.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kekecha.xiantu.domain.*;
import com.kekecha.xiantu.service.ICarService;
import com.ruoyi.common.config.RuoYiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import com.kekecha.xiantu.mapper.CarMapper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CarServiceImpl implements ICarService
{
    @Autowired
    private CarMapper carMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Car buildCarFromJson(Map<String, Object> jsonMap)
    {
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals("imageUrl")) {
                /* 将image数组转变为字符串 */
                String imageUrl = value.toString();
                imageUrl = imageUrl.subSequence(1, imageUrl.length() - 1).toString().replace(" ","");
                jsonMap.put("imageUrl", imageUrl);
            }
            if (key.equals("videoUrl")) {
                String videoUrl = value.toString();
                videoUrl = videoUrl.subSequence(1, videoUrl.length() - 1).toString().replace(" ","");
                jsonMap.put("videoUrl", videoUrl);
            }
        }
        return objectMapper.convertValue(jsonMap, Car.class);
    }

    @Override
    public Map<String, Object> ConverCarToJson(Car car)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.convertValue(car, Map.class);

        /* 将字符串的imageUrl转换成数组 */
        if (jsonMap.get("imageUrl") != null) {
            String imageUrl = jsonMap.get("imageUrl").toString();
            if (imageUrl == null || imageUrl.isEmpty()) {
                jsonMap.put("imageUrl", new ArrayList<String>());
            } else {
                List<String> image_url_list = Arrays.asList(imageUrl.split(","));
                jsonMap.put("imageUrl", image_url_list);
            }
        } else {
            jsonMap.put("imageUrl", new ArrayList<String>());
        }

        if (jsonMap.get("videoUrl") != null) {
            String videoUrl = jsonMap.get("videoUrl").toString();
            if (videoUrl == null || videoUrl.isEmpty()) {
                jsonMap.put("videoUrl", new ArrayList<String>());
            } else {
                List<String> video_url_list = Arrays.asList(videoUrl.split(","));
                jsonMap.put("videoUrl", video_url_list);
            }
        } else {
            jsonMap.put("videoUrl", new ArrayList<String>());
        }

        return jsonMap;
    }

    @Override
    public int removeRealPathFile(String imageFilePath)
    {
        if (!imageFilePath.startsWith("/profile/upload/")) {
            return 1;
        }
        try {
            String imageRealPath = RuoYiConfig.getUploadPath() + imageFilePath.substring("/profile/upload".length());
//            System.out.println("imageRealPath : " + imageRealPath);
            Path path = Paths.get(imageRealPath);
            Files.delete(path);
        } catch (Exception e)
        {
//            System.out.println("exception : " + e.getMessage());
            return 2;
        }
        return 0;
    }

    @Override
    public Car appendImageToCar(String imageUploadMaskPath, String name)
    {
        Car car = selectCarDetail(name);
        if (car == null) {
            return null;
        }

        /* 将图片增加到车型中 */
        String imageUrl = car.getImageUrl();
        /* 图片列表不为空，要做字符串拼接 */
        if (imageUrl != null) {
            if (imageUrl.contains(",")) {
                String[] url = imageUrl.split(",");
                /* 多串 */
                String[] newUrls = Arrays.copyOf(url, url.length + 1);
                newUrls[url.length] = imageUploadMaskPath;
                car.setImageUrl(String.join(",", newUrls));
            } else {
                /* 单串 */
                String[] newUrls = {imageUrl, imageUploadMaskPath};
                car.setImageUrl(String.join(",", newUrls));
            }
        } else {
            /* 空串 */
            car.setImageUrl(imageUploadMaskPath);
        }
        updateCar(car);
        return car;
    }

    public Car removeImageFromCar(String imageUploadMaskPath, String name)
    {
        Car car = selectCarDetail(name);
        if (car == null) {
            return null;
        }

        /* 从车型中将该图片抹除 */
        String imageUrl = car.getImageUrl();

        if (imageUrl != null) {
            if (imageUrl.contains(",")) {
                if (imageUrl.contains(imageUploadMaskPath)) {
                    String[] filePath = imageUrl.split(",");
                    String[] newFilePath = new String[filePath.length - 1];
                    /* 删除关联的图片 */
                    int j = 0;
                    for (int i = 0; i < filePath.length; i++) {
                        if (filePath[i].equals(imageUploadMaskPath)) {
                            continue;
                        }
                        newFilePath[j++] = filePath[i];
                    }
                    car.setImageUrl(String.join(",", newFilePath));
                } else {
                    ;
                }
            } else {
                if (imageUrl.equals(imageUploadMaskPath)) {
                    car.setImageUrl(null);
                } else {
                    ;
                }
            }
        }
        updateCar(car);
        return car;
    }

    @Override
    public List<CarOverview> selectCarOverviewList()
    {
        List<CarOverview> list = carMapper.selectCarOverviewList();
        for (CarOverview carOverview : list) {
            String imageUrl = carOverview.getImage();
            /* 剪裁只返回第一个图片 */
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String[] url = imageUrl.split(",");
                carOverview.setImage(url[0]);
            } else {
                carOverview.setImage(null);
            }
        }
        return list;
    }

    @Override
    public Car selectCarDetail(String name) {
        return carMapper.selectCarDetail(name);
    }

    @Override
    public Car selectCarDetailByID(int id)  {
        return carMapper.selectCarDetailByID(id);
    }

    @Override
    public int insertCar(Car detail) {
        return carMapper.insertCar(detail);
    }

    @Override
    public int updateCar(Car detail) {
        return carMapper.updateCar(detail);
    }

    @Override
    public int deleteCar(String name) {
        Car car = selectCarDetail(name);
        if (car != null) {
            String imageUrl = car.getImageUrl();
            if (imageUrl != null) {
                String[] filePath = imageUrl.split(",");
                /* 删除关联的图片 */
                for (int i = 0; i < filePath.length; i++) {
                    if (filePath[i].isEmpty()) {
                        continue;
                    }
                    removeRealPathFile(filePath[i]);
                }
            }
        }
        return carMapper.deleteCar(name);
    }
}

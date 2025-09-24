package com.ruoyi.web.controller.xkt.migartion.vo.fhb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * FhbProdVO 数据检查器
 */
public class FhbProdChecker {

    /**
     * 检查同一 artNo 是否有多个相同颜色
     * 
     * @param prodVO FhbProdVO 对象
     * @return 如果同一 artNo 有多个相同颜色则返回 true，否则返回 false
     */
    public static boolean hasDuplicateColorsForSameArtNo(FhbProdVO prodVO) {
        if (prodVO == null || prodVO.getData() == null || prodVO.getData().getRecords() == null) {
            return false;
        }

        // 按 artNo 分组记录
        Map<String, List<FhbProdVO.SMIVO>> artNoRecordsMap = new HashMap<>();
        
        // 将记录按 artNo 分组
        for (FhbProdVO.SMIVO record : prodVO.getData().getRecords()) {
            if (record.getArtNo() != null) {
                artNoRecordsMap.computeIfAbsent(record.getArtNo(), k -> new ArrayList<>()).add(record);
            }
        }
        
        // 检查每个 artNo 是否有重复颜色
        for (Map.Entry<String, List<FhbProdVO.SMIVO>> entry : artNoRecordsMap.entrySet()) {
            List<FhbProdVO.SMIVO> records = entry.getValue();
            Set<String> colorSet = new HashSet<>();
            
            // 检查当前 artNo 的记录中是否有重复颜色
            for (FhbProdVO.SMIVO record : records) {
                if (record.getColor() != null) {
                    if (!colorSet.add(record.getColor())) {
                        // 如果添加失败，说明颜色已存在，即有重复
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * 获取所有具有重复颜色的 artNo 列表
     * 
     * @param prodVO FhbProdVO 对象
     * @return 具有重复颜色的 artNo 列表
     */
    public static List<String> getArtNoWithDuplicateColors(FhbProdVO prodVO) {
        List<String> result = new ArrayList<>();
        
        if (prodVO == null || prodVO.getData() == null || prodVO.getData().getRecords() == null) {
            return result;
        }

        // 按 artNo 分组记录
        Map<String, List<FhbProdVO.SMIVO>> artNoRecordsMap = new HashMap<>();
        
        // 将记录按 artNo 分组
        for (FhbProdVO.SMIVO record : prodVO.getData().getRecords()) {
            if (record.getArtNo() != null) {
                artNoRecordsMap.computeIfAbsent(record.getArtNo(), k -> new ArrayList<>()).add(record);
            }
        }
        
        // 检查每个 artNo 是否有重复颜色
        for (Map.Entry<String, List<FhbProdVO.SMIVO>> entry : artNoRecordsMap.entrySet()) {
            String artNo = entry.getKey();
            List<FhbProdVO.SMIVO> records = entry.getValue();
            Set<String> colorSet = new HashSet<>();
            boolean hasDuplicate = false;
            
            // 检查当前 artNo 的记录中是否有重复颜色
            for (FhbProdVO.SMIVO record : records) {
                if (record.getColor() != null) {
                    if (!colorSet.add(record.getColor())) {
                        // 如果添加失败，说明颜色已存在，即有重复
                        hasDuplicate = true;
                        break;
                    }
                }
            }
            
            if (hasDuplicate) {
                result.add(artNo);
            }
        }
        
        return result;
    }
}
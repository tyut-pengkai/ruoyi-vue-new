package com.ruoyi.common.utils;

import java.util.*;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/8/1 14:35
 */
public class AdValidator {

    /**
     * 检查标题是否包含任意敏感词（全词匹配）
     *
     * @param prodTitle 待校验的标题
     * @return true-包含敏感词, false-不包含
     */
    public static boolean containsProhibitedWord(String prodTitle) {
        for (String word : AdValidator.PROHIBITED_WORDS_SET) {
            if (prodTitle.contains(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查标题并返回匹配到的所有敏感词
     *
     * @param prodTitle 待校验的标题
     * @return 匹配到的敏感词列表（若无则返回空列表）
     */
    public static List<String> findProhibitedWords(String prodTitle) {
        List<String> matchedWords = new ArrayList<>();
        for (String word : AdValidator.PROHIBITED_WORDS_SET) {
            if (prodTitle.contains(word)) {
                matchedWords.add(word);
            }
        }
        return matchedWords;
    }

    // 使用 HashSet 存储违禁词（全局唯一，避免重复初始化）
    private static final Set<String> PROHIBITED_WORDS_SET = new HashSet<>(Arrays.asList(
            "最", "最佳", "最优", "最好", "最大", "最高", "最便宜", "最时尚", "最舒适", "最流行",
            "最先进", "最顶级", "首选", "唯一", "独家", "全网第一", "行业领先", "销量冠军",
            "100%", "绝对", "无敌", "完美", "终极", "极致", "巅峰", "史无前例", "遥遥领先",
            "微信", "QQ", "加V", "私聊", "联系客服", "电话", "下单", "购买",
            "小红书", "抖音", "微博", "官网", "网址", "二维码"
    ));

}

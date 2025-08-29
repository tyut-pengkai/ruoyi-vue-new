package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推广营销风格类型
 *
 * @author liujiang
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum AdStyleType {

    ELEGANT_CONCISE_FLOW(1, "优雅精致风"),
    RELAXED_MINIMALISM_FLOW(2, "休闲简约风"),
    CLASSIC_FLOW(3, "复古风"),
    PROFESSIONAL_COMMUTE_FLOW(4, "职业通勤风"),
    MINIMALISM_FLOW(5, "极简主义风"),
    COLLEGE_FLOW(6, "学院风"),
    CLASSY_WORK_FLOW(7, "简约职场风"),
    BANQUET_NIGHT_FLOW(8, "宴会晚装风"),
    STREET_CURRENT_FLOW(9, "街头潮流风"),
    SPORTS_FLOW(10, "运动风"),
    CUTE_AND_SWEET_FLOW(11, "可爱甜美风"),
    LORDA_FLOW(12, "洛丽塔风"),
    ROMANTIC_GIRL_FLOW(13, "浪漫少女风"),
    FUTURE_FLOW(14, "未来主义风"),
    SEXY_FLOW(15, "性感魅惑风"),
    GOTHIC_FLOW(16, "哥特风"),
    PONK_FLOW(17, "朋克风"),
    TRAVEL_FLOW(18, "度假风"),
    TOWN_FLOW(19, "田园风"),
    MILITARY_FLOW(20, "军旅风"),
    CHEN_FLOW(21, "禅意风"),
    CLASSIC_SPORT_FLOW(22, "复古运动风"),
    MODERN_STYLE_FLOW(23, "摩登时髦风"),
    MIX_FLOW(24, "混搭风"),

    ;

    private final Integer value;
    private final String label;

    public static AdStyleType of(Integer value) {
        for (AdStyleType e : AdStyleType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        // 默认风格
        return AdStyleType.ELEGANT_CONCISE_FLOW;
    }
}

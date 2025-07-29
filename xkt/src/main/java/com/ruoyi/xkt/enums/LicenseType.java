package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 营业执照 类型
 * @author 刘江
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum LicenseType {

    LIMITED_LIABILITY_COMPANY(1, "有限责任公司"),
    JOINT_STOCK_COMPANY(2, "股份有限公司"),
    STATE_OWNED_COMPANY(3, "国有独资公司"),
    ONE_PERSON_COMPANY_NATURAL(4, "一人有限责任公司（自然人独资）"),
    ONE_PERSON_COMPANY_LEGAL(5, "一人有限责任公司（法人独资）"),
    FOREIGN_INVESTED_LLC(6, "外商投资有限责任公司"),
    SINO_FOREIGN_EQUITY_JVC(7, "中外合资经营企业（有限责任公司形式）"),
    SINO_FOREIGN_CONTRACTUAL_JVC(8, "中外合作经营企业（有限责任公司形式）"),
    WHOLLY_FOREIGN_OWNED_ENTERPRISE(9, "外资企业（有限责任公司形式）"),
    HMT_DOMESTIC_JOINT_LLC(10, "台港澳与境内合资有限责任公司"),
    HMT_DOMESTIC_COOPERATIVE_LLC(11, "台港澳与境内合作有限责任公司"),
    HMT_SOLELY_OWNED_LLC(12, "台港澳独资有限责任公司"),
    LISTED_COMPANY(13, "上市公司（股份有限公司形式）"),
    UNLISTED_JSC(14, "非上市股份有限公司"),
    WHOLE_PEOPLE_OWNED(15, "全民所有制企业"),
    COLLECTIVE_OWNED(16, "集体所有制企业"),
    SHARE_COOPERATIVE(17, "股份合作制企业"),
    JOINT_OPERATION_ENTERPRISE(18, "联营企业"),
    GENERAL_PARTNERSHIP(19, "普通合伙企业"),
    SPECIAL_GENERAL_PARTNERSHIP(20, "特殊普通合伙企业"),
    LIMITED_PARTNERSHIP(21, "有限合伙企业"),
    FOREIGN_INVESTED_PARTNERSHIP(22, "外商投资合伙企业"),
    INDIVIDUAL_ENTERPRISE(23, "个人独资企业"),
    FOREIGN_INDIVIDUAL_ENTERPRISE(24, "外商投资个人独资企业"),
    BRANCH_COMPANY(25, "分公司"),
    FOREIGN_INVESTED_BRANCH(26, "外商投资企业分支机构"),
    HMT_INVESTED_BRANCH(27, "台港澳投资企业分支机构"),
    FARMERS_COOPERATIVE(28, "农民专业合作社"),
    FARMERS_COOPERATIVE_BRANCH(29, "农民专业合作社分支机构"),
    INDIVIDUAL_BUSINESS(30, "个体工商户"),
    FOREIGN_REPRESENTATIVE_OFFICE(31, "外国（地区）企业常驻代表机构"),
    FOREIGN_COMPANY_OPERATIONS(32, "外国（地区）企业在中国境内从事经营活动"),
    ENTERPRISE_GROUP(33, "企业集团")

    ;

    private final Integer value;
    private final String label;

    public static LicenseType of(Integer value) {
        for (LicenseType e : LicenseType.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}

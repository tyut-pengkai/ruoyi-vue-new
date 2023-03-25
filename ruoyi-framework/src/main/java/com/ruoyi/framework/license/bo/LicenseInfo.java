package com.ruoyi.framework.license.bo;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.license.bo.LicenseCheckModel;
import com.ruoyi.common.utils.DateUtils;
import de.schlichtherle.license.LicenseContent;

import java.util.Date;

public class LicenseInfo {

    private Date from;
    private Date to;
    private String licenseType = "未授权";
    private String appLimit = "-";
    private String maxOnline = "-";
    private String licenseTo = "-";
    private String licenseDomain = "-";
    private String licenseIp = "-";
    private String datetime = "0000-00-00 00:00:00 - 0000-00-00 00:00:00 (0天0小时0分钟)";

    public static LicenseInfo getLicenseInfo() {
        LicenseContent license = Constants.LICENSE_CONTENT;
        LicenseInfo info = new LicenseInfo();
        if (license != null) {
            Date from = license.getNotBefore();
            Date to = license.getNotAfter();
            LicenseCheckModel extra = (LicenseCheckModel) license.getExtra();
            info.setFrom(from);
            info.setTo(to);
            info.setLicenseTo(extra.getLicenseTo());
            info.setLicenseType(extra.getLicenseType());
            info.setAppLimit(extra.getAppLimit() == -1 ? "*": extra.getAppLimit().toString());
            info.setMaxOnline(extra.getMaxOnline() == -1 ? "*": extra.getMaxOnline().toString());
            info.setLicenseDomain(String.join("/", extra.getDomainName()));
            info.setLicenseIp(String.join("/", extra.getIpAddress()));
            info.setDatetime(DateUtils.parseDateToStr(from) + " - " + DateUtils.parseDateToStr(to) +
                    " (剩余" + DateUtils.timeDistance(to, DateUtils.getNowDate()) + ")");
        }
        return info;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenceType) {
        this.licenseType = licenceType;
    }

    public String getAppLimit() {
        return appLimit;
    }

    public void setAppLimit(String appLimit) {
        this.appLimit = appLimit;
    }

    public String getMaxOnline() {
        return maxOnline;
    }

    public void setMaxOnline(String maxOnline) {
        this.maxOnline = maxOnline;
    }

    public String getLicenseTo() {
        return licenseTo;
    }

    public void setLicenseTo(String licenseTo) {
        this.licenseTo = licenseTo;
    }

    public String getLicenseDomain() {
        return licenseDomain;
    }

    public void setLicenseDomain(String licenseDomain) {
        this.licenseDomain = licenseDomain;
    }

    public String getLicenseIp() {
        return licenseIp;
    }

    public void setLicenseIp(String licenseIp) {
        this.licenseIp = licenseIp;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}

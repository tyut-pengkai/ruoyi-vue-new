package com.ruoyi;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.enums.LicenseType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import de.schlichtherle.license.LicenseContent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.Date;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RuoYiApplication {
    public static void main(String[] args) {

        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
//        RuoYiConfig config = SpringUtils.getBean(RuoYiConfig.class);

        try {
            LicenseContent license = Constants.LICENSE_CONTENT;
            Date from = null;
            Date to = null;
            String licenceType = "未授权";
            String appLimit = "未授权";
            String maxOnline = "未授权";
            String datetime = "0000-00-00 00:00:00 - 0000-00-00 00:00:00 (0天0小时0分钟)";
            if (license != null) {
                from = license.getNotBefore();
                to = license.getNotAfter();
                licenceType = LicenseType.valueOfCode(license.getConsumerType()).getInfo();
                appLimit = "无限制";
                maxOnline = String.valueOf(license.getConsumerAmount()) + " 人";
                datetime = DateUtils.parseDateToStr(from) + " - " + DateUtils.parseDateToStr(to) +
                        " (" + DateUtils.getDatePoor(to, from) + ")";
            }

            System.out.format(
                    "===============================================================================================\n" +
                            "//                                        服务器特征信息                                          \n" +
                            "===============================================================================================\n" +
                            "//              机　器　码: %-40.40s \n" +
                            "//              服务器名称: %-24.24s 服务器IP: %-24.24s \n" +
                            "===============================================================================================\n" +
                            "//                                        服务器授权信息                                          \n" +
                            "===============================================================================================\n" +
                            "//              授权类型: %-12.12s 软件数量限制: %-12.12s 最大同时在线: %-12.12s \n" +
                            "//              授权有效期限: %-80.80s \n" +
                            "===============================================================================================\n" +
                            ">>: 系统启动成功\n",
                    Constants.SERVER_SN, IpUtils.getHostName(), IpUtils.getHostIp(),
                    licenceType, appLimit, maxOnline, datetime);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">>: 系统启动失败\n");
        }
    }
}

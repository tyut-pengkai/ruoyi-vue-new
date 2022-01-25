package com.ruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication
{
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
//        RuoYiConfig config = SpringUtils.getBean(RuoYiConfig.class);
        System.out.println(
                "===============================================================================================\n" +
                        "//                                        服务器特征信息                                          \n" +
                        "===============================================================================================\n" +
                        "//                     机器码: ${machineCode}         校验码: ${checkCode}                        \n" +
                        "//                     服务器名称: ${serverName}       服务器IP: ${serverIp}                       \n" +
                        "===============================================================================================\n" +
                        "//                                        服务器授权信息                                          \n" +
                        "===============================================================================================\n" +
                        "//                     授权类型: 个人                   最大在线人数: 无限制                          \n" +
                        "//                     授权有效期: 2022/01/01 00:00:00 - 2023/01/01 00:00:00                     \n" +
                        "===============================================================================================\n" +
                        ">>: 系统启动成功\n"
        );
    }
}

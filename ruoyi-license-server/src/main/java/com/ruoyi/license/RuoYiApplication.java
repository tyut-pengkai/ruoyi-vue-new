package com.ruoyi.license;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

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
        RuoYiConfig config = SpringUtils.getBean(RuoYiConfig.class);
        System.out.println(" 系统启动成功，当前版本：" + config.getVersion() + " (" + config.getVersionNo() + ")" + " \n" +
                "   _____ ____   ____  _____  _____   _____  ____  ______ _______ _____ ____  __  __ \n" +
                "  / ____/ __ \\ / __ \\|  __ \\|  __ \\ / ____|/ __ \\|  ____|__   __/ ____/ __ \\|  \\/  |\n" +
                " | |   | |  | | |  | | |__) | |  | | (___ | |  | | |__     | | | |   | |  | | \\  / |\n" +
                " | |   | |  | | |  | |  _  /| |  | |\\___ \\| |  | |  __|    | | | |   | |  | | |\\/| |\n" +
                " | |___| |__| | |__| | | \\ \\| |__| |____) | |__| | |       | |_| |___| |__| | |  | |\n" +
                "  \\_____\\____/ \\____/|_|  \\_\\_____/|_____/ \\____/|_|       |_(_)\\_____\\____/|_|  |_|\n");
    }
}

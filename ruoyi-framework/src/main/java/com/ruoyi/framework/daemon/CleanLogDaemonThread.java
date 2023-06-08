package com.ruoyi.framework.daemon;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.FileSizeUtils;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.system.mapper.PublicSqlMapper;
import com.ruoyi.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableAsync
@Slf4j
public class CleanLogDaemonThread {

    private final Long G = (long) 1024 * 1024 * 1024; // G
    private final Long M = (long) 1024 * 1024; // M
    private final Integer H = 3600 * 1000;
    @Resource
    private ISysConfigService configService;
    @Resource
    private PublicSqlMapper publicSqlMapper;

    @Async
    public void cleanLog() {

        // 模拟日志文件增长
//        new Thread(() -> {
//            while(true) {
//                for (int i = 0; i < 10000; i++) {
//                    try {
//                        FileUtils.writeStringToFile(new File(PathUtils.getUserPath() + File.separator + "nohup.out"), RandomStringUtils.randomAlphanumeric(1000000), StandardCharsets.UTF_8, true);
//                        FileUtils.writeStringToFile(new File("D:\\Workspaces\\IdeaProjects\\RuoYi-Vue\\logs\\sys-error.log"),
//                                RandomStringUtils.randomAlphanumeric(1000000), StandardCharsets.UTF_8, true);
//                        Thread.sleep(1000);
//                    } catch (IOException | InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();

        Map<String, String> tablePkMap = new HashMap<>();
        tablePkMap.put("sys_oper_log", "oper_id");
        tablePkMap.put("sys_logininfor", "info_id");
        tablePkMap.put("sys_app_logininfor", "info_id");
        tablePkMap.put("sys_app_trial_logininfor", "info_id");
//        tablePkMap.put("test", "info_id");

        while (true) {
            try {
                String configCleanLogCycle = configService.selectConfigByKey("sys.log.cleanLogCycle");
                Integer cleanLogCycle = Convert.toInt(configCleanLogCycle, 60);
                if (cleanLogCycle > 0) {
                    // 检查日志文件
                    log.info("[自动清理]开始检查日志文件");
                    File nohup = new File(PathUtils.getUserPath() + File.separator + "nohup.out");
                    File logs = new File(PathUtils.getUserPath() + File.separator + "logs");
                    checkLogFile(nohup);
                    if (logs.exists() && logs.isDirectory()) {
                        Collection<File> files = FileUtils.listFiles(logs, new String[]{"log"}, false);
                        for (File file : files) {
                            checkLogFile(file);
                        }
                    }
                    log.info("[自动清理]日志文件检查完毕");

                    // 检查日志表
                    log.info("[自动清理]开始检查日志表");
                    String configCheanLogTableThreshold = configService.selectConfigByKey("sys.log.cheanLogTableThreshold");
                    Integer cleanLogTableThreshold = Convert.toInt(configCheanLogTableThreshold, 100000);
//                    cleanLogTableThreshold = 10;
                    if (cleanLogTableThreshold > 0) {
                        for (Map.Entry<String, String> entry : tablePkMap.entrySet()) {
                            String logTable = entry.getKey();
                            String pk = entry.getValue();
                            String sql1 = "select count(1) from " + logTable;
                            Object o1 = publicSqlMapper.nativeSql(sql1);
                            long row = Long.parseLong(String.valueOf(o1));
                            if (row > cleanLogTableThreshold) {
                                log.info("[自动清理]日志数据表过大，执行自动清理：数据表：{}，数据量：{}条", logTable, row);
                                // 取最新的记录最小行号
                                String sql2 = "select " + pk + " from " + logTable + " order by " + pk + " desc limit " + cleanLogTableThreshold;
                                log.info(sql2);
                                List<Long> idList = publicSqlMapper.select2(sql2, Long.class);
                                Long min = idList.stream().min(Long::compareTo).get();
                                log.info("[自动清理]本次清理最小ID为{}（不包含）", min);
                                String sql3 = "delete from " + logTable + " where " + pk + " < " + min;
                                log.info(sql3);
                                int o3 = publicSqlMapper.delete(sql3);
                                log.info("[自动清理]自动清理完毕：数据表：{}，数据量：{}条，清理{}条，剩余{}条", logTable, row, o3, row - o3);
                            } else {
//                                log.info("[自动清理]数据表：{}无需清理", logTable);
                            }
                        }
                    }
                    log.info("[自动清理]日志表检查完毕");
                    sleep(cleanLogCycle * 60 * 1000);
                } else {
                    log.info("[自动清理]自动清理功能已关闭，无需清理");
                    sleep(H);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sleep(H);
            }
        }
    }

    private void checkLogFile(File logFile) throws IOException {
        String configCleanLogFileThreshold = configService.selectConfigByKey("sys.log.cleanLogFileThreshold");
        Integer cleanLogFileThreshold = Convert.toInt(configCleanLogFileThreshold, 1024);
        if (cleanLogFileThreshold > 0) {
            if (logFile.exists() && logFile.isFile() && logFile.length() > cleanLogFileThreshold * M) {
                log.info("[自动清理]日志文件过大，执行自动清理：文件：{}，大小：{}", logFile.getCanonicalPath(), FileSizeUtils.readableFileSize(logFile.length()));
                FileUtils.writeStringToFile(logFile, "", StandardCharsets.UTF_8, false);
            } else {
//                log.info("[自动清理]日志文件大小正常，无需清理：文件：{}，大小：{}", logFile.getCanonicalPath(), FileSizeUtils.readableFileSize(logFile.length()));
            }
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

}

package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
@Slf4j
public class SysSystemController extends BaseController {

    private boolean inProgress = false;
    private AjaxResult result = AjaxResult.success();

    @PreAuthorize("@ss.hasAnyRoles('admin,sadmin')")
    @RequestMapping("/restart/doRestart")
    public AjaxResult doUpdate() {
        new Thread(() -> {
            log.info("红叶系统重启");
            try {
                // 重启服务
                new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                        String command = "";
                        String osName = System.getProperty("os.name");
                        command = "cmd /c start \"D:\\网络验证\\红叶\\release\\红叶网络验证系统_v1.0.0_20221018203100_test_update\\upload\\hy.bat\"";
                        if ("jar".equals(SysSystemController.class.getResource("").getProtocol())) {
                            // 以 jar 的方式运行
                            if (osName.startsWith("Windows")) {
                                command = "cmd /c start \"hy.bat\"";
                            } else if (osName.startsWith("Linux")) {
                                command = "sh hy.sh restart";
                            }
                        }
                        log.info("执行重启指令：" + command);
                        CommandLine commandLine = CommandLine.parse(command);
                        DefaultExecutor exec = new DefaultExecutor();
                        exec.execute(commandLine, new DefaultExecuteResultHandler());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                result = AjaxResult.success("即将重启，服务将中断2~3分钟");
            } catch (Exception e) {
                e.printStackTrace();
                log.info("重启出错：" + e.getMessage());
                result = AjaxResult.success("重启出错，请稍后重试：" + e.getMessage());
            } finally {
                inProgress = false;
            }
        }).start();
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,sadmin')")
    @RequestMapping("/restart/status")
    public AjaxResult status() {
        if (inProgress) {
            return AjaxResult.success("重启中").put("finish", 0);
        } else {
            return result.put("finish", 1);
        }
    }

}

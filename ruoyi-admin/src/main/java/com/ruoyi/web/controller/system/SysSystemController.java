package com.ruoyi.web.controller.system;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ZipUtil;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;
import java.util.UUID;

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

    @PreAuthorize("@ss.hasAnyRoles('admin,sadmin')")
    @RequestMapping("/exportErrorLog")
    public AjaxResult exportErrorLog() {
        try {
            // 取临时目录
            File tempFile = File.createTempFile("temp", ".temp");
            // 压缩文件中包含的文件列表,此处为测试代码,实际为自己需要的文件列表
            List<File> fileList = CollUtil.newArrayList();
            String pathPrefix = PathUtils.getUserPath() + File.separator + "logs" + File.separator;
            fileList.add(new File(pathPrefix + "sys-error.log"));
            fileList.add(new File(pathPrefix + "sys-info.log"));
            fileList.add(new File(pathPrefix + "sys-user.log"));
            fileList.add(new File(PathUtils.getUserPath() + File.separator + "nohup.out"));
            // 压缩多个文件,压缩后会将压缩临时文件删除
            ZipUtil.zip(tempFile, false, fileList.stream().filter(File::exists).toArray(File[]::new));
//            String destFileName = "__error_log_" + DateUtils.dateTimeNow() +".zip";
            String destFileName = UUID.randomUUID() + "_" + "error_log_" + DateUtils.dateTimeNow() +".zip";
            FileUtils.copyFile(tempFile, new File(RuoYiConfig.getDownloadPath() + File.separator + destFileName));
            return AjaxResult.success(destFileName);
        }catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ErrorCode.ERROR_OTHER_FAULTS, e.getMessage());
        }
    }

}

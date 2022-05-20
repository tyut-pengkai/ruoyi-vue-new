package com.ruoyi.api.v1.utils;

import com.ruoyi.common.enums.ScriptLanguage;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.vo.ScriptResultVo;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ScriptUtils {

    public static ScriptResultVo exec(String scriptContent, ScriptLanguage language, String params) {
        String execPrefix = "";
        if (language == ScriptLanguage.JAVA_SCRIPT) {
            execPrefix = "node";
        } else if (language == ScriptLanguage.PYTHON2) {
            execPrefix = "python2";
        } else if (language == ScriptLanguage.PYTHON3) {
            execPrefix = "python";
        } else if (language == ScriptLanguage.PHP) {
            execPrefix = "php";
        } else {
            throw new ServiceException("暂未支持的脚本语言" + language.getInfo());
        }
        if (StringUtils.isBlank(execPrefix)) {
            throw new ServiceException("未指定脚本语言");
        }
        File tempFile;
        try {
            // 生成临时脚本文件
            tempFile = File.createTempFile("hyScriptTempFile" + System.currentTimeMillis(), null);
            FileUtils.write(tempFile, scriptContent, "utf-8");
            String command = execPrefix + " " + tempFile.getCanonicalPath();
            if (StringUtils.isNotBlank(command)) {
                command += " " + params;
            }
            //接收正常结果流
            ByteArrayOutputStream susStream = new ByteArrayOutputStream();
            //接收异常结果流
            ByteArrayOutputStream errStream = new ByteArrayOutputStream();
            CommandLine commandLine = CommandLine.parse(command);
            DefaultExecutor exec = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(susStream, errStream);
            exec.setStreamHandler(streamHandler);
            int code = exec.execute(commandLine);
            // 不同操作系统注意编码，否则结果乱码
            String suc = susStream.toString("UTF-8");
            String err = errStream.toString("UTF-8");
            return new ScriptResultVo(code, suc, err);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

}

package com.ruoyi.system.service;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.enums.ScriptLanguage;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysGlobalScript;
import com.ruoyi.system.domain.vo.ScriptResultVo;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
public class RemoteFunctionTest {

    @Resource
    private ISysGlobalScriptService globalScriptService;

    public ScriptResultVo exec(String scriptContent, ScriptLanguage language) {
        String execPrefix = "";
        if (language == ScriptLanguage.JAVA_SCRIPT) {
            execPrefix = "node";
        } else if (language == ScriptLanguage.PYTHON2) {
            execPrefix = "python";
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
        File tempFile = null;
        try {
            // 生成临时脚本文件
            tempFile = File.createTempFile("hyScriptTempFile" + System.currentTimeMillis(), null);
            FileUtils.write(tempFile, scriptContent, "utf-8");
            String command = execPrefix + " " + tempFile.getCanonicalPath();
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

    @Test
    public void test() {
        SysGlobalScript script = globalScriptService.selectSysGlobalScriptByScriptId(1L);
        System.out.println(script.getContent());
        ScriptResultVo scriptResult = exec(script.getContent(), script.getLanguage());
        System.out.println(JSON.toJSONString(scriptResult));
    }

    @Test
    public void test2() {
        System.out.println("你".getBytes(StandardCharsets.UTF_8).length);
    }
}
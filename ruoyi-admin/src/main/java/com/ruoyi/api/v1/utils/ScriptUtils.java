package com.ruoyi.api.v1.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.api.v1.api.noAuth.general.GlobalScript;
import com.ruoyi.api.v1.domain.vo.*;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.enums.ScriptLanguage;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.vo.ScriptResultVo;
import com.ruoyi.system.service.ISysAppTrialUserService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysConfigService;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptUtils {

    public static ScriptResultVo exec(String scriptContent, ScriptLanguage language, String params) {

        ISysConfigService configService = SpringUtils.getBean(ISysConfigService.class);
        String execPrefixJavaScript = configService.selectConfigByKey("sys.globalScript.execPrefix.javaScript");
        String execPrefixPython2 = configService.selectConfigByKey("sys.globalScript.execPrefix.python2");
        String execPrefixPython3 = configService.selectConfigByKey("sys.globalScript.execPrefix.python3");
        String execPrefixPHP = configService.selectConfigByKey("sys.globalScript.execPrefix.php");

        execPrefixJavaScript = StringUtils.isNotBlank(execPrefixJavaScript) ? execPrefixJavaScript : "node";
        execPrefixPython2 = StringUtils.isNotBlank(execPrefixPython2) ? execPrefixPython2 : "python";
        execPrefixPython3 = StringUtils.isNotBlank(execPrefixPython3) ? execPrefixPython3 : "python3";
        execPrefixPHP = StringUtils.isNotBlank(execPrefixPHP) ? execPrefixPHP : "php";

        String execPrefix = "";
        if (language == ScriptLanguage.JAVA_SCRIPT) {
            execPrefix = execPrefixJavaScript;
        } else if (language == ScriptLanguage.PYTHON2) {
            execPrefix = execPrefixPython2;
        } else if (language == ScriptLanguage.PYTHON3) {
            execPrefix = execPrefixPython3;
        } else if (language == ScriptLanguage.PHP) {
            execPrefix = execPrefixPHP;
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
            if (isMessyCode(suc) || isMessyCode(err)) {
                try {
                    suc = susStream.toString("GBK");
                    err = errStream.toString("GBK");
                } catch (UnsupportedEncodingException ignored) {
                }
            }
            return new ScriptResultVo(code, suc, err);
        } catch (ExecuteException e) {
            return new ScriptResultVo(e.getExitValue(), "", e.getMessage());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private static boolean isMessyCode(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f(即问号字符?)
            //从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
            //System.out.println("--- " + (int) c);
            if ((int) c == 0xfffd) {
                // 存在乱码
                //System.out.println("存在乱码 " + (int) c);
                return true;
            }
        }
        return false;
    }

    public static String renderScriptContent(String scriptContent, GlobalScript context) {
        // 渲染内置参数
        String pattern = "\\$\\{(context|app|appUser|version|trialUser|deviceCode)\\.(.+?)}";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(scriptContent);

        while (m.find()) {
            String group = m.group(0);
            String object = m.group(1);
            String item = m.group(2);
            String replacement = "ERR_NULL";
            try {
                JSONObject jsonObject = null;
                List<String> itemList = new ArrayList<>(Arrays.asList(item.split("\\.")));

                if ("context".equals(object)) {
                    if (context != null) {
                        jsonObject = JSON.parseObject(JSON.toJSONString(context));
                    }
                } else if ("app".equals(object)) {
                    if (context != null) {
                        jsonObject = JSON.parseObject(JSON.toJSONString(new SysAppVo(context.getApp())));
                    }
                } else if ("appUser".equals(object)) {
                    if (context != null) {
                        if (context.getLoginUser().getIfApp() && !context.getLoginUser().getIfTrial()) {
                            ISysAppUserService userService = SpringUtils.getBean(ISysAppUserService.class);
                            jsonObject = JSON.parseObject(JSON.toJSONString(new SysAppUserVo(userService.selectSysAppUserByAppUserId(context.getLoginUser().getAppUserId()))));
                        }
                    }
                } else if ("version".equals(object)) {
                    if (context != null) {
                        jsonObject = JSON.parseObject(JSON.toJSONString(new SysAppVersionVo(context.getLoginUser().getAppVersion())));
                    }
                } else if ("trialUser".equals(object)) {
                    if (context != null) {
                        if (context.getLoginUser().getIfApp() && context.getLoginUser().getIfTrial()) {
                            ISysAppTrialUserService appTrialUserService = SpringUtils.getBean(ISysAppTrialUserService.class);
                            jsonObject = JSON.parseObject(JSON.toJSONString(new SysAppTrialUserVo(appTrialUserService.selectSysAppTrialUserByAppTrialUserId(context.getLoginUser().getAppTrialUserId()))));
                        }
                    }
                } else if ("deviceCode".equals(object)) {
                    if (context != null) {
                        jsonObject = JSON.parseObject(JSON.toJSONString(new SysDeviceCodeVo(context.getLoginUser().getDeviceCode())));
                    }
                } else {
                    throw new ApiException(ErrorCode.ERROR_GLOBAL_SCRIPT_INNER_VARIABLE_ANALYTICS_FAILED, "脚本内置变量：" + group + "无法被解析");
                }
                replacement = recursion(itemList, jsonObject);
                if (replacement == null) {
                    replacement = "ERR_NULL";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            scriptContent = scriptContent.replace(group, replacement);
        }
        return scriptContent;
    }

    private static String recursion(List<String> itemList, JSONObject obj) {
        try {
            if (obj == null || itemList == null || itemList.size() == 0) {
                return null;
            }
            if (itemList.size() == 1) {
                if ("*".equals(itemList.get(0))) {
                    return obj.toString();
                }
                return obj.getString(itemList.get(0));
            } else {
                JSONObject object = obj.getJSONObject(itemList.get(0));
                return recursion(itemList.subList(1, itemList.size()), object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

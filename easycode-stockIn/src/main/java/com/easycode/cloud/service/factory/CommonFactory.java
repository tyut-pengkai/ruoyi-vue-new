package com.easycode.cloud.service.factory;

import com.weifu.cloud.tools.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description 共通获取相关业务
 * @Author zhangjj
 * @Date 2024/05/13 013 10:16
 */
@Component
public class CommonFactory<T> {

    @Autowired
    private Environment environment;

    public T getService(String tenantId, Map<String, T> serviceMap) {
        T currentService = null;
        if(StringUtil.isNotEmpty(tenantId) && serviceMap.size()>1){
            String serviceName = serviceMap.keySet().stream().findFirst().orElse(null);
            if (serviceName != null && serviceName.contains("Impl")){
                serviceName = serviceName.substring(0,serviceName.indexOf("Impl"));
            }
            String factoryConfig = environment.getProperty(tenantId+"."+serviceName);
            if(StringUtil.isNotEmpty(factoryConfig)){
                currentService = serviceMap.get(factoryConfig);
            }else{
                currentService = serviceMap.values().stream().findFirst().orElse(null);
            }

        }else{
            currentService = serviceMap.values().stream().findFirst().orElse(null);
        }
        return currentService;
    }
}

package com.ruoyi.framework.config.datapermission;

import com.ruoyi.common.annotation.DataPermission;
import com.ruoyi.common.core.domain.model.DataPermissionModel;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Description: 数据权限Aspect
 * @Author DuanYangYang
 * @Date 2024/5/23/023 11:50
 **/
@Slf4j
@Aspect
@Component
public class DataPermissionAspect {

    @Autowired
    private ISysUserService sysUserService;


    // 数据权限切面
    @Pointcut("execution(* com.tyjh.business.controller..*.*(..))")
    public void dataPermissionPointCut() {}


    /**
     * PC端数据权限切面：对在 com.tyjh.business.controller 包下，但不在 com.tyjh.business.controller.applet 包下的，所有
     *                标注了 @DataPermission 注解的方法进行切面处理
     * @Author DuanYangYang
     * @Date 15:44 2024/5/23/023
     * @Param
     * @Return
     **/
    @Around("dataPermissionPointCut() && !appletDataPermissionPointCut() && !thirdDataPermissionPointCut() && @annotation(controllerDataPermission))")
    public Object dataPermissionAspect(ProceedingJoinPoint point, DataPermission controllerDataPermission) throws Throwable {
        try {
            //获取当前用户的数据权限信息，
            DataPermissionModel dataPermissionModel = sysUserService.getCurrentUserDataPermission();

            //获取需要进行数据权限处理的mapper类名列表
            String[] mapperNames = controllerDataPermission.value();
            dataPermissionModel.setMappers(Arrays.asList(mapperNames));

            //将数据权限模型 放到 线程上下文中
            DataPermissionContextHolder.setContext(dataPermissionModel);

            return point.proceed();
        }finally {
            //清除数据权限上下文信息
            DataPermissionContextHolder.clearContext();
        }
    }

}

//package com.easycode.cloud.lock;
//
//import com.baomidou.lock.LockFailureStrategy;
//import com.weifu.cloud.common.core.exception.ServiceException;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//
//
///**
// * lock4j 自定义抢锁失败策略
// * @author yangyang.zhang
// */
//
//@Component
//public class GrabLockFailureStrategy implements LockFailureStrategy {
//
//
//    @Override
//    public void onLockFailure(String key, Method method, Object[] arguments) {
//        // key  截取#后的内容
//        key = key.substring(key.indexOf("#") + 1);
//        throw  new ServiceException(String.format("%s正在处理，不能同时处理",key));
//    }
//}

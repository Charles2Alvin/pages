package com.haitao.pages.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.mapping.Join;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Aspect
@Component
public class LogAspect {
    // 面向切面编程；好处：可以通过logger对服务进行时间/次数统计、性能评估
    // 思想：插入到所有的业务里边，可以用到拦截器

    // 通过工厂方法来创建logger
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    // 在调用IndexController类里的所有方法之前都要调用这个方法
    @Before("execution(* com.haitao.pages.controller.IndexController.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        // Joinpoint是切点，也就是调用这个方法的入口，可以通过joinpoint获取调用者的信息
        StringBuffer sb = new StringBuffer();
        for (Object arg : joinPoint.getArgs()) {
            sb.append("arg:" + arg.toString() + "|");
        }
        logger.info("Before method " + sb.toString());
    }
    @After("execution(* com.haitao.pages.controller.IndexController.*(..))")
    public void afterMethod() {
        // 在调用IndexController类里的所有方法之前都要调用这个方法
        logger.info("After method" + new Date());
    }
}

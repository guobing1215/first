package com.example.sysLog;

import java.lang.annotation.*;

/**
 * @ClassName : SysLog
 * @Description : 定义系统日志注解
 * @Author : 郭兵
 * @Date: 2020-09-28 11:57
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String description() default "";

}

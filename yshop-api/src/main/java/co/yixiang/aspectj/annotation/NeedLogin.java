package co.yixiang.aspectj.annotation;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * 
 * @author monster
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NeedLogin
{
    /**
     * 模块 
     */
    public String path() default "";

    /**
     * 是否需要登录
     */
    public boolean needLogin() default true;
}

package co.yixiang.aspectj;


import co.yixiang.aspectj.annotation.NeedLogin;
import co.yixiang.common.util.ServletUtils;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * 操作日志记录处理
 *
 * @author monster
 */
@Aspect
@Component
public class NeedLoginAspect {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(co.yixiang.aspectj.annotation.NeedLogin)")
    public void loginPointCut() {
    }

    @Before("loginPointCut()")
    public void beforeMethod(JoinPoint point){
        handleLog(point);
    }


    protected void handleLog(final JoinPoint joinPoint)  {
        // 获得注解
        NeedLogin needLogin = getAnnotationLog(joinPoint);
        if (null == needLogin) {
            return;
        }
        if (needLogin.needLogin()) {
            // 需要登录
            String requestUri = ServletUtils.getRequest().getRequestURI();
            String token = ServletUtils.getRequest().getHeader("token");
            // 部分公共参数校验
            Assert.isTrue(!StringUtils.isAnyBlank(token), "公共参数缺失:" + requestUri);
            // 获取登录用户
            SystemUser user = getRedisUser(token);
            if (null == user ) {
                throw new BadRequestException("用户未登录！");
            }
        }

    }


    /**
     * 从redis里面获取用户
     *
     * @param token
     * @return
     */
    private SystemUser getRedisUser(String token) {
        if (co.yixiang.utils.StringUtils.isBlank(token)) {
            return null;
        }
        if (redisUtils.hasKey(token)) {
            return (SystemUser) redisUtils.get(token);
        }
        return null;
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private NeedLogin getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(NeedLogin.class);
        }
        return null;
    }
}

package co.yixiang.common.web.controller;

import co.yixiang.common.api.ApiController;
import co.yixiang.exception.NotLoginException;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.utils.RedisUtils;
import co.yixiang.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public abstract class BaseController extends ApiController {
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取当前请求
     *
     * @return request
     */
    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    /**
     * 获取当前请求
     *
     * @return response
     */
    public HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 从redis里面获取用户
     *
     * @param token
     * @return
     */
    public SystemUser getRedisUser(String token) {
        if (StringUtils.isBlank(token)) {
            throw new NotLoginException("登录信息已过期");
        }
        if (redisUtils.hasKey(token)) {
            return (SystemUser) redisUtils.get(token);
        }
        throw new NotLoginException("登录信息已过期");
    }
}
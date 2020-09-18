package co.yixiang.utils;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;
import com.hyjf.framework.starter.redis.util.RedisUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * redis分布式锁工具列*
 *
 * @author zhangyk
 * @date 2020/4/13 14:09
 */
@Slf4j
public class RedisLockUtils extends RedisUtils {

    // 加锁执行结果
    private static final String LOCK_SUCCESS = "OK";
    // 解锁执行结果
    private static final Long RELEASE_SUCCESS = 1L;
    // 加锁方式(key不存在才能成功)
    private static final String SET_IF_NOT_EXIST = "NX";
    // 指定过期时间
    private static final String SET_WITH_EXPIRE_TIME_SECOND = "EX";


    /**
     * 加锁
     *
     * @param lockKey    加锁key
     * @param requestId  请求id,uuid即可
     * @param expireTime 过期时间,防止系统异常导致垃圾锁,影响业务
     * @author zhangyk
     * @date 2020/4/13 14:15
     */
    public static Boolean tryGetLock(String lockKey, String requestId, int expireTime) {
        JedisPool jedisPool = null;
        Jedis jedis = null;
        try {
            jedisPool = getConnection();
            jedis = jedisPool.getResource();
            String result = jedis.set(lockKey, requestId, SetParams.setParams().ex(expireTime).nx());
            if (LOCK_SUCCESS.equals(result)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            log.error("分布式锁加锁错误", e);
            return Boolean.FALSE;
        } finally {
            returnResource(jedisPool, jedis);
        }
    }


    /**
     * 解锁(使用lua脚本，保证"判断"和"删除"操作原子性)
     *
     * @author zhangyk
     * @date 2020/4/13 14:42
     */
    public static Boolean releaseLock(String lockKey, String requestId) {
        JedisPool jedisPool = null;
        Jedis jedis = null;
        try {
            jedisPool = getConnection();
            jedis = jedisPool.getResource();
            String script = "if redis.call('get',KEYS[1]) == ARGV[1] then redis.call('del',KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            log.error("分布式锁解锁错误", e);
            return Boolean.FALSE;
        } finally {
            returnResource(jedisPool, jedis);
        }
    }

    /**
     * 并发情况下, 对key值进行加减
     *
     * @param key
     * @param value
     */
    public static void add(String key, String value) {

        JedisPool jedisPool = null;
        Jedis jedis = null;

        try {
            jedisPool = getConnection();
            jedis = jedisPool.getResource();
            while ("OK".equals(jedis.watch(key))) {
                List<Object> results = null;

                String balance = jedis.get(key);
                BigDecimal bal = new BigDecimal(0);
                if (balance != null) {
                    bal = new BigDecimal(balance);
                }
                BigDecimal val = new BigDecimal(value);

                Transaction tx = jedis.multi();
                String valbeset = bal.add(val).toString();
                tx.set(key, valbeset);
                results = tx.exec();
                if (results == null || results.isEmpty()) {
                    jedis.unwatch();
                } else {
                    String ret = (String) results.get(0);
                    if (ret != null && "OK".equals(ret)) {
                        // 成功后
                        break;
                    } else {
                        jedis.unwatch();
                    }
                }
            }
        } catch (Exception e) {
            log.error("Redis错误", e);
        } finally {
            // 释放redis对象
            // 释放
            // 返还到连接池
            returnResource(jedisPool, jedis);
        }
    }

    public static void main(String[] args) {
        Integer merchantId = 43;
        String lockKey = "lock_key:" + merchantId;
        String requestId = UUID.randomUUID().toString().replaceAll("-", "");
        while (true) {
            if (tryGetLock(lockKey, requestId, 3)) {
                //   业务
                break;  // ****** 很重要，别漏了  *******
            }
            continue;
        }
        releaseLock(lockKey, requestId);  // 很重要  否则redis一直占用资源
    }


}

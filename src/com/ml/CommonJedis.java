package com.ml;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class CommonJedis {
	static Logger logger = Logger.getLogger(CommonJedis.class);

	public static String REDIS_PROPERTY_FILE = "redisconfig.properties";
	public static String HOST_IP = "127.0.0.1";
	public static int PORT = 6379;
	public static String PASSWORD = "";
	static {
		HOST_IP = CommonUtil.getProperty(REDIS_PROPERTY_FILE, "host_ip");
		PORT = Integer.parseInt(CommonUtil.getProperty(REDIS_PROPERTY_FILE, "port"));
		PASSWORD = CommonUtil.getProperty(REDIS_PROPERTY_FILE, "password");

	}

	/**
	 * 获取资源
	 * 
	 * @return
	 */
	public static Jedis getResource() {
		Jedis jedis = null;
		try {
			jedis = new Jedis(HOST_IP, PORT);
			jedis.auth(PASSWORD);

		} catch (Exception e) {
			logger.error("getResource:{}", e);
			if (jedis != null)
				jedis.close();

		}
		return jedis;
	}

	public static String get(String key) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.get(key);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
				logger.debug("get key=" + key + " value=" + value);
			}
		} catch (Exception e) {
			logger.warn("get key=" + key + " value=" + value + e.toString());
		} finally {
			jedis.close();
		}
		return value;
	}

	public static void set(String key, String value, int cacheSeconds) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.set(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("get key=" + key + " value=" + value);
		} catch (Exception e) {
			logger.warn("get key=" + key + " value=" + value + e.toString());
		} finally {
			jedis.close();
		}
		return;
	}

	/**
	 * 设置缓存
	 * 
	 * @param key
	 *            String
	 * @param value
	 *            Object对象
	 * @param cacheSeconds
	 *            超时时间，0为不超时
	 * @return
	 */
	public static void setMap(String key, Map<String, String> value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.hmset(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("get key=" + key + " value=" + value);
		} catch (Exception e) {
			logger.warn("get key=" + key + " value=" + value + e.toString());
		} finally {
			jedis.close();
		}
		return;
	}

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return 对象(反序列化)
	 */
	public static Map<String, String> getMap(String key) {
		Map<String, String> map = new HashMap<String, String>();
		Jedis jedis = null;
		try {
			jedis = getResource();
			map = jedis.hgetAll(key);

		} catch (Exception e) {
			logger.warn(e.toString());
		} finally {
			jedis.close();
		}
		return map;
	}

	public static void setRedisValue(String categoryName, String key, Map<String, String> dataValue) {
		setMap(categoryName + "_" + key, dataValue, 1000);
	}

	public static Object getRedisMapValue(String categoryName, String key) {
		return getMap(categoryName + "_" + key);
	}

	public static void setRedisValue(String categoryName, String key, String dataValue) {
		set(categoryName + "_" + key, dataValue, 1000);
	}

	public static String getRedisValue(String categoryName, String key) {
		return get(categoryName + "_" + key);
	}
}

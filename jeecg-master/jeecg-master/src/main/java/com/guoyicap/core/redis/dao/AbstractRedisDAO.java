package com.guoyicap.core.redis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author 王振广
 * @Description:Spring-data-redis 集成，获取RedisTemplate
 * @param <K>
 * @param <V>
 */
public abstract class AbstractRedisDAO<K, V> {  
	  
    @Autowired  
    protected RedisTemplate<K, V> redisTemplate; 
    
    @Autowired  
    protected StringRedisTemplate stringRedisTemplate; 
  
    // 设置redisTemplate  
    /**
     * @param redisTemplate
     */
    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {  
        this.redisTemplate = redisTemplate;  
    }  
  
    /**
     * @return redisTemplate
     */
    public RedisTemplate<K, V> getRedisTemplate() {  
        return redisTemplate;  
    }

	/**
	 * @return stringRedisTemplate
	 */
	public StringRedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}

	/**
	 * @param stringRedisTemplate
	 */
	public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}  
    
    
}

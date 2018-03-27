package com.guoyicap.core.redis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Limit;
import org.springframework.data.redis.connection.RedisZSetCommands.Range;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.guoyicap.core.redis.dao.AbstractRedisDAO;
import com.guoyicap.core.redis.service.RedisService;

/**
 * @author 王振广
 * @description:Redis Service，集成Sping-data-redis
 * @version v1.0.0
 * @date 2016/11/23
 *
 */
@Service(value = "redisService")  
public class RedisServiceImpl extends AbstractRedisDAO<String, String>  implements RedisService{
	private Logger logger=Logger.getLogger(RedisServiceImpl.class);
	
	 /**
     * @return redisTemplate
     */
    public RedisTemplate<String, String> getRedisTemplate() {
    	try {
    		return redisTemplate;  
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
    }

	/**
	 * @return stringRedisTemplate
	 */
	public StringRedisTemplate getStringRedisTemplate() {
		try {
			return stringRedisTemplate;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}
	

	@Override  
    public void setStr(String key, String value) {
		try {
			stringRedisTemplate.opsForValue().set(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }  
  
	
    @Override
	public void setStr(String key, String value, long timeout, TimeUnit unit) {
    	try {
    		stringRedisTemplate.opsForValue().set(key, value,timeout,unit); 
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	@Override  
    public String getStr(String key) { 
		try {
			return stringRedisTemplate.opsForValue().get(key);  
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
    }  
  
    @Override  
    public Long rPushList(String key, String value) {  
    	try {
    		return stringRedisTemplate.opsForList().rightPush(key, value); 
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
    } 
    
    @Override  
    public Long lPushList(String key, String value) {  
    	try {
    		return stringRedisTemplate.opsForList().leftPush(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
    } 
  
    @Override  
    public String lPopList(String key) {  
        try {
        	return stringRedisTemplate.opsForList().leftPop(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
    } 
    
    @Override  
    public Long listSize(String key) {  
        try {
        	return stringRedisTemplate.opsForList().size(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
    } 
    
    @Override  
    public String rPopList(String key) {  
        try {
        	return stringRedisTemplate.opsForList().rightPop(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
    }
    
    @Override
    public List<String> rangeList(String key,Long start,Long end){
    	try {
    		return stringRedisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
    }
      
    @Override  
    public void delKey(String key) {  
    	try {
    		stringRedisTemplate.delete(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }

	@Override
	public void setHash(String key, Map<Object,Object> map) {
		try {
			stringRedisTemplate.opsForHash().putAll(key, map);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	@Override
	public void setHash(String key, Object hashKey, Object value) {
		try {
			stringRedisTemplate.opsForHash().put(key, hashKey, value);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	
	@Override
	public Map getHash(String key) {
		try {
			return stringRedisTemplate.opsForHash().entries(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	@Override
	public Object getHash(String key, String hashKey) {
		try {
			return stringRedisTemplate.opsForHash().get(key, hashKey);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	@Override
	public List<Object> getHash(String key, Set<Object> hashKeys) {
		try {
			return stringRedisTemplate.opsForHash().multiGet(key, hashKeys);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	@Override
	public Long increment(String key ,Long delta) {
		if(delta==null){
			delta=1L;
		}
		try {
			return redisTemplate.opsForValue().increment(key, delta);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	@Override
	public Double increment(String key, Double delta) {
		if(delta==null){
			delta=1D;
		}
		try {
			return redisTemplate.opsForValue().increment(key, delta);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	@Override
	public boolean expire(String key ,long timeout,TimeUnit unit) {
		if(unit==null){
			unit=TimeUnit.SECONDS;
		}
		try {
			return redisTemplate.expire(key, timeout, unit);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	@Override
	public boolean expireAt(String key, Date date) {
		try {
			return redisTemplate.expireAt(key, date);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}  
	
	@Override
	public boolean exists(final String key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) {
				return connection.exists(key.getBytes());
			}
		}, true);
	}

	@Override
	public Long getExpire(String key) {
		try {
			return redisTemplate.getExpire(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	@Override
	public Long getExpire(String key, TimeUnit unit) {
		try {
			return redisTemplate.getExpire(key,unit);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}
	
	@Override
	public void executePipelined(RedisCallback<?> action){
		try {
			redisTemplate.executePipelined(action);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public Long addSet(String key,String... values){
		try {
			return stringRedisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	@Override
	public Long removeSet(String key, Object... values) {
		try {
			return stringRedisTemplate.opsForSet().remove(key, values);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
		
	}

	@Override
	public boolean ismemberSet(String key, Object o) {
		try {
			return stringRedisTemplate.opsForSet().isMember(key, o);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	@Override
	public Set<String> membersSet(String key) {
		try {
			return stringRedisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}
	
	
	public void select(final int dbIndex){
		redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.select(dbIndex);
				return true;
			}
			
		});
	}

	@Override
	public boolean addZSet(String key, String value,Double score) {
		try {
			return stringRedisTemplate.opsForZSet().add(key, value, score);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	@Override
	public Long removeZSet(String key, Object... values) {
		try {
			return stringRedisTemplate.opsForZSet().remove(key, values);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}
	
	@Override
	public Long removeZSet(String key, Long start,Long end) {
		try {
			return stringRedisTemplate.opsForZSet().removeRange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}
	@Override
	public Long removeZSet(String key, Double min,Double max) {
		try {
			return stringRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}
	
	@Override
	public Set rangeByScoreWithScores(String key,Double min,Double max) {
		try {
			return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}
	
	@Override
	public Set<String> rangeByLex(String key,Range range) {	
		try {
			return stringRedisTemplate.opsForZSet().rangeByLex(key, range);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}
	
	@Override
	public Set<String> rangeByScore(String key,Long min,Long max) {	
		try {
			return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
		
	}
	
	@Override
	public Set<String> rangeByLex(String key,Range range,Limit limit) {	
		try {
			return stringRedisTemplate.opsForZSet().rangeByLex(key, range);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
		
	}
	
	
}

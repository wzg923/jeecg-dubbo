package org.jeecgframework.core.redis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jeecgframework.core.redis.dao.AbstractRedisDAO;
import org.jeecgframework.core.redis.service.RedisService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Limit;
import org.springframework.data.redis.connection.RedisZSetCommands.Range;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author 王振广
 * @description:Redis Service，集成Sping-data-redis
 * @version v1.0.0
 * @date 2016/11/23
 *
 */
@Service(value = "redisService")  
public class RedisServiceImpl extends AbstractRedisDAO<String, String>  implements RedisService{
	 /**
     * @return redisTemplate
     */
    public RedisTemplate<String, String> getRedisTemplate() {  
        return redisTemplate;  
    }

	/**
	 * @return stringRedisTemplate
	 */
	public StringRedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}
	

	@Override  
    public void setStr(String key, String value) {  
        stringRedisTemplate.opsForValue().set(key, value);  
    }  
  
	
    @Override
	public Boolean setNX(String key, String value) {
    	return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
	}

	@Override
	public void setStr(String key, String value, long timeout, TimeUnit unit) {
    	stringRedisTemplate.opsForValue().set(key, value,timeout,unit); 
		
	}

	@Override  
    public String getStr(String key) {  
        return stringRedisTemplate.opsForValue().get(key);  
    }  
  
    @Override  
    public Long rPushList(String key, String value) {  
    	return stringRedisTemplate.opsForList().rightPush(key, value);  
  
    } 
    
    @Override  
    public Long lPushList(String key, String value) {  
    	return stringRedisTemplate.opsForList().leftPush(key, value);  
  
    } 
  
    @Override  
    public String lPopList(String key) {  
        return stringRedisTemplate.opsForList().leftPop(key);  
    } 
    
    @Override  
    public String rPopList(String key) {  
        return stringRedisTemplate.opsForList().rightPop(key);  
    }
    
    @Override
    public List<String> rangeList(String key,Long start,Long end){
    	return stringRedisTemplate.opsForList().range(key, start, end);
    	
    }
      
    @Override  
    public void delKey(String key) {  
    	stringRedisTemplate.delete(key);  
    }

	@Override
	public void setHash(String key, Map<Object,Object> map) {
		redisTemplate.opsForHash().putAll(key, map);
		
	}

	@Override
	public void setHash(String key, Object hashKey, Object value) {
		redisTemplate.opsForHash().put(key, hashKey, value);
		
	}

	
	@Override
	public Map getHash(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	@Override
	public Object getHash(String key, String hashKey) {
		return redisTemplate.opsForHash().get(key, hashKey);
	}

	@Override
	public List<Object> getHash(String key, Set<Object> hashKeys) {
		return redisTemplate.opsForHash().multiGet(key, hashKeys);
	}

	@Override
	public Long increment(String key ,Long delta) {
		if(delta==null){
			delta=1L;
		}
		return redisTemplate.opsForValue().increment(key, delta);
		
	}

	@Override
	public Double increment(String key, Double delta) {
		if(delta==null){
			delta=1D;
		}
		return redisTemplate.opsForValue().increment(key, delta);
	}

	@Override
	public boolean expire(String key ,long timeout,TimeUnit unit) {
		if(unit==null){
			unit=TimeUnit.SECONDS;
		}
		return redisTemplate.expire(key, timeout, unit);
		
	}

	@Override
	public boolean expireAt(String key, Date date) {
		return redisTemplate.expireAt(key, date);
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
		return redisTemplate.getExpire(key);
	}

	@Override
	public Long getExpire(String key, TimeUnit unit) {
		return redisTemplate.getExpire(key,unit);
	}
	
	@Override
	public void executePipelined(RedisCallback<?> action){
		redisTemplate.executePipelined(action);
	}
	
	@Override
	public Long addSet(String key,String... values){
		return stringRedisTemplate.opsForSet().add(key, values);
	}

	@Override
	public Long removeSet(String key, Object... values) {
		return stringRedisTemplate.opsForSet().remove(key, values);
		
	}

	@Override
	public Boolean ismemberSet(String key, Object o) {
		return stringRedisTemplate.opsForSet().isMember(key, o);
	}

	@Override
	public Set<String> membersSet(String key) {
		return stringRedisTemplate.opsForSet().members(key);
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
	public Boolean addZSet(String key, String value,Double score) {
		return stringRedisTemplate.opsForZSet().add(key, value, score);
	}

	@Override
	public Long removeZSet(String key, Object... values) {
		return stringRedisTemplate.opsForZSet().remove(key, values);
	}
	
	@Override
	public Long removeZSet(String key, Long start,Long end) {
		return stringRedisTemplate.opsForZSet().removeRange(key, start, end);
	}
	@Override
	public Long removeZSet(String key, Double min,Double max) {
		return stringRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
	}
	
	@Override
	public Set rangeByScoreWithScores(String key,Double min,Double max) {
		
		return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
	}
	
	@Override
	public Set<String> rangeByLex(String key,Range range) {	
		
		return stringRedisTemplate.opsForZSet().rangeByLex(key, range);
	}
	
	@Override
	public Set<String> rangeByScore(String key,Long min,Long max) {	
		
		return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
	}
	
	@Override
	public Set<String> rangeByLex(String key,Range range,Limit limit) {	
		
		return stringRedisTemplate.opsForZSet().rangeByLex(key, range);
	}
	
	@Override
	public Set<String> keys(String pattern) {	
		return stringRedisTemplate.keys(pattern);
	}
	
}

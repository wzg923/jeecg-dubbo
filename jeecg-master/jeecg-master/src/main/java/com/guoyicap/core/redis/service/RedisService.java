package com.guoyicap.core.redis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.RedisZSetCommands.Limit;
import org.springframework.data.redis.connection.RedisZSetCommands.Range;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author 王振广
 * @description:Redis Service，集成Sping-data-redis
 * @version v1.0.0
 * @date 2016/11/23
 *
 */
public interface RedisService {  
	 /**
     * @return redisTemplate
     */
    public RedisTemplate<String, String> getRedisTemplate() ;

	/**
	 * @return stringRedisTemplate
	 */
	public StringRedisTemplate getStringRedisTemplate() ;
	  
    public void setStr(String key, String value);  
    
    public void setStr(String key,String value,long timeout,TimeUnit unit);
      
    public String getStr(String key);  
      
    public Long rPushList(String key, String value);  
  
    public String lPopList(String key);  
  
    public void delKey(String key);  
    
    public void setHash(String key,Map<Object,Object> map);
    
    public void setHash(String key,Object hashKey,Object value);
    
    public Map getHash(String key);
    
    public Object getHash(String key, String hashKey);
    
    public List<Object> getHash(String key, Set<Object> hashKeys);
    
    /**  
    * @Title: increase  
    * @Description: TODO(这里用一句话描述这个方法的作用)  
    * @param key
    * @param delta 默认：1L
    * @return    设定文件  
    * @return Long    返回类型  
    * @throws  
    */
    public Long increment(String key ,Long delta);
    
    /**  
    * @Title: increase  
    * @Description: TODO(这里用一句话描述这个方法的作用)  
    * @param key
    * @param delta 默认：1D
    * @return    设定文件  
    * @return Double    返回类型  
    * @throws  
    */
    public Double increment(String key ,Double delta);
    
    /**  
    * @Title: expire  
    * @Description: 失效
    * @param key
    * @param timeout
    * @param unit 默认:TimeUnit.SECONDS
    * @return    设定文件  
    * @return boolean    返回类型  
    * @throws  
    */
    public boolean expire(String key ,long timeout,TimeUnit unit);
    
    /**  
    * @Title: expireAt  
    * @Description: 失效
    * @param key
    * @param date
    * @return    设定文件  
    * @return boolean    返回类型  
    * @throws  
    */
    public boolean expireAt(String key, Date date);
    
    /**  
    * @Title: exists  
    * @Description: 是否存在
    * @param key
    * @return    设定文件  
    * @return boolean    返回类型  
    * @throws  
    */
    public boolean exists(String key);
    
    /**  
    * @Title: getExpire  
    * @Description:失效时间 
    * @param key
    * @return    设定文件  
    * @return Long    返回类型  
    * @throws  
    */
    public Long getExpire(String key);
    
    /**  
    * @Title: getExpire  
    * @Description: TODO(这里用一句话描述这个方法的作用)  
    * @param key
    * @param unit
    * @return    设定文件  
    * @return Long    返回类型  
    * @throws  
    */
    public Long getExpire(String key,TimeUnit unit);
  
    public void executePipelined(RedisCallback<?> action);
    
    /**  
    * @Title: addSet  
    * @Author:王振广
    * @Description: Set集合中 追加值  
    * @param key
    * @param values
    * @return    设定文件  
    * @return Long    返回类型  
    * @throws  
    */
    public Long addSet(String key,String... values);
    
    /**  
    * @Title: removeSet  
    * @Author:王振广
    * @Description: 移除Set 中的值
    * @param key
    * @param values
    * @return    设定文件  
    * @return Long    返回类型  
    * @throws  
    */
    public Long removeSet(String key,Object... values);
    
    /**  
    * @Title: ismemberSet  
    * @Author:王振广
    * @Description: 判断Set 是否包含object
    * @param key
    * @param values
    * @return    设定文件  
    * @return Boolean    返回类型  
    * @throws  
    */
    public boolean ismemberSet(String key,Object object);
    
    /**  
    * @Title: membersSet  
    * @Author:王振广
    * @Description: 获取set 集合的内容
    * @param key
    * @return    设定文件  
    * @return Set<String>    返回类型  
    * @throws  
    */
    public Set<String> membersSet(String key);
    
    /**  
    * @Title: select  
    * @Author:王振广
    * @Description: 切换database
    * @param dbIndex    设定文件  
    * @return void    返回类型  
    * @throws  
    */
    public void select(int dbIndex);
    
    
    /**  
     * @Title: addSet  
     * @Author:王振广
     * @Description: Set集合中 追加值  
     * @param key
     * @param values
     * @return    设定文件  
     * @return Long    返回类型  
     * @throws  
     */
     public boolean addZSet(String key,String value,Double score);
     
     /**  
     * @Title: removeSet  
     * @Author:王振广
     * @Description: 移除Set 中的值
     * @param key
     * @param values
     * @return    设定文件  
     * @return Long    返回类型  
     * @throws  
     */
     public Long removeZSet(String key,Object... values);
     
     public Long removeZSet(String key, Long start,Long end);
     
     public Long removeZSet(String key, Double min,Double max);
     
     /**  
     * @Title: membersSet  
     * @Author:王振广
     * @Description: 获取set 集合的内容
     * @param key
     * @return    设定文件  
     * @return Set<String>    返回类型  
     * @throws  
     */
     public Set rangeByScoreWithScores(String key,Double min,Double max);
     
     public Set<String> rangeByLex(String key,Range range,Limit limit);

	Set<String> rangeByLex(String key, Range range);

	Set<String> rangeByScore(String key, Long min, Long max);

	List<String> rangeList(String key, Long start, Long end);

	Long lPushList(String key, String value);

	String rPopList(String key);

	Long listSize(String key);
}

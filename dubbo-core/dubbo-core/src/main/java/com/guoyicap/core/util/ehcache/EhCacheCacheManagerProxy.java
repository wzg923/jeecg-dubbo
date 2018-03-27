package com.guoyicap.core.util.ehcache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.util.Assert;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import net.sf.ehcache.cluster.CacheCluster;
import net.sf.ehcache.cluster.ClusterNode;
import net.sf.ehcache.cluster.ClusterScheme;
import net.sf.ehcache.cluster.ClusterTopologyListener;

/**
 * @author wangzhenguang
 * 与Spring集成Ehcache，增加是否开启全局缓存标志
 *
 */
public class EhCacheCacheManagerProxy extends AbstractCacheManager {
	private boolean globalFlag;//全局开启缓存标志
	private SimpleCacheManager simpleCacheManager;//自定义cacheManager,非EhcacheManager
	private CacheManager cacheManager;
	private List listenerList;
	private static final String SIMPLE_CACHE_NAME = "simpleCache";
	
	public EhCacheCacheManagerProxy()
    {
        simpleCacheManager = new SimpleCacheManager();
        globalFlag = false;
    }

	public void setGlobalFlag(boolean globalFlag) {
		this.globalFlag = globalFlag;
	}
	
	
	public CacheManager getCacheManager()
    {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager)
    {
        this.cacheManager = cacheManager;
    }
    
    public void setListenerList(List listenerList)
    {
        this.listenerList = listenerList;
    }

	@Override
	public void afterPropertiesSet()
    {
        
		initSimpleCacheManager();//实例化自定义CacheManager
        if(!globalFlag)
        {
            return;
        } else
        {
            super.afterPropertiesSet();
            return;
        }
    }

	 private void addClusterListener()
	    {
	        if(listenerList == null)
	            listenerList = new ArrayList();
	        listenerList.add(new ClusterTopologyListener() {

	            public void nodeLeft(ClusterNode node)
	            {
	            	logger.error((new StringBuilder("cluster nodeLeft,nodeIp=")).append(node != null ? node.getIp() : "null").toString());
	            }

	            public void nodeJoined(ClusterNode node)
	            {
	            	logger.error((new StringBuilder("cluster nodeJoined,nodeIp=")).append(node != null ? node.getIp() : "null").toString());
	            }

	            public void clusterRejoined(ClusterNode oldNode, ClusterNode newNode)
	            {
	            	logger.error((new StringBuilder("cluster clusterRejoined,oldNodeIp=")).append(oldNode != null ? oldNode.getIp() : "null").append(",newNodeIp=").append(newNode != null ? newNode.getIp() : "null").toString());
	            }

	            public void clusterOnline(ClusterNode node)
	            {
	            	logger.error((new StringBuilder("cluster clusterOnline,nodeIp=")).append(node != null ? node.getIp() : "null").toString());
	            }

	            public void clusterOffline(ClusterNode node)
	            {
	            	logger.error((new StringBuilder("cluster clusterOffline,nodeIp=")).append(node != null ? node.getIp() : "null").toString());
	            }

	            final EhCacheCacheManagerProxy this$0;
	            {
	                this$0 = EhCacheCacheManagerProxy.this;	                
	            }
	        }
	);
	        CacheCluster cluster = cacheManager.getCluster(ClusterScheme.TERRACOTTA);
	        for(Iterator iterator = listenerList.iterator(); iterator.hasNext();)
	        {
	            ClusterTopologyListener listener = (ClusterTopologyListener)iterator.next();
	            if(listener != null)
	                cluster.addTopologyListener(listener);
	        }

	    }
	@Override
	 public Cache getCache(String name)
	    {
	        if(!globalFlag){//如果全局缓存标志 false，则不实例化ehcache缓存
	        	return simpleCacheManager.getCache("simpleCache");
	        }	            
	        Cache cache = super.getCache(name);
	        if(cache == null)
	        {
	        	// check the EhCache cache again
				// (in case the cache was added at runtime)
				Ehcache ehcache = this.cacheManager.getEhcache(name);
				if (ehcache != null) {
					cache = new EhCacheCache(ehcache);
					addCache(cache);
				}
	        }
	        return cache;
	    }
	
	
	/**
	 * 实例化 SimpleCacheManager
	 */
	private void initSimpleCacheManager()
    {
        List caches = new ArrayList();
        ConcurrentMapCache memoryOnlyCache = new ConcurrentMapCache("simpleCache");
        caches.add(memoryOnlyCache);
        simpleCacheManager.setCaches(caches);
        simpleCacheManager.afterPropertiesSet();
    }
	protected final Logger logger = LoggerFactory.getLogger(EhCacheCacheManagerProxy.class);

	@Override
	protected Collection loadCaches()
    {
        Assert.notNull(cacheManager, "A backing EhCache CacheManager is required");
        Status status = cacheManager.getStatus();
        Assert.isTrue(Status.STATUS_ALIVE.equals(status), (new StringBuilder("An 'alive' EhCache CacheManager is required - current cache is ")).append(status.toString()).toString());
        String names[] = cacheManager.getCacheNames();
        Collection caches = new LinkedHashSet(names.length);
        String as[];
        int j = (as = names).length;
        for(int i = 0; i < j; i++)
        {
            String name = as[i];
            caches.add(new EhCacheCache(cacheManager.getEhcache(name)));
        }

        addClusterListener();
        return caches;
    }
}

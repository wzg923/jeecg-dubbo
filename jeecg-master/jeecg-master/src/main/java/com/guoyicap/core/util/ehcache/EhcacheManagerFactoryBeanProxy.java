package com.guoyicap.core.util.ehcache;

import java.io.IOException;
import net.sf.ehcache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;

public class EhcacheManagerFactoryBeanProxy extends EhCacheManagerFactoryBean
{

    public EhcacheManagerFactoryBeanProxy()
    {
        cacheFlag = false;
    }

    @Override
    public void afterPropertiesSet()
        throws CacheException
    {
        if(!cacheFlag)
        {
            logger.warn("************ehcache disabled!!!");
            return;
        } else
        {
            super.afterPropertiesSet();
            return;
        }
    }

    public boolean getCacheFlag()
    {
        return cacheFlag;
    }

    public void setCacheFlag(boolean cacheFlag)
    {
        this.cacheFlag = cacheFlag;
    }

    protected final Logger logger = LoggerFactory.getLogger(EhcacheManagerFactoryBeanProxy.class);
    private boolean cacheFlag;//开启缓存标志
}

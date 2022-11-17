package cn.sc.app.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class GuavaCache implements AppCache {

    @Value("${app.lookCacheEnabled}")
    private boolean lookCacheEnabled;

    private static final String JT = " -> ";

    @PostConstruct
    private void initLookCacheEnabled() {
        Cache<String, Object> build = CacheBuilder.newBuilder().maximumSize(500).build();
        build.put("lookCacheEnabled", lookCacheEnabled);
        cacheMap.putIfAbsent("config", build);
    }

    static ConcurrentMap<String, Cache<String, Object>> cacheMap;

    static {
        cacheMap = Maps.newConcurrentMap();
    }

    private static Cache<String, Object> getCacheByType(String type) {
        return cacheMap.get(type);
    }

    private static Cache<String, Object> setCache(String type, long duration, TimeUnit timeUnit) {
        Cache<String, Object> cache = getCacheByType(type);
        if (cache != null) {
            return cache;
        }
        cache = CacheBuilder.newBuilder().expireAfterWrite(duration, timeUnit).maximumSize(500).build();
        cacheMap.putIfAbsent(type, cache);
        return cache;
    }

    @Override
    public Object getObject(String type, String key) {
        Cache<String, Object> cache = getCacheByType(type);
        if (cache == null) {
            return null;
        }
        return cache.getIfPresent(key);
    }

    @Override
    public void setObject(String type, String key, Object object, long duration, TimeUnit timeUnit) {
        Cache<String, Object> cache = setCache(type, duration, timeUnit);
        cache.put(key, object);
        getAllCache();
    }

    @Override
    public void removeObject(String type, String key) {
        Cache<String, Object> cache = getCacheByType(type);
        cache.invalidate(key);
        getAllCache();
    }

    @Override
    public String getAllCache() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\r\n" + "@@@cache@@@");
        cacheMap.forEach((key, value) -> {
            StringBuffer map = new StringBuffer();
            value.asMap().forEach((mapKey, mapValue) -> {
                map.append("\r\n" + mapKey + JT + mapValue);
            });
            stringBuffer.append("\r\n" + "类型" + JT + JT + key + map);
        });
        log.debug(stringBuffer.toString());
        return stringBuffer.toString();
    }

}

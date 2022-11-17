package cn.sc.app.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 缓存模板 ->抽象类实现
 */
@Data
@Slf4j
public abstract class GuavaAbstractLoadingCache<K, V> {

    //用于初始化cache的参数及其缺省值
    private int maximumSize = 100;                    //最大缓存条数
    private int expireAfterWriteDuration = 30;        //数据存在时长
    private TimeUnit timeUnit = TimeUnit.DAYS;        //时间单位
    private Date resetTime;                           //Cache初始化或被重置的时间
    private long highestSize = 0;                     //历史最高记录数
    private Date highestTime;                         //创造历史记录的时间

    private LoadingCache<K, V> cache;

    /**
     * 通过调用getCache().get(key)来获取数据
     *
     * @return cache
     */
    public LoadingCache<K, V> getCache() {
        if (cache == null) {    //使用双重校验锁保证只有一个cache实例
            synchronized (this) {
                if (cache == null) {
                    //创建一个缓存，定义 CacheLoader 若没缓存则调用自定义的  fetchData() 方法 然后将得到的缓存存到这个本地缓存里
                    cache = CacheBuilder.newBuilder().maximumSize(maximumSize)        //缓存数据的最大条目，也可以使用.maximumWeight(weight)代替
                            .expireAfterWrite(expireAfterWriteDuration, timeUnit)    //数据被创建多久后被移除
                            .recordStats()                                            //启用统计
                            .removalListener(new GuavaListener())
                            .build(new CacheLoader<K, V>() {                        //若没有查到缓存数据，则从这里 自定义 去加载数据 然后再设置进本地缓存--》相当于 策略
                                @Override
                                public V load(K key) throws Exception {
                                    V v = fetchData(key);
                                    log.info("数据库 GET -> {}", v);
                                    return v;
                                }
                            });
                    this.resetTime = new Date();
                    this.highestTime = new Date();
                    log.info("本地缓存---{}---初始化成功，该缓存大小为：{},缓存数据保存时间为：{}-{}",
                            this.getClass().getSimpleName(),
                            this.getMaximumSize(),
                            this.getExpireAfterWriteDuration(),
                            this.getTimeUnit());
                }
            }
        }
        return cache;
    }

    /**
     * 根据key从数据库或其他数据源中获取一个value，并被自动保存到缓存中。
     *
     * @param key
     * @return value, 连同key一起被加载到缓存中的。
     */
    protected abstract V fetchData(K key);

    /**
     * 从缓存中获取数据（第一次自动调用fetchData从外部获取数据），并处理异常
     *
     * @param key
     * @return Value
     * @throws ExecutionException
     */
    protected V getValue(K key) throws ExecutionException {
        V result = getCache().get(key);
        if (getCache().size() > highestSize) {
            highestSize = getCache().size();
            highestTime = new Date();
        }
        return result;
    }

    protected void set(K k, V v) {
        getCache().put(k, v);
    }

    protected void invalidate(K key) {
        cache.invalidate(key);
    }
}

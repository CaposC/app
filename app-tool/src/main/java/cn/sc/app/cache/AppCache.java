package cn.sc.app.cache;

import java.util.concurrent.TimeUnit;

public interface AppCache {

    void setObject(String type, String key, Object object, long duration, TimeUnit timeUnit);

    Object getObject(String type, String key);

    //失效
    void removeObject(String type, String key);

    //打印全部
    String getAllCache();

}

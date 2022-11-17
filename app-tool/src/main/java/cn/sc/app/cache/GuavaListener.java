package cn.sc.app.cache;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuavaListener implements RemovalListener<Object, Object> {
    @Override
    public void onRemoval(RemovalNotification<Object, Object> notification) {
        log.info("key -> " + notification.getKey() + ",value -> " + notification.getValue() + " 失效了!");
    }
}

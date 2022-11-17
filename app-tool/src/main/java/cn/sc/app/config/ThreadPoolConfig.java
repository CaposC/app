package cn.sc.app.config;

import cn.sc.app.manager.AsyncManager;
import cn.sc.app.utils.Threads;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 **/
@Configuration
public class ThreadPoolConfig<T> {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolConfig.class);

    private static final int CPU_CORE = Runtime.getRuntime().availableProcessors();

    // 核心线程池大小
    private int corePoolSize = CPU_CORE * 2; // cpu 核心线数 *2

    // 最大可创建的线程数
    private int maxPoolSize = CPU_CORE * 3; //必须比corePoolSize大

    // 队列最大长度
    private int queueCapacity = 300;

    // 线程池维护线程所允许的空闲时间
    private int keepAliveSeconds = 300;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(maxPoolSize);
        executor.setCorePoolSize(corePoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService() {
        logger.info("scheduledExecutorService init...");
        return new ScheduledThreadPoolExecutor(corePoolSize,
                new ThreadFactoryBuilder().setNameFormat("retryClient-pool-").setDaemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }

    @PreDestroy
    protected void scheduledExecutorServiceShutDown() {
        logger.info("scheduledExecutorService shutDown...");
        AsyncManager.me().shutdown();
    }

}

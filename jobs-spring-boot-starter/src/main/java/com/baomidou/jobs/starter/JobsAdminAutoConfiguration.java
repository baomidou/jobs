package com.baomidou.jobs.starter;

import com.baomidou.jobs.disruptor.JobsDisruptorTemplate;
import com.baomidou.jobs.disruptor.JobsEventHandler;
import com.baomidou.jobs.disruptor.JobsInfoEvent;
import com.baomidou.jobs.router.ExecutorConsistentHashRouter;
import com.baomidou.jobs.router.IJobsExecutorRouter;
import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import com.baomidou.jobs.rpc.serialize.impl.HessianSerializer;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadFactory;

/**
 * Jobs Admin 启动配置
 *
 * @author jobob
 * @since 2019-06-27
 */
@Configuration
@EnableConfigurationProperties(JobsProperties.class)
public class JobsAdminAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IJobsRpcSerializer jobsRpcSerializer() {
        return new HessianSerializer();
    }

    @Bean
    @ConditionalOnMissingBean
    public CronParser cronParser(JobsProperties jobsProperties) {
        CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(
                CronType.valueOf(jobsProperties.getCronType()));
        return new CronParser(cronDefinition);
    }

    @Bean
    @ConditionalOnMissingBean
    public WaitStrategy waitStrategy() {
        return new SleepingWaitStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public ThreadFactory threadFactory() {
        return DaemonThreadFactory.INSTANCE;
    }

    @Bean
    @ConditionalOnMissingBean
    public JobsEventHandler jobsEventHandler() {
        return new JobsEventHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public IJobsExecutorRouter jobsExecutorRouter() {
        return new ExecutorConsistentHashRouter();
    }

    @Bean
    @ConditionalOnClass({Disruptor.class})
    public Disruptor<JobsInfoEvent> disruptor(WaitStrategy waitStrategy, ThreadFactory threadFactory,
                                              JobsEventHandler jobsEventHandler) {
        Disruptor<JobsInfoEvent> disruptor = new Disruptor<>(() -> new JobsInfoEvent(), 256 * 1024,
                threadFactory, ProducerType.MULTI, waitStrategy);
        disruptor.handleEventsWith(jobsEventHandler);

        // 启动
        disruptor.start();

        // WEB 容器关闭执行
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // OK
                disruptor.shutdown();

                // wait up to 10 seconds for the ringbuffer to drain
                RingBuffer<JobsInfoEvent> ringBuffer = disruptor.getRingBuffer();
                for (int i = 0; i < 20; i++) {
                    if (ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize())) {
                        break;
                    }
                    try {
                        // give ringbuffer some time to drain...
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // ignored
                    }
                }
                disruptor.shutdown();
            } catch (Exception e) {
                // to do nothing
            }
        }));
        return disruptor;
    }

    @Bean
    public JobsDisruptorTemplate jobsDisruptorTemplate() {
        return new JobsDisruptorTemplate();
    }
}

package com.ruoyi.web.core.config;

import com.ruoyi.common.utils.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置
 *
 * @author Engineer
 */
@Configuration
public class RedissonConfig {

    private final static String REDIS_URL_FORMAT = "redis://%s:%s";

    @Value("${spring.redis.host:}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private Integer port;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.cluster.nodes:}")
    private String[] nodes;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        SingleServerConfig config2 = config.useSingleServer();

        String redisUrl = String.format(REDIS_URL_FORMAT, this.host, this.port);
        config2.setAddress(redisUrl);

        if (StringUtils.isNotBlank(password)){
            config2.setPassword(password);
        }

        return Redisson.create(config);
    }
}

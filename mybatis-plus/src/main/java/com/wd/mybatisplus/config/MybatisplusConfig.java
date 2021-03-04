package com.wd.mybatisplus.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MybatisplusConfig
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2021/3/1
 * @Version V1.0
 **/
@Configuration
public class MybatisplusConfig {

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }

}

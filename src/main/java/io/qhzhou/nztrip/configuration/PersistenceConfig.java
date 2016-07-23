package io.qhzhou.nztrip.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan("io.qhzhou.nztrip.mapper")
@EnableTransactionManagement
public class PersistenceConfig {

}
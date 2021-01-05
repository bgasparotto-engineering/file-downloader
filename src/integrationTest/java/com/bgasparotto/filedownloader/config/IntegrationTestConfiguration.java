package com.bgasparotto.filedownloader.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("it")
@Configuration
@ComponentScan("com.bgasparotto.spring.kafka.avro.test")
public class IntegrationTestConfiguration {
}

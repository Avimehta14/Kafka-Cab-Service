package com.myCabKafka.cab_service_driver.configs;

import com.myCabKafka.cab_service_driver.constants.AppConstant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class kafkaConfig {

    @Bean
    public NewTopic topic()
    {
        return TopicBuilder.name(AppConstant.CAB_LOCATION).build();
    }

    @Bean
    public NewTopic myDLQ()
    {
        return TopicBuilder.name(AppConstant.CAB_LOCATION_DLQ).build();
    }
}

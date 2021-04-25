package com.gaga.springbootconsumer.messageConsumerController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费者
 *
 * @Author fuGaga
 * @Date 2021/3/8 15:07
 * @Version 1.0
 */
@Component
@Slf4j
public class KafkaConsumer {

    private final static String testTopic = "topicTest";

    /**
     * MANUAL_IMMEDIATE 手动调用Acknowledgment.acknowledge()后立即提交
     *
     * @param consumerFactory
     * @return
     */
    @Bean("manualImmediateListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> manualImmediateListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setPollTimeout(1500);
        factory.setBatchListener(true);
        //配置手动提交offset
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }


    @KafkaListener(id = "consumer1", topics = testTopic, groupId = "test1", containerFactory = "manualImmediateListenerContainerFactory")
    public void consumer(String message, Acknowledgment ack, @Header(KafkaHeaders.OFFSET) Integer offset, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition) {
        log.info("---consumer1---" + offset + "---" + partition + "---" + message);
        ack.acknowledge();
    }

    @KafkaListener(id = "consumer2", topics = testTopic, groupId = "test1", containerFactory = "manualImmediateListenerContainerFactory")
    public void consumer2(String message, Acknowledgment ack, @Header(KafkaHeaders.OFFSET) Integer offset, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition) {
        log.info("---consumer2---" + offset + "---" + partition + "---" + message);
        ack.acknowledge();
    }
}

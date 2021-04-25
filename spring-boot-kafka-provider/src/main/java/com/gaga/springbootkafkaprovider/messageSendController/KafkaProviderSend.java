package com.gaga.springbootkafkaprovider.messageSendController;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 消息生产者
 *
 * @Author fuGaga
 * @Date 2021/3/8 14:24
 * @Version 1.0
 */
@RestController
@Slf4j
public class KafkaProviderSend {

    @Value("${kafka.topic.test}")
    private String testTopic;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @RequestMapping("sendMessage")
    public void sendMessage(@RequestBody Object obj) {
        String jsonString = JSONObject.toJSONString(obj);
        for (int i = 0; i < 3; i++) {
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(testTopic, i, i + "", jsonString);
            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.info(testTopic + "-----消息发送成功-----" + throwable.toString());
                }

                @Override
                public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                    log.info(testTopic + "-----消息发送成功-----" + stringObjectSendResult.toString());
                }
            });
        }
    }
}

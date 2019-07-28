package com.fooock.robotstxt.downloader.stream;

import com.fooock.robotstxt.downloader.service.IncomingUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 */
@Service
@Slf4j
public class IncomeStreamService implements StreamListener<String, MapRecord<String, String, String>> {
    private final IncomingUrlService incomingUrlService;
    private final StreamMessageListenerContainer<String, MapRecord<String, String, String>> income;

    @Value("${stream.consumer.group}")
    private String groupName;

    @Value("${stream.consumer.name}")
    private String consumerName;

    public IncomeStreamService(IncomingUrlService incomingUrlService, RedisConnectionFactory connectionFactory) {
        this.incomingUrlService = incomingUrlService;
        this.income = StreamMessageListenerContainer.create(connectionFactory);
    }

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        log.debug("New incoming message: {}", message.getValue());
        incomingUrlService.receiveUrl(message.getValue().get("url"));
    }

    @PostConstruct
    public void onStart() {
        log.info("Start income stream with consumer group: {} and consumer name: {}", groupName, consumerName);
        Consumer consumer = Consumer.from(groupName, consumerName);
        income.receiveAutoAck(consumer, StreamOffset.create("income", ReadOffset.lastConsumed()), this);
        income.start();
    }

    @PreDestroy
    public void onStop() {
        income.stop(() -> log.info("Stopped income stream"));
    }
}

package com.fooock.robotstxt.downloader.stream;

import com.fooock.robotstxt.downloader.service.UpdateUrlService;
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
public class UpdateStreamService implements StreamListener<String, MapRecord<String, String, String>> {
    private final UpdateUrlService updateUrlService;
    private final StreamMessageListenerContainer<String, MapRecord<String, String, String>> update;

    @Value("${stream.consumer.group}")
    private String groupName;

    @Value("${stream.consumer.name}")
    private String consumerName;

    public UpdateStreamService(UpdateUrlService updateUrlService, RedisConnectionFactory connectionFactory) {
        this.updateUrlService = updateUrlService;
        this.update = StreamMessageListenerContainer.create(connectionFactory);
    }

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        log.debug("Update with message: {}", message.getValue());
        updateUrlService.updateUrl(message.getValue().get("url"));
    }

    @PostConstruct
    public void onStart() {
        log.info("Start update stream with consumer group: {} and consumer name: {}", groupName, consumerName);
        Consumer consumer = Consumer.from(groupName, consumerName);
        update.receiveAutoAck(consumer, StreamOffset.create("update", ReadOffset.lastConsumed()), this);
        update.start();
    }

    @PreDestroy
    public void onStop() {
        update.stop(() -> log.info("Stopped update stream"));
    }
}

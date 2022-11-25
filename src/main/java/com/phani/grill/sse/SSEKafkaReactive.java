package com.phani.grill.sse;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/sse")
public class SSEKafkaReactive {

    @Value("${event.bootstrap.server}")
    String bootstrapServer;

    @Value("${event.client.id}")
    String clientId;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;


    public KafkaReceiver getKafkaReceiver(String topic){

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put("group.id", UUID.randomUUID().toString());
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.create.topics.enable", "true");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        return new DefaultKafkaReceiver(ConsumerFactory.INSTANCE, ReceiverOptions.create(props).subscription(Collections.singleton(topic)));
    }

    @CrossOrigin(value = "*")
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux getEventsFlux(@RequestParam String topic){
        KafkaReceiver kafkaReceiver = getKafkaReceiver(topic);
        Flux<ReceiverRecord<String,String>> kafkaFlux = kafkaReceiver.receive();
        Flux<String> flux1 = kafkaFlux.checkpoint("Messages are started being consumed for topic: " + topic).log().doOnNext(r -> r.receiverOffset().acknowledge()).map(ReceiverRecord::value).checkpoint("Messages are done consumed");
        Flux<String> flux2 = Flux.interval(Duration.ofSeconds(60)).map(sequence -> "{}");
        return Flux.merge(flux1,flux2);
    }

}

package com.phani.grill.sse;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/sse2")
public class SSEController2 {

    private static final String BOOTSTRAP_SERVERS = "";
    private static final String CLIENT_ID_CONFIG = "";

    private static final String CLUSTER_API_KEY = "";
    private static final String CLUSTER_API_SECRET = "";


//    @Autowired
//    KafkaReceiver<String,String> kafkaReceiver;

    public KafkaReceiver getKafkaReceiver(String topic){

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID_CONFIG);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        props.put("security.protocol","SASL_SSL");
        props.put("sasl.mechanism","PLAIN");
        props.put("client.dns.lookup","use_all_dns_ips");
        props.put("acks","all");
        props.put("basic.auth.credentials.source","USER_INFO");
//        props.put("basic.auth.user.info","{{ SR_API_KEY }}:{{ SR_API_SECRET }}");
        props.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule   required username='"+CLUSTER_API_KEY+"'   password='"+CLUSTER_API_SECRET+"';");


        return new DefaultKafkaReceiver(ConsumerFactory.INSTANCE, ReceiverOptions.create(props).subscription(Collections.singleton(topic)));
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux getEventsFlux(@RequestParam String topic){
        KafkaReceiver kafkaReceiver = getKafkaReceiver(topic);
        Flux<ReceiverRecord<String,String>> kafkaFlux = kafkaReceiver.receive();
        return kafkaFlux.checkpoint("Messages are started being consumed for topic: "+topic).log().doOnNext(r -> r.receiverOffset().acknowledge()).map(ReceiverRecord::value).checkpoint("Messages are done consumed").doFinally(x->{
            System.out.println("Done!!!!");
        });
    }

}

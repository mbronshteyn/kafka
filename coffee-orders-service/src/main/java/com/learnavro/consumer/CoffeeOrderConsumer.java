package com.learnavro.consumer;

import com.learnavro.domain.generated.CoffeeOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class CoffeeOrderConsumer {

    private static final String COFFEE_ORDERS = "coffee-orders";

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "coffee.order.consumer");
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(COFFEE_ORDERS));

        while (true) {
            ConsumerRecords<String, byte[]> consumerRecords = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, byte[]> consumerRecord : consumerRecords) {
                CoffeeOrder coffeeOrder = decodeAvroGreeting(consumerRecord.value());

                log.info("coffee order: {}", coffeeOrder);
            }
        }

    }

    private static CoffeeOrder decodeAvroGreeting(byte[] array) throws Exception {
        return CoffeeOrder.fromByteBuffer(ByteBuffer.wrap(array));
    }
}

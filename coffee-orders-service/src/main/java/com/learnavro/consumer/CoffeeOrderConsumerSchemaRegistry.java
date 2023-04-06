package com.learnavro.consumer;

import com.learnavro.domain.generated.CoffeeOrder;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
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
public class CoffeeOrderConsumerSchemaRegistry {

    private static final String COFFEE_ORDERS = "coffee-orders-sr";

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "coffee.order.consumer.sr");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        KafkaConsumer<String, CoffeeOrder> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(COFFEE_ORDERS));

        while (true) {
            ConsumerRecords<String, CoffeeOrder> consumerRecords = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, CoffeeOrder> consumerRecord : consumerRecords) {
                CoffeeOrder coffeeOrder = consumerRecord.value();
                log.info("coffee order: {}", coffeeOrder);
            }
        }

    }
}

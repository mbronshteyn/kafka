package com.learnavro.producer;

import com.learnavro.domain.generated.CoffeeOrder;
import com.learnavro.domain.generated.OrderId;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static com.learnavro.util.CoffeeOrderUtil.buildNewCoffeeOrder;

public class CoffeeOrderProducerSchemaRegistry {

    private static final Logger log = LoggerFactory.getLogger(CoffeeOrderProducerSchemaRegistry.class);
    private static final String COFFEE_ORDERS = "coffee-orders-sr";


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        KafkaProducer<OrderId, CoffeeOrder> producer = new KafkaProducer<OrderId, CoffeeOrder>(properties);

        CoffeeOrder coffeOrder = buildNewCoffeeOrder();

        //byte[] value = coffeOrder.toByteBuffer().array();

        ProducerRecord<OrderId, CoffeeOrder> producerRecord =
                new ProducerRecord<>(COFFEE_ORDERS, coffeOrder.getId(), coffeOrder);

        var recordMetaData = producer.send(producerRecord).get();
        log.info("recordMetaData : {}", recordMetaData);

    }

}

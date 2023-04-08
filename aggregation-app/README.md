### Aggregation App

This example reads the messages from `inventory_purchases` topic and calculates totals per key ( name of the item ) and send the item with total count to `total purchases` topic.

In order to recreate an app please create two topics with commands below ( you can trigger it with producer, but seems cleaner to create them upfront)

```angular2html
kafka-topics --bootstrap-server localhost:9092 --create --topic inventory_purchases --partitions 1 --replication-factor 1

kafka-topics --bootstrap-server localhost:9092 --create --topic total_purchases --partitions 1 --replication-factor 1
```

Start main file with `./gradlew run` task.  

Start producer on `inventory_purchaes` topic and consumer on `total_purchases` topic.

```angular2html
kafka-console-producer --broker-list localhost:9092 --topic inventory_purchases --property parse.key=true --property key.separator=:

kafka-console-consumer --bootstrap-server localhost:9092 --topic total_purchases --property print.key=true --property value.deserializer=org.apache.kafka.common.serialization.IntegerDeserializer
```

in the producer terminal type:
apples:3
oranges:4
apples:3

you should see the following results in consumer:
apples  3
oranges 4
apples 6
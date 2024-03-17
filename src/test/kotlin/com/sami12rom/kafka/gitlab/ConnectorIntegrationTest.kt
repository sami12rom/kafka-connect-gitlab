package com.sami12rom.kafka.gitlab

import net.christophschubert.cp.testcontainers.CPTestContainerFactory
import net.christophschubert.cp.testcontainers.KafkaConnectContainer
import net.christophschubert.cp.testcontainers.SchemaRegistryContainer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.Network
import org.testcontainers.lifecycle.Startables
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.util.*

class ConnectorIntegrationTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            Startables.deepStart(listOf(kafkaContainer, schemaRegistryContainer, connectContainer)).join()
        }
        @AfterAll
        @JvmStatic
        fun teardown() {
            println(connectContainer.logs)
            listOf(connectContainer, schemaRegistryContainer, kafkaContainer).forEach { it.stop() }
        }
        private val cpTestContainerFactory: CPTestContainerFactory = CPTestContainerFactory().withTag("7.6.0")
        val kafkaContainer: KafkaContainer = cpTestContainerFactory.createKafka().withReuse(true)
        val schemaRegistryContainer: SchemaRegistryContainer = cpTestContainerFactory.createSchemaRegistry(kafkaContainer).withReuse(true)
        val connectContainer: KafkaConnectContainer by lazy {
            val connect = cpTestContainerFactory.createKafkaConnect(kafkaContainer).withReuse(true)
            connect
        }
    }


    @org.junit.jupiter.api.Test
    fun testKafkaContainerIsRunning() {
        assert(kafkaContainer.isRunning)
    }

    @org.junit.jupiter.api.Test
    fun `should send and receive messages`() {
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaContainer.bootstrapServers
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        val producer = KafkaProducer<String, String>(props)

        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.GROUP_ID_CONFIG] = "test"
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"

        println("PropertiesCat: $props")

        val consumer = KafkaConsumer<String, String>(props)
        consumer.subscribe(Collections.singletonList("test"))

        val message = "test message"
        producer.send(ProducerRecord("test", message))

        val records = consumer.poll(Duration.ofSeconds(3))
        assertEquals(1, records.count())
        println("Received message: ${records.iterator().next()}")
        assertEquals(message, records.iterator().next().value())
    }

}


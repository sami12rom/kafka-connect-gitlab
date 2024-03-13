package com.sami12rom.kafka.gitlab
import io.confluent.connect.avro.AvroConverter
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertNotNull

class GitlabSourceConnectorTest {

    private lateinit var connector: GitlabSourceConnector
    // Mock the config map
    private val config = mapOf(
        GitlabSourceConfig.GITLAB_REPOSITORIES_CONFIG to "kafka/confluent_kafka_connect_aws_terraform, kafka/conluent",
        GitlabSourceConfig.GITLAB_ENDPOINT_CONFIG to "https://gitlab.essent.nl/api/v4/",
        GitlabSourceConfig.GITLAB_RESOURCES_CONFIG to "merge_requests",
        GitlabSourceConfig.GITLAB_SINCE_CONFIG to "2023-12-10T20:12:59.300Z",
        GitlabSourceConfig.TOPICS_CONFIG to "gitlab-merge-requests",
        GitlabSourceConfig.TOKEN_CONFIG to "pietje",
        GitlabSourceConfig.INTERVAL to "40000",
        "key.converter" to AvroConverter::class.java.name,
        "value.converter" to AvroConverter::class.java.name,
        AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG to "http://localhost:1234",
        "Tasks.max" to "1"
    )

    @BeforeEach
    fun setUp() {
        connector = GitlabSourceConnector()
    }

    @org.junit.jupiter.api.Test
    fun testVersion() {
        val version = connector.version()
        assertEquals("1.0.0", version) // Replace with the expected version
    }

    @org.junit.jupiter.api.Test
    fun testConfig() {
        val configDef = connector.config()
        assertNotNull(configDef, "Config should not be null")
        assertFalse(configDef.names().isEmpty(), "ConfigDef should have names")
        assertDoesNotThrow { connector.start(config.toMutableMap())}
    }

    @org.junit.jupiter.api.Test
    fun testStart() {
        val props = config.toMutableMap()
        assertDoesNotThrow { connector.start(props) }
    }

    @org.junit.jupiter.api.Test
    fun testTaskClass() {
        val taskClass = connector.taskClass()
        assertEquals(GitlabSourceTask::class.java, taskClass)
    }

    @org.junit.jupiter.api.Test
    fun testTaskConfigs() {
        connector.start(config.toMutableMap())
        val taskConfigs = connector.taskConfigs(2)
        assertEquals(2, taskConfigs.size)
    }

    @org.junit.jupiter.api.Test
    fun testStop() {
        assertDoesNotThrow { connector.stop() }
    }
}


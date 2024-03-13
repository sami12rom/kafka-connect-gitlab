package com.sami12rom.kafka.gitlab

import org.apache.kafka.common.config.ConfigDef
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.DefaultAsserter.fail
import kotlin.test.assertFalse

class GitlabSourceConfigTest {

    companion object {

        private val configDef = GitlabSourceConfig.CONFIG
        lateinit var config: MutableMap<String, String>


        @BeforeAll
        @JvmStatic
        fun setUp() {
            println(configDef.toRst())
        }
    }

    @BeforeEach
    fun init() {
        config = mutableMapOf(
            GitlabSourceConfig.GITLAB_REPOSITORIES_CONFIG to "kafka/confluent_kafka_connect_aws_terraform",
            GitlabSourceConfig.GITLAB_ENDPOINT_CONFIG to "https://gitlab.essent.nl/api/v4",
            GitlabSourceConfig.GITLAB_RESOURCES_CONFIG to "merge_requests",
            GitlabSourceConfig.GITLAB_SINCE_CONFIG to "2023-12-10T20:12:59.300Z",
            GitlabSourceConfig.TOPICS_CONFIG to "gitlab-merge-requests",
            GitlabSourceConfig.TOKEN_CONFIG to "XXXXXXXX",
            GitlabSourceConfig.INTERVAL to "40000",
        )
    }

    @org.junit.jupiter.api.Test
    fun initialConfigIsValid() {
        val invalidConfig = (configDef.validate(config).find { it.errorMessages().size > 0 } )
        if (invalidConfig != null) {
            fail("Configuration ${invalidConfig.name()} is invalid: ${invalidConfig.errorMessages().joinToString()}")
        }
    }

    @org.junit.jupiter.api.Test
    fun testConfigDef() {
        val config = configDef.configKeys()
        assertEquals(config.size, 7)
    }

    @org.junit.jupiter.api.Test
    fun testConfigDefWithDefaultValue() {
        val config = configDef.configKeys()
        assertEquals(config["gitlab.service.url"]!!.defaultValue, "https://gitlab.essent.nl/api/v4/")
    }

    @org.junit.jupiter.api.Test
    fun testConfigDefWithType() {
        val config = configDef.configKeys()
        assertEquals(config["gitlab.service.url"]!!.type, ConfigDef.Type.STRING)
    }

    @org.junit.jupiter.api.Test
    fun testConfigDefWithImportance() {
        val config = configDef.configKeys()
        assertEquals(config["gitlab.service.url"]!!.importance, ConfigDef.Importance.MEDIUM)
    }

    @org.junit.jupiter.api.Test
    fun validateSince() {
        config.put(GitlabSourceConfig.GITLAB_SINCE_CONFIG, "not-a-date")
        val configValue = configDef.validateAll(config).get(GitlabSourceConfig.GITLAB_SINCE_CONFIG)
        assertTrue((configValue?.errorMessages()?.size ?: 0) > 0)
    }
}

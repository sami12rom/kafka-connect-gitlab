package com.sami12rom.kafka.gitlab

import org.apache.commons.validator.routines.UrlValidator
import org.apache.kafka.common.config.AbstractConfig
import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.common.config.ConfigException
import java.time.Instant
import java.time.LocalDateTime

class GitlabSourceConfig : AbstractConfig {
    constructor(props: MutableMap<*, *>) : super(CONFIG, props)
    companion object {

        const val GITLAB_REPOSITORIES_CONFIG = "gitlab.repositories"
        const val GITLAB_ENDPOINT_CONFIG = "gitlab.service.url"
        const val GITLAB_RESOURCES_CONFIG = "gitlab.resources"
        const val GITLAB_SINCE_CONFIG = "gitlab.since"
        const val TOPICS_CONFIG = "topic.name.pattern"
        const val TOKEN_CONFIG = "gitlab.access.token"
        const val INTERVAL = "max.poll.interval.ms"
        const val gitlabUrl = "https://gitlab.essent.nl/api/v4/"


        val CONFIG: ConfigDef = ConfigDef()
            .define(
                /* name = */ GITLAB_REPOSITORIES_CONFIG,
                /* type = */ ConfigDef.Type.LIST, //TODO("Test List type")
                /* importance = */ ConfigDef.Importance.HIGH,
                /* documentation = */ "The GitLab repositories to read from in the form of owner/repo-name. For example, “confluentinc/kafka-connect-github, confluentinc/kafka-connect-jira”",
                /* group = */ "Settings",
                /* orderInGroup = */ -1,
                /* width = */ ConfigDef.Width.MEDIUM,
                /* displayName = */ "GitLab Repositories"
            )
            .define(
                /* name = */ GITLAB_ENDPOINT_CONFIG,
                /* type = */ ConfigDef.Type.STRING,
                /* defaultValue = */ gitlabUrl,
                /* validator = */ GitlabEndpointConfig.EndpointValidator(),
                /* importance = */ ConfigDef.Importance.MEDIUM,
                /* documentation = */ "GitLab API Root Endpoint Ex. https://gitlab.example.com/api/v4/",
                /* group = */ "Settings",
                /* orderInGroup = */ -1,
                /* width = */ ConfigDef.Width.MEDIUM,
                /* displayName = */ "GitLab Endpoint",
                /* recommender = */ GitlabEndpointConfig.EndpointRecommender()
            )
            .define(
                /* name = */ GITLAB_RESOURCES_CONFIG,
                /* type = */ ConfigDef.Type.STRING,
                /* defaultValue = */ "commits",
                /* validator = */ GitlabResourcesConfig.ResourcesValidator(),
                /* importance = */ ConfigDef.Importance.HIGH,
                /* documentation = */ "The resources that the connector extracts and writes to Kafka Ex. one of [pull_requests, commits]",
                /* group = */ "Settings",
                /* orderInGroup = */ -1,
                /* width = */ ConfigDef.Width.MEDIUM,
                /* displayName = */ "GitLab Resources",
                /* recommender = */ GitlabResourcesConfig.ResourcesRecommender()
            )
            .define(
                /* name = */ GITLAB_SINCE_CONFIG,
                /* type = */ ConfigDef.Type.STRING,
                /* defaultValue = */ "${Instant.now()}",
                /* validator = */ GitlabSinceConfig.SinceValidator(),
                /* importance = */ ConfigDef.Importance.MEDIUM,
                /* documentation = */ "Records created or updated after this time will be processed by the connector. If left blank, the default time will be set to the time this connector is launched Ex. YYYY-MM-DD or YYYY-MM-DDThh:mm:ssZ",
                /* group = */ "Settings",
                /* orderInGroup = */ -1,
                /* width = */ ConfigDef.Width.MEDIUM,
                /* displayName = */ "Since"
            )
            .define(
                /* name = */ TOKEN_CONFIG,
                /* type = */ ConfigDef.Type.PASSWORD,
                /* importance = */ ConfigDef.Importance.HIGH,
                /* documentation = */ "The supplied token will be used as the value of Authorization header in HTTP requests",
                /* group = */ "Settings",
                /* orderInGroup = */ -1,
                /* width = */ ConfigDef.Width.MEDIUM,
                /* displayName = */ "GitLab Access Token"
            )
            .define(
                /* name = */ TOPICS_CONFIG,
                /* type = */ ConfigDef.Type.STRING,
                /* importance = */ ConfigDef.Importance.MEDIUM,
                /* documentation = */ "The pattern to use for the topic name, where the \${resourceName} literal will be replaced with each entity name",
                /* group = */ "Settings",
                /* orderInGroup = */ -1,
                /* width = */ ConfigDef.Width.MEDIUM,
                /* displayName = */ "Topic Name Pattern"
            )
            .define(
                /* name = */ INTERVAL,
                /* type = */ ConfigDef.Type.LONG,
                /* defaultValue = */ 300000,
                /* importance = */ ConfigDef.Importance.LOW,
                /* documentation = */ "The time in milliseconds to wait while polling for a full batch of records Ex. 30000",
                /* group = */ "Limits",
                /* orderInGroup = */ -1,
                /* width = */ ConfigDef.Width.MEDIUM,
                /* displayName = */ "Maximum Poll Interval (ms)"
            )
    }
}


class GitlabEndpointConfig {
    class EndpointRecommender: ConfigDef.Recommender {
        override fun validValues(name: String, parsedConfig: MutableMap<String, Any>): MutableList<String> {
            return mutableListOf(
                "https://gitlab.essent.nl/api/v4/"
            )
        }
        override fun visible(name: String?, parsedConfig: MutableMap<String, Any>?): Boolean {
            return true
        }
    }
    class EndpointValidator: ConfigDef.Validator {
        override fun ensureValid(name: String?, value: Any?) {
            val url = value as String
            val validator = UrlValidator()
            if (!validator.isValid(url)) {
                throw ConfigException("$url must be a valid URL, use examples https://gitlab.example.com/api/v4/")
            }
        }
    }
}
class GitlabResourcesConfig {
    companion object {
        val validResources: MutableList<String> = mutableListOf(
            "pull_requests",
            "commits",
            "merge_requests"
        )
    }
    class ResourcesRecommender: ConfigDef.Recommender {
        override fun validValues(name: String, parsedConfig: MutableMap<String, Any>): MutableList<String> {
            return validResources
        }
        override fun visible(name: String?, parsedConfig: MutableMap<String, Any>?): Boolean {
            return true
        }
    }
    class ResourcesValidator: ConfigDef.Validator {
        override fun ensureValid(name: String?, value: Any?) {
            val resource = value as String
            if (!validResources.contains(resource)) {
                throw ConfigException("$resource is not a valid resource")
            }
        }
    }
}

class GitlabSinceConfig {
    class SinceValidator: ConfigDef.Validator {
        override fun ensureValid(name: String?, value: Any?) {
            val timestamp = value as String
            try {
                Instant.parse(timestamp)
            } catch (e: Exception) {
                throw ConfigException("$timestamp must be a valid timestamp  formatted according to ISO standards, use examples 2023-12-10T20:12:59.300Z")
            }
        }
    }
}
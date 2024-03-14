package com.sami12rom.kafka.gitlab


import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.GITLAB_REPOSITORIES_CONFIG
import com.sami12rom.kafka.gitlab.helpers.ConnectorVersionDetails
import com.sami12rom.kafka.gitlab.helpers.Utils
import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.connect.connector.Task
import org.apache.kafka.connect.source.SourceConnector
import org.apache.kafka.connect.util.ConnectorUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GitlabSourceConnector: SourceConnector() {

    companion object {
        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(GitlabSourceConnector::class.java)
    }

    private var props: Map<String, String>? = null

    override fun version(): String {
        return ConnectorVersionDetails.getVersion()
    }

    override fun config(): ConfigDef {
        return GitlabSourceConfig.CONFIG
    }

    override fun start(props: MutableMap<String, String>?) {
        logger.info("Starting GitlabSourceConnector with props: $props")
        this.props = props
    }

    override fun taskClass(): Class<out Task> {
        return GitlabSourceTask::class.java
    }

    override fun taskConfigs(maxTasks: Int): MutableList<MutableMap<String, String>> {
        val taskConfigs = mutableListOf<MutableMap<String, String>>()
        val repositories = props?.get(GITLAB_REPOSITORIES_CONFIG).toString().split(", ")
        val groups = repositories.size.coerceAtMost(maxTasks)
        val reposGrouped = ConnectorUtils.groupPartitions(repositories, groups)

        for (group in reposGrouped) {
            val taskProps = mutableMapOf<String, String>()
            taskProps.putAll(props?.toMap()!!)
            taskProps.replace(GITLAB_REPOSITORIES_CONFIG, group.joinToString(";"))
            logger.info(taskProps.toString())
            taskConfigs.add(taskProps)
        }

        return taskConfigs
        //TODO("Improve logic for taskConfigs")
    }

    override fun stop() {
        logger.info("Requested connector to stop at ${Instant.now()}")
    }

}
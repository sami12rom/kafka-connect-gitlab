package com.sami12rom.kafka.gitlab


import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.GITLAB_REPOSITORIES_CONFIG
import com.sami12rom.kafka.gitlab.helpers.ConnectorVersionDetails
import com.sami12rom.kafka.gitlab.helpers.Utils
import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.connect.connector.Task
import org.apache.kafka.connect.source.SourceConnector
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
        val numTasks = repositories.size.coerceAtMost(maxTasks)
        when (numTasks) {
            repositories.size -> {
                println("numTasks == $numTasks")
                for (item in 0..< numTasks) {
                    val taskProps = mutableMapOf<String, String>()
                    taskProps.putAll(props?.toMap()!!)
                    taskProps.replace("gitlab.repositories", repositories.get(item))
                    taskConfigs.add(taskProps)
                }
            }
            1 -> {
                println("numTasks == $numTasks")
                for (item in 0..< numTasks) {
                    val taskProps = mutableMapOf<String, String>()
                    taskProps.putAll(props?.toMap()!!)
                    taskConfigs.add(taskProps)
                }
            }
            2 -> {
                println("numTasks == $numTasks")
                val groups = Utils.randomizer(repositories)
                for (item in 0..< numTasks) {
                    val taskProps = mutableMapOf<String, String>()
                    taskProps.putAll(props?.toMap()!!)
                    taskProps.replace("gitlab.repositories", groups.toList().get(item).toString()
                        .replace("[", "")
                        .replace("]", "")
                        .replace(" ", "")
                    )
                    taskConfigs.add(taskProps)
                }
            }
        }
        return taskConfigs
    }

    override fun stop() {
        logger.info("Requested connector to stop at ${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)}")
    }

}
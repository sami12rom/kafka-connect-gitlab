package com.sami12rom.kafka.gitlab

import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.GITLAB_REPOSITORIES_CONFIG
import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.GITLAB_RESOURCES_CONFIG
import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.GITLAB_SINCE_CONFIG
import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.INTERVAL
import com.sami12rom.kafka.gitlab.Schemas.Companion.mergedRequestKeySchema
import com.sami12rom.kafka.gitlab.Schemas.Companion.mergedRequestValueSchema
import com.sami12rom.kafka.gitlab.Structs.Companion.mergedRequestKeyStruct
import com.sami12rom.kafka.gitlab.Structs.Companion.mergedRequestValueStruct
import com.sami12rom.kafka.gitlab.helpers.ApiCalls
import com.sami12rom.kafka.gitlab.helpers.ConnectorVersionDetails
import com.sami12rom.kafka.gitlab.model.MergedRequest
import org.apache.kafka.connect.header.ConnectHeaders
import org.apache.kafka.connect.header.Headers
import org.apache.kafka.connect.source.SourceRecord
import org.apache.kafka.connect.source.SourceTask
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

class GitlabSourceTask : SourceTask() {

    companion object {
        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(GitlabSourceTask::class.java)
    }

    var props: MutableMap<String, String>? = null
    private var firstRun: Boolean = true
    private var validateRepositories: List<String>? = null

    override fun version(): String {
        return ConnectorVersionDetails.getVersion()
    }

    override fun start(props: MutableMap<String, String>?) {
        logger.info("Starting task ${GitlabSourceTask::class.java}")
        logger.info("Configuration: $props")
        this.props = props
        this.validateRepositories = props?.get(GITLAB_REPOSITORIES_CONFIG)?.split(";")
        initializeSource()
    }

    override fun stop() {
        logger.info("Requested task to stop")
    }

    override fun poll(): MutableList<SourceRecord> {
        val records = mutableListOf<SourceRecord>()
        sleepForInterval()
        val currentDateTime = Instant.now()

        try {
            for (repository in validateRepositories!!) {
                props?.put(GITLAB_REPOSITORIES_CONFIG, repository)
                val response = ApiCalls.GitLabCall(props!!)
                for (message in response) {
                    val record = generateSourceRecord(message as MergedRequest)
                    records.add(record)
                }
            }
            // Update the offset to the current time
            props?.put(GITLAB_SINCE_CONFIG, currentDateTime.toString())

        } catch (e: Exception) {
            throw e
        }
        logger.info("Records sent till ${currentDateTime}: ${records.size}")
        return records
    }

    private fun sourcePartition(): Map<String, String> {
        return mapOf(
            GITLAB_REPOSITORIES_CONFIG to props?.get(GITLAB_REPOSITORIES_CONFIG)!!,
            GITLAB_RESOURCES_CONFIG to props?.get(GITLAB_RESOURCES_CONFIG)!!
        )
    }

    fun sourceOffset(lastCallTime: Instant): Map<String, String> {
        return mapOf(
            GITLAB_SINCE_CONFIG to maxOf(lastCallTime, Instant.now()).toString()
        )
    }

    fun initializeSource(): Map<String, Any>?{
         val lastRecordedOffset = context.offsetStorageReader().offset(sourcePartition())
         if (lastRecordedOffset != null) {
             // Use the last recorded offset to start reading from the source
             logger.info("Last recorded offset: ${lastRecordedOffset.get(GITLAB_SINCE_CONFIG)}")
             props?.put(GITLAB_SINCE_CONFIG, lastRecordedOffset.get(GITLAB_SINCE_CONFIG) as String)
         } else {
             logger.info("No offset recorded, start reading from the beginning or a default position")
         }
        return lastRecordedOffset
    }

    fun sleepForInterval() {
        val interval = props!!.get(INTERVAL)!!.toLong()
        if (!firstRun) {
            logger.info("Sleep for interval: ${interval/1000} seconds")
            Thread.sleep(interval)
        } else {
            firstRun = false
        }
    }
    fun generateHeaders(): Headers {
        return ConnectHeaders()
            .addString(GITLAB_REPOSITORIES_CONFIG, props?.get(GITLAB_REPOSITORIES_CONFIG))
            .addString(GITLAB_SINCE_CONFIG, props?.get(GITLAB_SINCE_CONFIG))
    }

    fun generateSourceRecord(message: MergedRequest): SourceRecord {
        return SourceRecord(
            /* sourcePartition = */ sourcePartition(),
            /* sourceOffset = */ sourceOffset(Instant.parse(props?.get(GITLAB_SINCE_CONFIG))),
            /* topic = */ props!!.get("topic.name.pattern"),
            /* partition = */ null,
            /* keySchema = */ mergedRequestKeySchema,
            /* key = */ mergedRequestKeyStruct(message),
            /* valueSchema = */ mergedRequestValueSchema,
            /* value = */ mergedRequestValueStruct(message),
            /* timestamp = */ Instant.now().epochSecond,
            /* headers = */ generateHeaders()
        )
    }
}





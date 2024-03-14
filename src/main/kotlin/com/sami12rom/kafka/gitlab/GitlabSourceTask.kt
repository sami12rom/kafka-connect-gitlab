package com.sami12rom.kafka.gitlab

import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.GITLAB_REPOSITORIES_CONFIG
import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.GITLAB_RESOURCES_CONFIG
import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.GITLAB_SINCE_CONFIG
import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.INTERVAL
import com.sami12rom.kafka.gitlab.helpers.ApiCalls
import com.sami12rom.kafka.gitlab.helpers.ConnectorVersionDetails
import org.apache.kafka.connect.data.Struct
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

    private var props: MutableMap<String, String>? = null
//    private var interval: Long? = props?.get("max.poll.interval.ms")?.toLong()?: 3000
    private var lastPoll: Long = 0
    private var firstRun: Boolean = true

    override fun version(): String {
        return ConnectorVersionDetails.getVersion()
    }

    override fun start(props: MutableMap<String, String>?) {
        logger.info("Starting task ${GitlabSourceTask::class.java}")
        logger.info("Configuration: $props")
        // source = MySourceSystem(props)
        this.props = props
        initializeSource()
    }


    override fun stop() {
        logger.info("Requested task to stop")
    }

    override fun poll(): MutableList<SourceRecord> {
        sleepForInterval()
        val records = mutableListOf<SourceRecord>()
        try {
            val validateRepositories = props?.get(GITLAB_REPOSITORIES_CONFIG)?.split(";")
            for (repository in validateRepositories!!) {
                props?.put(GITLAB_REPOSITORIES_CONFIG, repository)

                val response = ApiCalls.GitLabCall(props!!)
                for (message in response) {
                    val record = SourceRecord(
                        /* sourcePartition = */ sourcePartition(),
                        /* sourceOffset = */ sourceOffset(Instant.parse(props?.get(GITLAB_SINCE_CONFIG))),
                        /* topic = */ props!!.get("topic.name.pattern"),
                        /* valueSchema = */ Schemas.mergedRequestValueSchema,
                        /* value = */ Structs().structMergedRequestValue(message as MergedRequest),
                    )
                    records.add(record)
                }
            }

        } catch (e: Exception) {
            throw e
        }
        return records
        //TODO("Improve Logic")
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
             println("Last recorded offset: ${lastRecordedOffset.get(GITLAB_SINCE_CONFIG)}")
             props?.put(GITLAB_SINCE_CONFIG, lastRecordedOffset.get(GITLAB_SINCE_CONFIG) as String)
         } else {
                println("No offset recorded, start reading from the beginning or a default position")
         }
        return lastRecordedOffset
    }

    fun sleepForInterval() {
        val currentTime = System.currentTimeMillis()
        println(Date(currentTime))
        val interval = props!!.get(INTERVAL)!!.toLong()
        println("Sleep for interval: ${interval/1000} seconds")
        if (!firstRun) {
            Thread.sleep(interval)
        } else {
            firstRun = false
        }
    }
}


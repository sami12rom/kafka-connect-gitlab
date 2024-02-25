package com.sami12rom.kafka.gitlab

import com.sami12rom.kafka.gitlab.helpers.ApiCalls
import com.sami12rom.kafka.gitlab.helpers.ConnectorVersionDetails
import org.apache.kafka.connect.data.Struct
import org.apache.kafka.connect.source.SourceRecord
import org.apache.kafka.connect.source.SourceTask
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GitlabSourceTask : SourceTask() {

    companion object {
        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(GitlabSourceTask::class.java)
    }

    private var props: MutableMap<String, String>? = null
//    private var interval: Long? = props?.get("max.poll.interval.ms")?.toLong()?: 3000
    private var lastPoll: Long = 0

    override fun version(): String {
        return ConnectorVersionDetails.getVersion()
    }

    override fun start(props: MutableMap<String, String>?) {
        logger.info("Starting task ${GitlabSourceTask::class.java}")
        logger.info("Configuration: $props")
        this.props = props
    }

    override fun stop() {
        logger.info("Requested task to stop")
    }

    override fun poll(): MutableList<SourceRecord> {
        val records = mutableListOf<SourceRecord>()
        try {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastPoll < (props?.get("max.poll.interval.ms")?.toLong() ?: 30000)) {
            }
            lastPoll = currentTime

            val response = ApiCalls.GitLabCall(props!!)

            for (message in response) {
                val record = SourceRecord(
                    null,
                    null,
                    props!!.get("topic.name.pattern"),
                    Schemas.mergedRequestValueSchema,
                    Structs().structMergedRequestValue(message as MergedRequest),
                )
                records.add(record)
            }

        } catch (e: Exception) {
            throw e
        }
        return records
        //TODO("Improve Logic")
    }
}
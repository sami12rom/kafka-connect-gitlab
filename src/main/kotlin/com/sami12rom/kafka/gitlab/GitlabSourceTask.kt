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
    // private lateinit var source: MySourceSystem

    override fun version(): String {
        return ConnectorVersionDetails.getVersion()
    }

    override fun start(props: MutableMap<String, String>?) {
        logger.info("Starting task ${GitlabSourceTask::class.java}")
        logger.info("Configuration: $props")
        // source = MySourceSystem(props)
        this.props = props

        // val lastRecordedOffset = context.offsetStorageReader().offset(sourcePartition())
        // if (lastRecordedOffset != null) {
        //     // Use the last recorded offset to start reading from the source
        //     source.startReadingFrom(lastRecordedOffset)
        // } else {
        //     // No offset recorded, start reading from the beginning or a default position
        //     source.startReadingFromDefault()
        // }
    }
    
    private fun sourcePartition(): Map<String, String> {
        // Return a map that uniquely represents the source partition
        return mapOf("source" to "my-source")
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
                    /* sourcePartition = */ null,
                    /* sourceOffset = */ null,
                    /* topic = */ props!!.get("topic.name.pattern"),
                    /* valueSchema = */ Schemas.mergedRequestValueSchema,
                    /* value = */ Structs().structMergedRequestValue(message as MergedRequest),
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
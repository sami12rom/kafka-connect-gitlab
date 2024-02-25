package com.sami12rom.kafka.gitlab

import org.apache.kafka.connect.source.SourceRecord
import org.apache.kafka.connect.source.SourceTask

class GitlabSourceTaskTest: SourceTask() {
    override fun version(): String {
        TODO("Not yet implemented")
    }

    override fun start(props: MutableMap<String, String>?) {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun poll(): MutableList<SourceRecord> {
        TODO("Not yet implemented")
    }
}
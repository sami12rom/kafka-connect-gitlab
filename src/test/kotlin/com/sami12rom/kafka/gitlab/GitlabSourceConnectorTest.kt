package com.sami12rom.kafka.gitlab

import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.connect.connector.Task
import org.apache.kafka.connect.source.SourceConnector
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GitlabSourceConnectorTest: SourceConnector() {



    override fun version(): String {
        TODO("Not yet implemented")
    }

    override fun start(props: MutableMap<String, String>?) {
        TODO("Not yet implemented")
    }

    override fun taskClass(): Class<out Task> {
        TODO("Not yet implemented")
    }

    override fun taskConfigs(maxTasks: Int): MutableList<MutableMap<String, String>> {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun config(): ConfigDef {
        TODO("Not yet implemented")
    }
}
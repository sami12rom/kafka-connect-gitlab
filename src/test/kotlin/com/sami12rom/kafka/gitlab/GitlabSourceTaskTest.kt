package com.sami12rom.kafka.gitlab

import com.sami12rom.kafka.gitlab.helpers.ConnectorVersionDetails
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import java.util.*

class GitlabSourceTaskTest {

    private lateinit var gitlabSourceTask: GitlabSourceTask

    @BeforeEach
    fun setUp() {
        gitlabSourceTask = GitlabSourceTask()
    }

    @org.junit.jupiter.api.Test
    fun testVersion() {
        val version = gitlabSourceTask.version()
        assertEquals(ConnectorVersionDetails.getVersion(), version)
    }

    @org.junit.jupiter.api.Test
    fun testStart() {
        val props = mutableMapOf("max.poll.interval.ms" to "5000")
        gitlabSourceTask.start(props)
        assertEquals(props, gitlabSourceTask.props)
    }

    @org.junit.jupiter.api.Test
    fun testStop() {
        gitlabSourceTask.stop()

        // Add assertions or verify any necessary behavior
    }

}
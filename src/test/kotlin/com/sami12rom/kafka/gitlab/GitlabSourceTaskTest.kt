//package com.sami12rom.kafka.gitlab
//
//import org.apache.kafka.connect.source.SourceRecord
//import org.apache.kafka.connect.source.SourceTask
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Test
//import org.junit.jupiter.api.BeforeEach
//import org.mockito.Mockito.*
//import java.util.*
//
//class GitlabSourceTaskTest {
//
//    private lateinit var gitlabSourceTask: GitlabSourceTask
//
//    @BeforeEach
//    fun setUp() {
//        gitlabSourceTask = GitlabSourceTask()
//    }
//
//    @org.junit.jupiter.api.Test
//    fun testVersion() {
//        val version = gitlabSourceTask.version()
//        assertEquals(ConnectorVersionDetails.getVersion(), version)
//    }
//
//    @org.junit.jupiter.api.Test
//    fun testStart() {
//        val props = mutableMapOf("max.poll.interval.ms" to "5000")
//        gitlabSourceTask.start(props)
//        assertEquals(props, gitlabSourceTask.props)
//    }
//
//    @org.junit.jupiter.api.Test
//    fun testStop() {
//        gitlabSourceTask.stop()
//        // Add assertions or verify any necessary behavior
//    }
//
//    @org.junit.jupiter.api.Test
//    fun testPoll() {
//        // Mock the necessary dependencies and setup the test data
//        val props = mutableMapOf("max.poll.interval.ms" to "5000")
//        gitlabSourceTask.start(props)
//
//        val response = listOf(
//            MergedRequest(/* Test data */),
//            MergedRequest(/* Test data */),
//            MergedRequest(/* Test data */)
//        )
//        `when`(ApiCalls.GitLabCall(props)).thenReturn(response)
//
//        // Call the method under test
//        val records = gitlabSourceTask.poll()
//
//        // Add assertions to verify the expected behavior
//        assertEquals(response.size, records.size)
//        // Verify any necessary behavior using Mockito's verify() method
//        verify(ApiCalls).GitLabCall(props)
//    }
//
//}
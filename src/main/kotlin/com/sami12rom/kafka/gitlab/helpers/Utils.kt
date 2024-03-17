package com.sami12rom.kafka.gitlab.helpers

import com.sami12rom.kafka.gitlab.GitlabSourceConfig
import com.sami12rom.kafka.gitlab.GitlabSourceConnector
import com.sami12rom.kafka.gitlab.GitlabSourceTask
import com.sami12rom.kafka.gitlab.model.MergedRequest
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.decodeFromString
import org.apache.kafka.connect.source.SourceRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

class Utils {
    companion object {

        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(Utils::class.java)

        fun randomizer(list: List<String>): Pair<List<String>, List<String>> {
            while (true) {
                val randomizedList = list.shuffled().partition { Random.nextBoolean() }
                if (randomizedList.first.isNotEmpty() && randomizedList.second.isNotEmpty()) {
                    return randomizedList
                }
            }
        }

        fun retryOnError(function: () -> HttpURLConnection): HttpURLConnection {
            // A variable to keep track of the number of attempts
            var attempts = 0
            // A variable to keep track of the waiting time
            var waitTime = 1000L // 1 second in milliseconds
            // A loop that runs until the function succeeds or the attempts reach 3
            while (true) {
                try {
                    // Call the function and break the loop if no exception is thrown
                    return function()
                } catch (e: Exception) {
                    // Increment the attempts and print the error message
                    attempts++
                    println("Error: ${e.message}")
                    // If the attempts are 3, rethrow the exception and exit the loop
                    if (attempts == 3) {
                        throw e
                    }
                    // Otherwise, wait for the specified time and double it for the next attempt
                    Thread.sleep(waitTime)
                    waitTime *= 2
                }
            }
        }

        fun apiStructCall(url: URL, token: String = ""): HttpURLConnection {
            var connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Authorization", token)
                logger.info("url: $url")
                connect()
            }
            return try {
                val response = connection
                response
            } finally {
                connection.disconnect()
            }
        }
        suspend fun processRepositoriesAsync(
            validateRepositories: List<String>?,
            props: MutableMap<String, String>?,
            records: MutableList<SourceRecord>,
            generateSourceRecord: (message: MergedRequest) -> SourceRecord) = coroutineScope {
            validateRepositories?.map { repository ->
                async {
                    GitlabSourceTask.logger.info("Processing repository: $repository on thread: ${Thread.currentThread()}")
                    props?.put(GitlabSourceConfig.GITLAB_REPOSITORIES_CONFIG, repository)
                    val response = ApiCalls.GitLabCall(props!!)
                    for (message in response) {
                        val record = generateSourceRecord(message as MergedRequest)
                        records.add(record)
                    }
                }.await()
            }
        }
    }
}



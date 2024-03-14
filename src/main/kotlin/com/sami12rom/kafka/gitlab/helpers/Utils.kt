package com.sami12rom.kafka.gitlab.helpers

import com.sami12rom.kafka.gitlab.MergedRequest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.decodeFromString
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

class Utils {
    companion object {
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

        fun apiCall(url: URL, token: String = ""): JsonElement? {
            var connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", token)
            connection.connect()
            // Read the response as a string
            val response = connection.inputStream.bufferedReader().readText()
            connection.disconnect()
            val parsed = Json.parseToJsonElement(response)
//            println(parsed)
            return parsed.jsonArray
        }

        fun apiStructCall(url: URL, token: String = ""): HttpURLConnection {
            var connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Authorization", token)
                connect()
            }
            return try {
                val response = connection
                response
            } finally {
                connection.disconnect()
            }
        }


//
//
//        fun getCall(apiUrl: String): Any {
//            for (attempt in 1..3) {  // Retry up to 3 times
//                try {
//                    val url = URL(apiUrl)
//                    var connection = url.openConnection() as HttpURLConnection
//                    connection.requestMethod = "GET"
//                    connection.connect()
//                    // Read the response as a string
//                    val response = connection.inputStream.bufferedReader().readText()
//                    connection.disconnect()
////                    val joke = Json.decodeFromString<Joke>(response)
//                    return response
//                } catch (e: Exception) {
//                    if (attempt < 3) {
//                        println("API call failed, retrying in 5 seconds...")
//                        TimeUnit.SECONDS.sleep(5)  // Introduce delay between retries
//                    } else {
//                        throw RuntimeException("Error calling API", e)
//                    }
//                }
//            }
//            // If all retries fail, throw a RuntimeException
//            throw RuntimeException("Failed to fetch response after 3 attempts")
//        }
//
//        fun GitLabApi(endpoint: String): JsonElement? {
//            for (attempt in 1..3) {  // Retry up to 3 times
//                try {
//                    val url = URL("https://gitlab.essent.nl/api/v4/$endpoint")
//                    var connection = url.openConnection() as HttpURLConnection
//                    connection.requestMethod = "GET"
//                    connection.setRequestProperty("Authorization", "Bearer 7mXVJRFyzsjH_bEsQSc4")
//                    connection.connect()
//                    // Read the response as a string
//                    val response = connection.inputStream.bufferedReader().readText()
//                    connection.disconnect()
//                    val parsed = Json.parseToJsonElement(response)
//                    return parsed.jsonArray
//                } catch (e: Exception) {
//                    if (attempt < 3) {
//                        println("API call failed, retrying in 5 seconds...")
//                        TimeUnit.SECONDS.sleep(1)  // Introduce delay between retries
//                    } else {
//                        throw RuntimeException("Error calling API", e)
//                    }
//                }
//            }
//            // If all retries fail, throw a RuntimeException
//            throw RuntimeException("Failed to fetch response after 3 attempts")
//        }

    }
}



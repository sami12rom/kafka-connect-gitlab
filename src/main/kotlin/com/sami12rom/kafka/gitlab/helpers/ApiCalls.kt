package com.sami12rom.kafka.gitlab.helpers


//import java.util.logging.Logger
import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.GITLAB_ENDPOINT_CONFIG
import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.GITLAB_REPOSITORIES_CONFIG
import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.GITLAB_RESOURCES_CONFIG
import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.GITLAB_SINCE_CONFIG
import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.TOKEN_CONFIG
import com.sami12rom.kafka.gitlab.GitlabSourceConfig.Companion.gitlabUrl
import com.sami12rom.kafka.gitlab.GitlabSourceTask
import com.sami12rom.kafka.gitlab.MergedRequest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.net.URLEncoder
import java.util.Dictionary

class ApiCalls {
    companion object {

        fun GitLabCall(props: MutableMap<String, String>): List<Any> {
            val repositories =  URLEncoder.encode(props.get(GITLAB_REPOSITORIES_CONFIG), "UTF-8")
            val resources = props.get(GITLAB_RESOURCES_CONFIG)
            val since = props.get(GITLAB_SINCE_CONFIG)
            val token = "Bearer ${props.get(TOKEN_CONFIG)}"
            val url = (props.get(GITLAB_ENDPOINT_CONFIG)?: gitlabUrl)
                    .plus("projects/")
                    .plus("$repositories/")
                    .plus("$resources?")
                    .plus("per_page=100&")
                    .plus("scope=all&")
                    .plus("created_after=$since$")

            try {
                val response = Utils.retryOnError { Utils.apiStructCall(URL(url), token) }
                val totalPages = response.headerFields.get("X-Total-Pages")?.get(0)?.toInt()?: 1
                var body = response.inputStream.bufferedReader().readText()
                val json = Json { ignoreUnknownKeys = true ; prettyPrint = true}
                var parsed = json.parseToJsonElement(body).jsonArray
                for (i in 2..totalPages) {
                    val response = Utils.retryOnError {
                        Utils.apiStructCall(
                            URL(
                                url.plus("page=$i")), token)
                    }
                    var parsedItem = json.parseToJsonElement(
                        response.inputStream.bufferedReader().readText()
                    ).jsonArray
                    parsed = JsonArray(parsed.plus(parsedItem))
                }
                return json.decodeFromJsonElement<List<MergedRequest>>(parsed)
            } catch (e: Exception) {
                throw RuntimeException("Error calling GitLab API", e)
            }
        }
    }
}


package com.sami12rom.kafka.gitlab.helpers


//import java.util.logging.Logger
import com.sami12rom.kafka.gitlab.MergedRequest
import java.net.URL
import java.net.URLEncoder

class ApiCalls {
    companion object {
        fun GitLabCall(props: MutableMap<String, String>): List<Any> {
            val repositories =  URLEncoder.encode(props.get("gitlab.repositories"), "UTF-8")
            val resources = props.get("gitlab.resources")
            val since = props.get("gitlab.since")
            val token = "Bearer ${props.get("gitlab.access.token")}"
            val url = URL(
                props.get("gitlab.service.url")
                    .plus("projects/")
                    .plus("$repositories/")
                    .plus("$resources?")
                    .plus("scope=all&")
                    .plus("created_at=$since")
            )
            try {
                return Utils.retryOnError {
//                       Utils.apiCall(url, token)
                    Utils.apiStructCall(url, token)
                }
            } catch (e: Exception) {
                throw RuntimeException("Error calling GitLab API", e)
            }
        }
    }
}

//

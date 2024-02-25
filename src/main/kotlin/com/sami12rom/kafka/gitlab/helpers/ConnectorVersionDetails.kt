package com.sami12rom.kafka.gitlab.helpers

class ConnectorVersionDetails {

    companion object {
        const val TIMESTAMP = "Build-Timestamp"
        const val VERSION = "Connector-Version"

        fun loadVersion(resourceName: String = "version.properties"): ConnectorVersionDetails {
            val input = ConnectorVersionDetails::class.java.getResourceAsStream("$resourceName")
            println(ConnectorVersionDetails.javaClass.classLoader.getResourceAsStream("/$resourceName"))
            println(input)
            return ConnectorVersionDetails()
        }

        fun getVersion(): String {
            return """
            ${javaClass.simpleName} Version: 1.0.0,
            Author: Sami Alashabi
        """.trimIndent().replace("\n", ", ")
        }
    }
}
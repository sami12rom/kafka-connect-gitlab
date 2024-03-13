package com.sami12rom.kafka.gitlab.helpers

class ConnectorVersionDetails {

    companion object {

        fun getVersion(): String {
            return ConnectorVersionDetails::class.java.`package`.implementationVersion ?: "1.0.0"
        }
    }
}
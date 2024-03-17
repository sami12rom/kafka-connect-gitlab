/**
 * This is the build.gradle.kts file for the Kafka Gitlab Source Connector project.
 * It configures the build settings, dependencies, and tasks for the project.
 *
 * The file includes the following sections:
 * - Plugins: Defines the plugins used in the project, such as the Java and Kotlin plugins.
 * - Allprojects: Configures the project settings, repositories, and dependencies.
 * - Tasks: Defines the tasks for the project, such as running tests and generating version information.
 * - Publishing: Configures the publication of the project artifacts to a Maven repository.
 *
 * For more information on how to build and publish the Kafka Gitlab Source Connector project, refer to the project's documentation.
 */

import java.time.LocalDateTime

plugins {
    `java-library`
    kotlin("jvm") version "1.9.22"
    id("com.palantir.git-version") version "1.0.0"
    kotlin("plugin.serialization") version "1.5.0"
    `maven-publish`
}

allprojects {
    val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
    val details = versionDetails()
    val currentDateTime: LocalDateTime = LocalDateTime.now()

    group = "com.sami12rom.kafka.gitlab"
    version = "1.0.4-${details.gitHash}"
    description = "Kafka Gitlab Source Connector"

    repositories {
        mavenCentral()
        maven { url = uri("https://packages.confluent.io/maven/") }
        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        implementation("org.apache.kafka:connect-api:3.4.0")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
        implementation("org.slf4j:slf4j-api:2.0.6")
        implementation("commons-validator:commons-validator:1.7")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        //// Testing dependencies
        //  A library for converting data between Avro and Kafka Connect formats.
        testImplementation("io.confluent:kafka-connect-avro-converter:7.3.0") 
        // A library that provides a set of utilities for running Kafka Connect in a test environment.
        testImplementation("com.github.christophschubert:cp-testcontainers:v0.2.1")
        // A library for testing Kafka Connect connectors allowing to use a Kafka instance in your tests.
        testImplementation("org.testcontainers:kafka:1.19.6")
        // A library for testing allowing interacting with the Kafka Connect REST API.
        testImplementation("org.sourcelab:kafka-connect-client:4.0.3")
        // A lightweight logging framework for Kotlin, a wrapper for slf4j with Kotlin extensions
        testImplementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
        // Mockito is a mocking framework for unit tests in Java.
        testImplementation("org.mockito:mockito-core:3.12.4")
        // JUnit Jupiter is the combination of the new programming model and extension model for writing tests and extensions in JUnit 5.
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.2") // JUnit Jupiter
        testRuntimeOnly("org.junit.platform:junit-platform-launcher") // JUnit Platform Launcher
        // Standard library for writing test in Kotlin
        testImplementation(kotlin("test"))
    }

//        implementation("org.json:json:20230618")
//        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
//        implementation("org.apache.avro:avro:1.11.2")
//        implementation("org.apache.kafka:connect-runtime:3.4.0")

    tasks.test {
        useJUnitPlatform()
        reports.html.required.set(true)
    }

    tasks.jar {
        val dependencies = configurations
            .runtimeClasspath
            .get()
            //Include kotlin specific runtime jars
            .filter { it.name.contains("kotlin") }
            .map(::zipTree) // OR .map { zipTree(it) }
        from(dependencies)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        manifest {
            attributes(
                mapOf(
                    "Build-Timestamp" to currentDateTime,
                    "Build-Revision" to details.gitHash,
                    "Build-Is-Clean" to details.isCleanTag,
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version,
                )
            )
        }
    }

    tasks.register("generateVersion") {
        description = "Create a version properties file in the build folder"
        doLast {
            sourceSets.main.get().output.resourcesDir?.mkdirs()
            file("${sourceSets.main.get().output.resourcesDir}/version.properties").writeText(
                """
            Build-Timestamp=${currentDateTime}
            Build-Revision=${details.gitHash}
            Connector-Version=${project.version},
            "Implementation-Title" to ${project.name},
            "Implementation-Version" to ${project.version},
        """.trimIndent()
            )
        }
    }
    tasks.build {
        dependsOn(":generateVersion")
    }


    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }

    tasks.withType<PublishToMavenRepository> {
        enabled = true
    }
}

publishing {
    publications {
        create<MavenPublication>("gitlabKafkaConnector") {
            groupId = group as String
            artifactId = "gitlab-connector"
            version = version
            description = description
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://essent-eda-587693564902.d.codeartifact.eu-central-1.amazonaws.com/maven/gitlabKafkaConnector/")
            credentials {
                username = "aws"
                password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
            }
        }
    }
}

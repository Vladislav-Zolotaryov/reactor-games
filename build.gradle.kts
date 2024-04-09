plugins {
    kotlin("jvm") version "1.9.23"
    id("idea")
}

group = "org.learning"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    val reactorVersion = "3.6.4"
    implementation("io.projectreactor:reactor-core:$reactorVersion")
    testImplementation("io.projectreactor:reactor-test:$reactorVersion")

    val kotestVersion = "5.8.1"
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
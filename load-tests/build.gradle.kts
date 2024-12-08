val gatling_version: String by project

plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.allopen") version "2.1.0"

    id("io.gatling.gradle") version "3.13.1"
}

repositories {
    mavenCentral()
}

gatling {}

dependencies {
    implementation("io.gatling.highcharts:gatling-charts-highcharts:$gatling_version")
    implementation("io.gatling:gatling-http:$gatling_version")
}

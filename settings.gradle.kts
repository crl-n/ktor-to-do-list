rootProject.name = "ktor-to-do-list"
include("application")
include("load-tests")

pluginManagement {
    val kotlin_version: String by settings

    plugins {
        kotlin("jvm") version kotlin_version
        kotlin("plugin.allopen") version kotlin_version
    }
}

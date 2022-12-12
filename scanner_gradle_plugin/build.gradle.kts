plugins {
    kotlin("jvm") version "1.6.21"
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.1.0"
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleApi())
}

gradlePlugin {
    plugins {
        create("org.archguard.scanner.gradle.plugin") {
            id = "org.archguard.scanner.gradle.plugin"
            implementationClass = "org.archguard.scanner.gradle.plugin.ArchguardPlugin"
            version = "0.0.1"
            displayName = "ArchGuard"
        }
    }
}

pluginBundle {
    website = "https://github.com/archguard/archguard"
    vcsUrl = "https://github.com/archguard/archguard"
    description = "ArchGuard"
    tags = listOf("plugin", "gradle")
}

tasks.create("setupPluginUploadFromEnvironment") {
    doLast {
        val key = System.getenv("GRADLE_PUBLISH_KEY")
        val secret = System.getenv("GRADLE_PUBLISH_SECRET")

        if (key == null || secret == null) {
            throw GradleException("gradlePublishKey and/or gradlePublishSecret are not defined environment variables")
        }

        System.setProperty("gradle.publish.key", key)
        System.setProperty("gradle.publish.secret", secret)
    }
}


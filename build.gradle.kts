buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.4.32")
        classpath("org.jfrog.buildinfo:build-info-extractor-gradle:4.21.0")
    }
}

plugins {

    id("org.jetbrains.dokka") version "1.4.0"
    id("maven-publish")
    id( "com.jfrog.artifactory") version "4.21.0"
}

tasks.dokkaHtmlMultiModule {
    outputDirectory.set(rootDir.resolve("docs"))
}

allprojects {
    group = "org.sagebionetworks.assessmentmodel"
    version = "0.4.3"

    repositories {
        jcenter()
        google()
    }
}

artifactory {
    setContextUrl("https://sagebionetworks.jfrog.io/artifactory")
    publish(delegateClosureOf<org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig> {
        repository(delegateClosureOf<org.jfrog.gradle.plugin.artifactory.dsl.DoubleDelegateWrapper> {
            val username = System.getenv("artifactoryUser")
            val password = System.getenv("artifactoryPwd")
            setProperty("repoKey", "mobile-sdks")
            setProperty("username", username)
            setProperty("password", password)
            setProperty("maven", true)
        })
        defaults(delegateClosureOf<groovy.lang.GroovyObject> {
            invokeMethod("publications", arrayOf(
                "ALL_PUBLICATIONS"
            ))
            setProperty("publishArtifacts", true)
        })
    })
}



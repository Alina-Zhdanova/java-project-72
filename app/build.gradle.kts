import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.github.ben-manes.versions") version "0.51.0"
    application
    checkstyle
    jacoco
    id("org.sonarqube") version "6.2.0.5505"
    id("io.freefair.lombok") version "8.13.1"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("hexlet.code.App")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.1.3")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("io.javalin:javalin-rendering:6.1.3")
    implementation("gg.jte:jte:3.1.9")
    implementation("com.h2database:h2:2.2.220")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("gg.jte:jte:3.1.9")
    implementation("io.javalin:javalin-rendering:6.1.3")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.test {
    useJUnitPlatform()
    // https://technology.lastminute.com/junit5-kotlin-and-gradle-dsl/
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        // showStackTraces = true
        // showCauses = true
        showStandardStreams = true
    }
}

tasks.jacocoTestReport { reports { xml.required.set(true) } }

sonar {
    properties {
        property("sonar.projectKey", "Alina-Zhdanova_java-project-722")
        property("sonar.organization", "alina-zhdanova")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
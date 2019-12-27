import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val junitPlatformVersion = "1.5.2"
val jupiterVersion = "5.5.2"

val statemachineVersion= "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.3.60"
}

group = "io.mbo.labs"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("io.mbo.labs:mbo-statemachine:$statemachineVersion")

    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")
    testImplementation("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0-Beta")
    }
}

apply(plugin = "kotlin")

plugins {
    id("java")

    id("com.google.devtools.ksp") version "2.1.0-Beta2-1.0.26"
}


group = "com.traderbird"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.11")
    implementation("com.google.auto.service:auto-service-annotations:1.0")
    implementation(kotlin("stdlib"))
}

tasks.test {
    useJUnitPlatform()
}
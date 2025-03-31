import java.io.IOException

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
    idea
    application
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.22"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.springframework.boot") version "+"
    id("io.spring.dependency-management") version "1.1.0"
    id("com.github.node-gradle.node") version "7.1.0"
    id("org.jetbrains.kotlin.plugin.spring") version "+"

    id("com.google.devtools.ksp") version "2.1.0-Beta2-1.0.26"

}

repositories {
    mavenCentral()
}

group = "com.traderbird"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc:10.2.0.jre11")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("mysql:mysql-connector-java:8.0.28")

    // jjwt dependencies
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // jakarta
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    runtimeOnly("org.glassfish.jaxb:jaxb-runtime:3.0.1")
    // servlet
    implementation("jakarta.servlet:jakarta.servlet-api:4.0.4")

    // email
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // test
    testImplementation("org.mockito:mockito-core:4.11.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")

    // https
    implementation("org.apache.httpcomponents.client5:httpclient5:5.4.1")
    implementation("org.apache.httpcomponents.core5:httpcore5:5.3")

    // ksp
    implementation("com.google.dagger:dagger-compiler:2.51.1")
    implementation(project(":processor"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.11")

    ksp(project(":processor"))

    // thumbnail
    implementation("net.coobird:thumbnailator:0.4.20")

    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
}

node {
    download = true
    version = "12.18.2"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

fun npm(): String {
    return if (System.getProperty("os.name").contains("Mac", ignoreCase = true)) {
        "npm"
    } else {
        "npm.cmd"
    }
}

fun registerAndStartCmdsAsync(vararg commands: String) {
    val combinedCommand = commands.joinToString(" && ")
    val isMac = System.getProperty("os.name").contains("Mac", ignoreCase = true)

    Thread {
        try {
            if (isMac) {

                Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", combinedCommand))
            } else {

                Runtime.getRuntime().exec("cmd /c start cmd /k \"$combinedCommand\"")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }.start()
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.register<Exec>("installFrontendAPI") {
    workingDir = file("frontend")
    commandLine(npm(), "install", "react")
    commandLine(npm(), "install", "axios")
    commandLine(npm(), "install", "bootstrap@4")
}

tasks.register("buildBackend") {
    dependsOn("bootJar")
}

tasks.register<Exec>("buildFrontend") {
    dependsOn("npmInstall")
    dependsOn("installFrontendAPI")
    workingDir = file("frontend")
    commandLine(npm(), "run", "build")
}

tasks.named("processResources") {
    dependsOn("buildFrontend")
    copy {
        from("frontend/build")
        into("src/main/resources/static")
    }
}


tasks.register("startFrontend") {
    doLast {
        registerAndStartCmdsAsync("cd frontend", "${npm()} run start")
    }
}

tasks.register("startBackend") {
    doLast {
        registerAndStartCmdsAsync("java -jar build/libs/project-tarannon-traderbird-${version}.jar")
    }
}

tasks.register("startApp") {
    dependsOn("bootRun")
} // This will not update the project when you made changes.

tasks.register("buildAll") {
    dependsOn("processResources", "buildFrontend")
    dependsOn("bootJar")
} // You should run this task to build the project.

// You would most likely want to run this task to test the project.
tasks.register("buildAllAndRun") {
    dependsOn("buildFrontend", "bootJar")
    dependsOn("bootRun")
} // You should run this task to test, to enable debugging, run this using debug mode.
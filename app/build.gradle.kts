plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    application
}

repositories {
    mavenCentral()
}

val ktor_version: String by project
val jackson_db_version: String by project
val jackson_kotlin_version: String by project

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.guava:guava:30.1.1-jre")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // db
    implementation("org.jetbrains.exposed:exposed-core:0.37.3")
    runtimeOnly("org.jetbrains.exposed:exposed-jdbc:0.37.3")

    // ktor api
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version") 

    // json
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_db_version")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson_kotlin_version")

    // logging
    implementation("org.slf4j:slf4j-api:2.0.0-alpha6")

}

application {
    mainClass.set("com.josavezaat.vmachine.EntryPoint")
}

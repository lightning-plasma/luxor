plugins {
    id("org.springframework.boot")
    kotlin("kapt")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":infra:persistence"))
    implementation(project(":infra:s3"))
    implementation(project(":infra:api"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.3")
    implementation("org.mapstruct:mapstruct:1.5.2.Final")
    implementation("org.mapstruct.extensions.spring:mapstruct-spring-annotations:0.1.2")
    kapt("org.mapstruct.extensions.spring:mapstruct-spring-extensions:0.1.2")
    kapt("org.mapstruct:mapstruct-processor:1.5.2.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "org.mockito")
    }
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    mainClass.set("com.archetype.luxor.LuxorApplicationKt")
}
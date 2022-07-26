dependencies {
    implementation(project(":domain"))

    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "org.mockito")
    }
}
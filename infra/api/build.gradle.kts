dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "org.mockito")
    }
}

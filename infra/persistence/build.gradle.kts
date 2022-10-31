dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.3")

    // TODO Boot 2.7 になると org.postgresqlに書き方が変わる (そのままだとConnectionを確立できないので書き方自体考える)
    runtimeOnly("io.r2dbc:r2dbc-postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "org.mockito")
    }
}
dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(platform("software.amazon.awssdk:bom:2.20.7"))
    implementation("software.amazon.awssdk:s3")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "org.mockito")
    }
}

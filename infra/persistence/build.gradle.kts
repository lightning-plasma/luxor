dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.2.0")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.10")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "org.mockito")
    }
}
dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("pro.chenggang:reactive-mybatis-support:1.2.5.RELEASE")
    implementation("pro.chenggang:mybatis-r2dbc-spring:1.2.5.RELEASE")

    runtimeOnly("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "org.mockito")
    }
}
dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.4.0-b180830.0438")


//    implementation("com.sun.xml.bind:jaxb-impl:2.4.0-b180830.0438")
//    implementation("com.sun.xml.bind:2.3.0.1")

    // implementation("org.glassfish.jaxb:jaxb-runtime:2.4.0-b180830.0438")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "org.mockito")
    }
}

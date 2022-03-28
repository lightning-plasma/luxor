import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.5" apply false
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("java")
	kotlin("jvm") version "1.6.10" apply false
	kotlin("plugin.spring") version "1.6.10" apply false
}

repositories {
	mavenCentral()
}

allprojects {
	group = "com.archetype"
	version = "0.0.1-SNAPSHOT"
}

subprojects {
	apply {
		plugin("org.jetbrains.kotlin.jvm")
		plugin("org.jetbrains.kotlin.plugin.spring")
	}

	repositories {
		mavenCentral()
	}

	java.sourceCompatibility = JavaVersion.VERSION_11
	java.targetCompatibility = JavaVersion.VERSION_11

	dependencies {
		// https://docs.gradle.org/current/userguide/platforms.html
		implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))

		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
		implementation("com.google.guava:guava:31.0.1-jre")

		// for blockhound
		implementation("io.projectreactor.tools:blockhound:1.0.6.RELEASE")
		runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-debug")

		testImplementation("io.mockk:mockk:1.12.2")
	}

	// https://github.com/gradle/kotlin-dsl-samples/blob/master/samples/multi-kotlin-project-config-injection/build.gradle.kts
	tasks.withType<KotlinCompile>().configureEach {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "11"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

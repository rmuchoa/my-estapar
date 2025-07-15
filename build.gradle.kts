plugins {
	kotlin("jvm") version "2.1.21"
	kotlin("plugin.jpa") version "2.1.21"
	kotlin("plugin.spring") version "2.1.21"
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(platform("org.springframework.boot:spring-boot-dependencies:3.5.3"))

	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")

	implementation("io.projectreactor:reactor-core")

	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage")
	}
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
	testImplementation("org.hamcrest:hamcrest:2.2")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.test {
	useJUnitPlatform()
}
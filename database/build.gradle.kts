import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    maven(url = "https://repo.spring.io/libs-milestone")
    maven(url = "https://repo.spring.io/libs-snapshot")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:2.1.6.RELEASE") {
            // See: https://youtrack.jetbrains.com/issue/KT-27994#focus=streamItem-27-3148043-0-0
            bomProperty("kotlin.version", "1.3.61")
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.data:spring-data-redis:2.2.0.M2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.vladmihalcea:hibernate-types-52:2.4.4")
    implementation("io.lettuce:lettuce-core:5.1.7.RELEASE")

    compile(project(":parser")) {
        exclude(module = "org.jetbrains.kotlin")
    }

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

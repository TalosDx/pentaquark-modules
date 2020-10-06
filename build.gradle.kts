val kotlinVersion = "1.3.72"
val ktorVersion = "1.3.2"

val slf4jVersion = "1.7.30"
val kotestVersion = "4.0.6"

plugins {
    java
    kotlin("jvm") version "1.3.72"
    jacoco
    id("com.github.kt3k.coveralls") version "2.10.2"
}

group = "dev.talosdx"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = sourceCompatibility
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

tasks {
    val developmentOnly = configurations.create("developmentOnly")
    configurations.runtimeClasspath.get().extendsFrom(developmentOnly)

    compileKotlin {
        kotlinOptions.jvmTarget = sourceCompatibility
        kotlinOptions.javaParameters = true
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = targetCompatibility
        kotlinOptions.javaParameters = true
    }

    withType<Test> {
        classpath += developmentOnly
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    withType<JavaExec> {
        classpath += developmentOnly
    }

    jacocoTestReport {
        dependsOn(test) // tests are required to run before generating the report

        reports {
            xml.isEnabled = true // coveralls plugin depends on xml format report
            html.isEnabled = true
        }
    }
}

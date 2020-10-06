val kotlinVersion = "1.3.72"
val ktorVersion = "1.3.2"

val slf4jVersion = "1.7.30"
val kotestVersion = "4.0.6"

plugins {
    java
    kotlin("jvm") version "1.3.72"
    jacoco
    id("com.github.kt3k.coveralls") version "2.10.2"
    `maven-publish`
    signing
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

    javadoc {
        if (JavaVersion.current().isJava9Compatible) {
            (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
        }
    }

    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    val javadocJar by creating(Jar::class) {
        dependsOn.add(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc)
    }

    artifacts {
        archives(sourcesJar)
        archives(javadocJar)
        archives(jar)
    }


    jacocoTestReport {
        dependsOn(test) // tests are required to run before generating the report

        reports {
            xml.isEnabled = true // coveralls plugin depends on xml format report
            html.isEnabled = true
        }
    }
}




publishing {
    repositories {
        maven {
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

            credentials {
                username = project.findProperty("sonatype.username").toString()
                password = project.findProperty("sonatype.password").toString()
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()

            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name.set("Penta Quark Modules")
                description.set("Allows Ktor modules to register automatically")
                url.set("https://github.com/TalosDx/pentaquark-modules")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("talosdx")
                        name.set("Ushakov Roman Alexsandrovich")
                        email.set("talosdx@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/TalosDx/pentaquark-modules.git")
                    developerConnection.set("scm:git:git@github.com:TalosDx/pentaquark-modules.git")
                    url.set("https://github.com/TalosDx/pentaquark-modules")
                }
            }
        }
    }
}
signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}
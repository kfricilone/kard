import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    base
    alias(libs.plugins.dependencies)

    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.kotlinter)
    `maven-publish`
    signing
}

val jvmVersion = JavaVersion.VERSION_21

group = "me.kfricilone"
version = "1.0.0-SNAPSHOT"
description = "A Kotlin/JS library for generating github like repository cards."

tasks.withType<Wrapper> {
    gradleVersion = libs.versions.gradle.get()
}

val rejectVersionRegex = Regex("(?i)[._-](?:alpha|beta|rc|cr|m|dev)")
tasks.withType<DependencyUpdatesTask> {
    gradleReleaseChannel = "current"
    revision = "release"

    rejectVersionIf {
        candidate.version.contains(rejectVersionRegex)
    }
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    jvm {
        jvmToolchain(jvmVersion.majorVersion.toInt())
    }

    js {

        binaries.executable()

        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }

    sourceSets {
        val jsMain by getting {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation(libs.kotlin.react)
                implementation(libs.kotlin.reactdom)
                implementation(libs.kotlin.styled)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.html)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

val copyJs = tasks.register<Copy>("copyJsResources") {
    val distTask = tasks.getByName("jsBrowserDistribution")
    val webpackTask = tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack")

    dependsOn(distTask)

    from({
        webpackTask.outputDirectory.file(webpackTask.mainOutputFileName)
    })

    into({
        layout.buildDirectory.file(
            "processedResources/jvm/main/META-INF/resources/webjars/${rootProject.name}/${project.version}/${webpackTask.mainOutputFileName.get()}"
        )
    })
}

tasks.named("jvmProcessResources") {
    dependsOn(copyJs)
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            url.set("https://github.com/kfricilone/${rootProject.name}")
            inceptionYear.set("2021")

            licenses {
                license {
                    name.set("ISC License")
                    url.set("https://opensource.org/licenses/isc-license.txt")
                }
            }

            developers {
                developer {
                    name.set("Kyle Fricilone")
                    url.set("https://kfricilone.me")
                }
            }

            scm {
                connection.set("scm:git:https://github.com/kfricilone/${rootProject.name}")
                developerConnection.set("scm:git:git@github.com:kfricilone/${rootProject.name}.git")
                url.set("https://github.com/kfricilone/${rootProject.name}")
            }

            issueManagement {
                system.set("GitHub")
                url.set("https://github.com/kfricilone/${rootProject.name}/issues")
            }

            ciManagement {
                system.set("GitHub")
                url.set("https://github.com/kfricilone/${rootProject.name}/actions?query=workflow%3Aci")
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

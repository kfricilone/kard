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

val jvmVersion = JavaVersion.VERSION_11.toString()
val commonArgs = listOf(
    "-opt-in=kotlin.contracts.ExperimentalContracts",
    "-opt-in=kotlin.time.ExperimentalTime",
    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-Xinline-classes",
    "-Xallow-result-return-type"
)
val jvmArgs = commonArgs + listOf(
        "-Xjsr305=strict"
)
val jsArgs = commonArgs + listOf(
    "-opt-in=kotlin.js.ExperimentalJsExport",
    "-opt-in=kotlinx.coroutines.DelicateCoroutinesApi"
)

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
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

kotlin {
    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = jvmVersion
                freeCompilerArgs = jvmArgs
            }
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }

    js(LEGACY) {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs = jsArgs
            }
        }

        binaries.executable()

        browser {
            dceTask {
                keep("kard.buildGhCards", "kard.switchGhTheme")
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js", libs.versions.kotlin.get()))
                implementation(libs.bundles.js)
                implementation(libs.bundles.common)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js", libs.versions.kotlin.get()))
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

val move by tasks.registering {
    val webpackTask = tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack")
    doLast {
        File(webpackTask.destinationDirectory, webpackTask.outputFileName).copyTo(
            File(
                buildDir,
                "processedResources/jvm/main/META-INF/resources/webjars/${rootProject.name}/${project.version}/${webpackTask.outputFileName}"
            ),
            true
        )
    }
}

tasks.named("jvmProcessResources") {
    val webpackTask = tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack")
    dependsOn(webpackTask)
    finalizedBy(move)
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

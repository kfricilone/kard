import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    base
    alias(libs.plugins.dependencies)

    id("library-conventions")
}

tasks.withType<Wrapper> {
    gradleVersion = libs.versions.gradle.get()
    distributionType = Wrapper.DistributionType.ALL
}

tasks.withType<DependencyUpdatesTask> {
    gradleReleaseChannel = "current"
    rejectVersionIf {
        listOf("alpha", "beta", "rc", "cr", "m", "eap", "pr", "dev").any {
            candidate.version.contains(it, ignoreCase = true)
        }
    }
}

kotlin {
    sourceSets {
        jsMain {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
            dependencies {
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

        jsTest {
            dependencies {
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
            "processedResources/jvm/main/META-INF/resources/webjars/${rootProject.name}/${project.version}"
        )
    })
}

tasks.named("jvmProcessResources") {
    dependsOn(copyJs)
}

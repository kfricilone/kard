import org.gradle.api.JavaVersion

/**
 * Created by Kyle Fricilone on Oct 29, 2020.
 */
object Props {
    const val group = "me.kfricilone"
    const val version = "1.0.0-SNAPSHOT"

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
}

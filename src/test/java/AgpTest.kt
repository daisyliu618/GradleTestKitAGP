import org.gradle.testkit.runner.GradleRunner
import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class AgpTest {

    @get:Rule
    val rule = TemporaryFolder()

    @Before
    fun setup() {
        rule.newFile("settings.gradle").writeText(SETTINGS_GRADLE)
        rule.newFile("gradle.properties").writeText(GRADLE_PROPERTIES)
        rule.root.apply { File(this, "src/main").mkdirs() }
        rule.newFile("src/main/AndroidManifest.xml").writeText(MANIFEST)
    }

    @Test
    fun build_agp_7_3_0_alpha03() {
        rule.newFile("build.gradle").writeText(createBuildGradle(agpVersion = "7.3.0-alpha03"))
        runGradleBuild()
    }

    @Test
    fun build_agp_7_3_0_alpha04() {
        rule.newFile("build.gradle").writeText(createBuildGradle(agpVersion = "7.3.0-alpha04"))
        runGradleBuild()
    }
    
//     @Test
//     fun component_publish_agp_7_3_0_alpha04() {
//         rule.newFile("build.gradle").writeText(createBuildGradle(agpVersion = "7.3.0-alpha04"))
//         runGradleBuild()
//         val result = buildWithGradleWrapper(productTemporaryFolder, ":printComponents", "-is")
//         assertTrue(result.output.contains("COMPONENT:"))
//      }

    private fun runGradleBuild() {
        GradleRunner.create().withProjectDir(rule.root)
                .withArguments("assembleDebug", "assembleDebugAndroidTest", "--info")
                .withDebug(true)
                .build()
    }
}

@Language("gradle")
private fun createBuildGradle(agpVersion: String) = """
    plugins {
        id 'com.android.application' version '${agpVersion}'
    }

    android {
        namespace = 'com.example.agp'
        compileSdkVersion 32
        buildToolsVersion "32.0.0"

        defaultConfig {
            versionCode 1
        }
    }

    dependencies {
        implementation 'androidx.annotation:annotation:1.1.0'
    }
    """.trimIndent()

@Language("xml")
const val MANIFEST = """
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"/>
"""

@Language("properties")
const val GRADLE_PROPERTIES = """
    android.useAndroidX=true
"""

@Language("gradle")
const val SETTINGS_GRADLE = """
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
"""


// @Language("groovy")
// val buildGradleFile = """
//         task printComponents {
//            doLast {
//               components.all { c ->
//                     project.logger.warn("COMPONENT: " + c)
//               }
//            }

//         }

// """.trimIndent()

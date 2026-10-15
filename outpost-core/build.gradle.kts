import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.serialization)
}

compose.resources {
    packageOfResClass = "dev.appoutlet.outpost.generated.resources"
}

kotlin {
    iosArm64()
    iosSimulatorArm64()

    jvm()

    android {
        namespace = "dev.appoutlet.outpost"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.foundation)
            api(libs.compose.material3)
            implementation(libs.compose.components.resources)
            api(libs.compose.runtime)
            api(libs.compose.ui)
            api(libs.compose.uiToolingPreview)
            implementation(libs.kermit)
            implementation(libs.kermit.koin)
            api(libs.material.kolor)
            implementation(libs.orbit.compose)
            implementation(libs.orbit.core)
            implementation(libs.orbit.viewModel)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.navigation3)
            implementation(libs.lifecycle.viewmodel.navigation3)
            implementation(libs.material3.adaptive.navigation3)
            implementation(libs.serialization.json)
            implementation(libs.navigation3.ui)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.compose.ui.test)
            implementation(libs.kotest.assertions)
            implementation(libs.orbit.test)
        }

        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        all {
            languageSettings.optIn("androidx.compose.ui.test.ExperimentalTestApi")
            languageSettings.optIn("org.koin.core.annotation.KoinExperimentalAPI")
            languageSettings.optIn("androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi")
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
    detektPlugins(libs.detekt.formatting)
}

buildkonfig {
    packageName = "dev.appoutlet.outpost"
    exposeObjectWithName.set("BuildKonfig")

    defaultConfigs {
        buildConfigField(FieldSpec.Type.BOOLEAN, "isDebug", "true")
    }

    defaultConfigs("release") {
        buildConfigField(FieldSpec.Type.BOOLEAN, "isDebug", "false")
    }
}

detekt {
    autoCorrect = true
    parallel = true
    buildUponDefaultConfig = true
    config.from(files("$rootDir/detekt.yml"))
    source.setFrom(
        "src/commonMain/kotlin",
        "src/commonTest/kotlin",
        "src/androidMain/kotlin",
        "src/iosMain/kotlin",
        "src/desktopMain/kotlin",
    )
}
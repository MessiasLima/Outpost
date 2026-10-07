import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
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
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
        }

        commonMain.dependencies {
            api(libs.compose.foundation)
            api(libs.compose.material3)
            api(libs.compose.components.resources)
            api(libs.compose.runtime)
            api(libs.compose.ui)
            api(libs.compose.uiToolingPreview)
            implementation(libs.material.kolor)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.navigation3)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.navigation3.ui)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.compose.ui.test)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        all {
            languageSettings.optIn("androidx.compose.ui.test.ExperimentalTestApi")
            languageSettings.optIn("org.koin.core.annotation.KoinExperimentalAPI")
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
    detektPlugins(libs.detekt.formatting)
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
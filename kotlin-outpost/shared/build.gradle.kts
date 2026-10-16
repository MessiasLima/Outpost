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

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "KotlinOutpostShared"
            isStatic = true
        }
    }

    jvm()

    android {
        namespace = "dev.appoutlet.outpost.kotlin"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.outpostCore)
            implementation(libs.compose.components.resources)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.compose.ui.test)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        iosMain.dependencies {
            implementation(libs.compose.ui)
        }

        all {
            languageSettings.optIn("androidx.compose.ui.test.ExperimentalTestApi")
        }
    }
}

dependencies {
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

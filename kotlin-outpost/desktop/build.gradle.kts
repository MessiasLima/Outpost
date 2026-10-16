import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
}

dependencies {
    implementation(projects.kotlinOutpost.shared)
    implementation(compose.desktop.currentOs)
    implementation(libs.coroutines.swing)
    implementation(libs.compose.ui)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.compose.components.resources)
    detektPlugins(libs.detekt.formatting)
}

compose.desktop {
    application {
        mainClass = "dev.appoutlet.outpost.kotlin.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.appoutlet.outpost.kotlin.desktop"
            packageVersion = libs.versions.outpost.name.get()

            macOS {
                iconFile.set(project.file("icon/icon.icns"))
            }

            windows {
                iconFile.set(project.file("icon/icon.ico"))
                upgradeUuid = libs.versions.outpost.uuid.get()
            }

            linux {
                iconFile.set(project.file("icon/icon.png"))
            }
        }
    }
}

detekt {
    autoCorrect = true
    parallel = true
    buildUponDefaultConfig = true
    config.from(files("$rootDir/detekt.yml"))
}
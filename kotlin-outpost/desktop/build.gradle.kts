import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.detekt)
}

dependencies {
    implementation(projects.kotlinOutpost.shared)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
    detektPlugins(libs.detekt.formatting)
}

detekt {
    autoCorrect = true
    config.from(files("$rootDir/detekt.yml"))
}

compose.desktop {
    application {
        mainClass = "dev.appoutlet.outpost.kotlin.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.appoutlet.outpost.kotlin.desktop"
            packageVersion = "1.0.0"
        }
    }
}

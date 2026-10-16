import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
}

dependencies {
    implementation(projects.{{CAMEL}}.shared)
    implementation(compose.desktop.currentOs)
    implementation(libs.coroutines.swing)
    implementation(libs.compose.uiToolingPreview)
    detektPlugins(libs.detekt.formatting)
}

compose.desktop {
    application {
        mainClass = "dev.appoutlet.outpost.{{PKG}}.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.appoutlet.outpost.{{PKG}}.desktop"
            packageVersion = "1.0.0"

            // Brand assets: add the macOS/windows/linux iconFile entries once the
            // variant has its own icons.
        }
    }
}

detekt {
    autoCorrect = true
    parallel = true
    buildUponDefaultConfig = true
    config.from(files("$rootDir/detekt.yml"))
}

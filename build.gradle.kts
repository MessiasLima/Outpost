plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

allprojects {
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        exclude { it.file.absolutePath.contains("${File.separator}build${File.separator}") }
        exclude { it.file.absolutePath.contains("${File.separator}.gradle${File.separator}") }
    }
}

tasks.register("detekt") {
    dependsOn(subprojects.map { it.tasks.withType<io.gitlab.arturbosch.detekt.Detekt>() })
}

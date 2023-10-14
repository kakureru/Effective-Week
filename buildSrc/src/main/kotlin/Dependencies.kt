import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {

    const val core = "androidx.core:core-ktx:1.8.0"
    const val appcompat = "androidx.appcompat:appcompat:1.5.1"
    const val fragment = "androidx.fragment:fragment-ktx:1.6.0"
    const val activityCompose = "androidx.activity:activity-compose:1.5.1"
    const val navigationCompose = "androidx.navigation:navigation-compose:2.6.0"
    const val constraintlayoutCompose = "androidx.constraintlayout:constraintlayout-compose:1.0.1"

    object Compose {
        const val bom = "androidx.compose:compose-bom:2023.10.00"
        const val runtime = "androidx.compose.runtime:runtime"
        const val foundation = "androidx.compose.foundation:foundation:1.6.0-alpha04"
        const val foundationLayout = "androidx.compose.foundation:foundation-layout"
        const val material3 = "androidx.compose.material3:material3-android:1.2.0-alpha09"
        const val ui = "androidx.compose.ui:ui"
        const val uiGraphics = "androidx.compose.ui:ui-graphics"
        const val uiTooling = "androidx.compose.ui:ui-tooling"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    }

    object Koin {
        const val android = "io.insert-koin:koin-android:${Versions.koin}"
        const val compose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${Versions.room}"
        const val ktx = "androidx.room:room-ktx:${Versions.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.room}"
    }
}

fun DependencyHandler.room() {
    implementation(Dependencies.Room.runtime)
    implementation(Dependencies.Room.ktx)
    ksp(Dependencies.Room.compiler)
}

fun DependencyHandler.koin() {
    implementation(Dependencies.Koin.android)
    implementation(Dependencies.Koin.compose)
}

fun DependencyHandler.compose() {
    implementation(Dependencies.activityCompose)
    implementation(Dependencies.constraintlayoutCompose)
    implementation(Dependencies.navigationCompose)

    implementation(Dependencies.Compose.runtime)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiGraphics)
    debugImplementation(Dependencies.Compose.uiTooling)
    implementation(Dependencies.Compose.uiToolingPreview)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.foundationLayout)
    implementation(Dependencies.Compose.material3)
    implementation(platform(Dependencies.Compose.bom))
}
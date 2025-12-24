import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
    alias(libs.plugins.openapiGenerator)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(
                layout.buildDirectory.dir(
                    "generated/openapi/src/main/kotlin"
                )
            )
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.bundles.androidx.lifecycle)

            implementation(libs.compose.ui.backhandler)

            implementation(libs.ktor.client.cio)

            implementation(libs.bundles.koin.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            /*implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)*/

            implementation(libs.material3.adaptive)
            implementation(libs.material3.adaptive.layout)
            //implementation(libs.compose.ui.backhandler)
            implementation(libs.kotlinx.serialization.json)
            //implementation(libs.coil.compose)
            implementation(libs.jetbrains.compose.navigation)
            api(libs.ktor.serialization.kotlinx.json)
            implementation(libs.bundles.compose.ui)
            implementation(libs.bundles.koin.common)
            //implementation(libs.bundles.androidx.lifecycle)
            implementation(libs.napier)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.towhid.memeic"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.towhid.memeic"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

// OpenAPI Kotlin Multiplatform generation task
tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("buildKotlinClient") {
    // The generator type
    generatorName.set("kotlin")

    // Path to your YAML file
    inputSpec.set("$projectDir/src/memeic.yml")

    // Where the generated code will go
    outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.absolutePath)


    // The Kotlin package for the generated code
    packageName.set("com.towhid.memeic.api")

    // Configuration options for the generator
    configOptions.set(
        mapOf(
            "library" to "multiplatform",             // KMM client
            //"serializationLibrary" to "kotlinx_serialization",      // Use kotlinx.serialization
            "dateLibrary" to "string",                // Dates as string
            "useTags" to "true",                      // Generate separate API classes for tags
            "generateOneOfAnyOfWrappers" to "false",   // Optional: handle oneOf/anyOf
            "ensureUniqueParams" to "true" ,           // Avoid duplicate parameters
            "useResponseWrappers" to "false"
        )
    )

    // Global properties (optional)
    globalProperties.set(
        mapOf(
            "modelDocs" to "false",  // Disable model docs if you want
            "apiDocs" to "false"
        )
    )

    // Clean output folder before generation (optional)
    doFirst {
        delete(file(outputDir.get()))
    }
}

/*// Ensure Kotlin compile depends on OpenAPI generation
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    dependsOn(tasks.named("buildKotlinClient"))
}*/

tasks.withType<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>().configureEach {
    notCompatibleWithConfigurationCache("OpenAPI Generator Gradle plugin is not configuration-cache compatible")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    dependsOn(
        tasks.matching {
            it.name.contains("generateComposeResources", ignoreCase = true)
        }
    )
}




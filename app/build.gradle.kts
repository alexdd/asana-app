plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.asana.timer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.asana.timer"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// Task zum Kopieren der APK nach releases/ und Hinzufügen zu Git
tasks.register("copyApkToReleases") {
    group = "release"
    description = "Kopiert die gebaute APK nach releases/ und fügt sie zu Git hinzu"
    
    doLast {
        val releasesDir = rootProject.file("releases")
        releasesDir.mkdirs()
        
        // Finde die gebaute APK (Release oder Debug) - neueste zuerst
        val apkFiles = fileTree("build/outputs/apk") {
            include("**/*.apk")
        }.sortedByDescending { it.lastModified() }
        
        if (apkFiles.isEmpty()) {
            throw GradleException("Keine APK-Datei gefunden. Bitte zuerst 'assembleRelease' oder 'assembleDebug' ausführen.")
        }
        
        // Nimm nur die neueste APK (normalerweise die gerade gebaute)
        val apkFile = apkFiles.first()
        val versionName = android.defaultConfig.versionName
        val buildType = apkFile.parentFile.name // debug oder release
        val targetFileName = "app-${buildType}-v${versionName}.apk"
        val targetFile = File(releasesDir, targetFileName)
        
        println("Kopiere ${apkFile.name} nach releases/${targetFileName}")
        apkFile.copyTo(targetFile, overwrite = true)
        
        // Füge zu Git hinzu
        try {
            val gitAddProcess = ProcessBuilder("git", "add", targetFile.absolutePath)
                .directory(rootProject.projectDir)
                .start()
            gitAddProcess.waitFor()
            
            if (gitAddProcess.exitValue() == 0) {
                println("✓ APK zu Git hinzugefügt: releases/${targetFileName}")
            } else {
                println("⚠ Warnung: Git add fehlgeschlagen (möglicherweise nicht in einem Git-Repository)")
            }
        } catch (e: Exception) {
            println("⚠ Warnung: Git add fehlgeschlagen: ${e.message}")
        }
        
        println("\n✓ APK erfolgreich nach releases/ kopiert und zu Git hinzugefügt")
        println("  Führe 'git commit' und 'git push' aus, um das Release zu erstellen")
    }
}

// Automatisch nach assembleRelease/assembleDebug ausführen
afterEvaluate {
    tasks.named("assembleRelease") {
        finalizedBy("copyApkToReleases")
    }
    tasks.named("assembleDebug") {
        finalizedBy("copyApkToReleases")
    }
}


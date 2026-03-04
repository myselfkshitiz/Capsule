import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val localProperties = Properties().apply {
    val locFile = rootProject.file("local.properties")
    if (locFile.exists()) locFile.inputStream().use { load(it) }
}

android {
    namespace = "com.kshitiz.capsule"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kshitiz.capsule"
        minSdk = 23
        targetSdk = 36

        val major = 2
        val minor = 0
        val patch = 2

        val timestamp = SimpleDateFormat("yyyyMMddHHmm").apply {
            timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        }.format(Date())

        versionName = "${major}.${minor}.${patch}-${timestamp}"
        versionCode = (System.currentTimeMillis() / 60000).toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("voyager-release.jks")
            storePassword = localProperties.getProperty("RELEASE_STORE_PASSWORD") ?: System.getenv("RELEASE_STORE_PASSWORD") ?: "KshitizKumar2020"
            keyAlias = localProperties.getProperty("RELEASE_KEY_ALIAS") ?: System.getenv("RELEASE_KEY_ALIAS") ?: "voyager-key"
            keyPassword = localProperties.getProperty("RELEASE_KEY_PASSWORD") ?: System.getenv("RELEASE_KEY_PASSWORD") ?: "KshitizKumar2020"
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "Capsule - Debug")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            resValue("string", "app_name", "Capsule")
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a")
            isUniversalApk = false
        }
    }

    applicationVariants.all {
        outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            val abi = output.filters.find { it.filterType == "ABI" }?.identifier
            if (abi != null) {
                outputFileName = "Capsule-v${defaultConfig.versionName}-$abi.apk"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget("17"))
        }
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes.add("**/*.kotlin_builtins")
            excludes.add("META-INF/LICENSE*")
            excludes.add("META-INF/NOTICE*")
            excludes.add("META-INF/*.version")
            excludes.add("DebugProbesKt.bin")
            excludes.add("kotlin-tooling-metadata.json")
            excludes.add("META-INF/androidx/**/LICENSE.txt")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
}

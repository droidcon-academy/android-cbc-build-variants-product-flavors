@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.ksp)
    // Add the Google services Gradle plugin
    //id("com.google.gms.google-services")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.droidcon.wealthbuddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.droidcon.wealthbuddy"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.droidcon.wealthbuddy.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Enable room auto-migrations
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    signingConfigs {
        create("carbuddyRelease") {
            storeFile = file(project.property("CARBUDDY_RELEASE_KEYSTORE_FILE").toString())
            storePassword = project.property("CARBUDDY_RELEASE_STORE_PASSWORD").toString()
            keyAlias = project.property("CARBUDDY_RELEASE_KEY_ALIAS").toString()
            keyPassword = project.property("CARBUDDY_RELEASE_KEY_PASSWORD").toString()
        }
        create("investbuddyRelease") {
            storeFile = file(project.property("INVESTBUDDY_RELEASE_KEYSTORE_FILE").toString())
            storePassword = project.property("INVESTBUDDY_RELEASE_STORE_PASSWORD").toString()
            keyAlias = project.property("INVESTBUDDY_RELEASE_KEY_ALIAS").toString()
            keyPassword = project.property("INVESTBUDDY_RELEASE_KEY_PASSWORD").toString()
        }
        create("sipcalcRelease") {
            storeFile = file(project.property("SIPCALC_RELEASE_KEYSTORE_FILE").toString())
            storePassword = project.property("SIPCALC_RELEASE_STORE_PASSWORD").toString()
            keyAlias = project.property("SIPCALC_RELEASE_KEY_ALIAS").toString()
            keyPassword = project.property("SIPCALC_RELEASE_KEY_PASSWORD").toString()
        }
    }

    flavorDimensions += listOf("customer", "version")
    productFlavors {
        create("trial") {
            dimension = "version"
            applicationIdSuffix = ".trial"
            versionNameSuffix = "-trial"
            buildConfigField("boolean", "isPremium", "false")
            buildConfigField("int", "HISTORY_COUNT", "3")
        }
        create("premium") {
            dimension = "version"
            applicationIdSuffix = ".premium"
            versionNameSuffix = ""
            buildConfigField("boolean", "isPremium", "true")
            buildConfigField("int", "HISTORY_COUNT", "10")
        }
        create("carbuddy") {
            dimension = "customer"
            applicationId = "com.droidcon.wealthbuddy.carbuddy"
            buildConfigField("boolean", "isInvestment", "false")
            buildConfigField(
                "String",
                "connect_url",
                "\"https://www.w3schools.com/sql/default.asp\""
            )
            buildConfigField("String", "apply_url", "\"https://academy.droidcon.com/\"")
        }
        create("sipcalculator") {
            dimension = "customer"
            applicationId = "com.droidcon.wealthbuddy.sipcalculator"
            buildConfigField("boolean", "isInvestment", "true")
            buildConfigField(
                "String",
                "connect_url",
                "\"https://developer.android.com/build/build-variants\""
            )
            buildConfigField("String", "apply_url", "\"https://play.kotlinlang.org/\"")
        }
        create("investbuddy") {
            dimension = "customer"
            applicationId = "com.droidcon.wealthbuddy.investbuddy"
            buildConfigField("boolean", "isInvestment", "true")
            buildConfigField(
                "String",
                "connect_url",
                "\"https://developer.android.com/build/build-variants\""
            )
            buildConfigField("String", "apply_url", "\"https://play.kotlinlang.org/\"")
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            productFlavors.getByName("carbuddy").signingConfig =
                signingConfigs.getByName("carbuddyRelease")
            productFlavors.getByName("investbuddy").signingConfig =
                signingConfigs.getByName("investbuddyRelease")
            productFlavors.getByName("sipcalculator").signingConfig =
                signingConfigs.getByName("sipcalcRelease")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    kotlinOptions {
        jvmTarget = "18"
    }

    buildFeatures {
        compose = true
        aidl = false
        renderScript = false
        shaders = false
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

androidComponents {
    beforeVariants { applicationVariantBuilder ->
        if (applicationVariantBuilder.productFlavors
                .containsAll(
                    listOf("customer" to "sipcalculator", "version" to "trial")
                )
        ) {
            applicationVariantBuilder.enable = false
        }
    }
}

dependencies {

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // Hilt and instrumented tests.
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    // Hilt and Robolectric tests.
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler)

    // Arch Components
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // Tooling
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Instrumented tests
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Firebase
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // Local tests: jUnit, coroutines, Android runner
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // Instrumented tests: jUnit rules and runners

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
}

import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}
val properties = Properties().apply {
    load(rootProject.file("local.properties").reader())
}

fun hasSigningVars(): Boolean {
    return providers.environmentVariable("SIGNING_KEY_ALIAS").orNull != null
        && providers.environmentVariable("SIGNING_KEY_PASSWORD").orNull != null
        && providers.environmentVariable("SIGNING_STORE_FILE").orNull != null
        && providers.environmentVariable("SIGNING_STORE_PASSWORD").orNull != null
}

android {
    compileSdk = project.libs.versions.app.build.compileSDKVersion.get().toInt()

    defaultConfig {
        applicationId = libs.versions.app.version.appId.get()
        minSdk = project.libs.versions.app.build.minimumSDK.get().toInt()
        targetSdk = project.libs.versions.app.build.targetSDK.get().toInt()
        versionCode = project.libs.versions.app.version.versionCode.get().toInt()
        versionName = project.libs.versions.app.version.versionName.get()
        archivesName.set("calendar-$versionCode")
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        buildConfigField("String", "RIGHT_APP_KEY", "\"${properties["RIGHT_APP_KEY"]}\"")
        buildConfigField("String", "PRODUCT_ID_X1", "\"${properties["PRODUCT_ID_X1"]}\"")
        buildConfigField("String", "PRODUCT_ID_X2", "\"${properties["PRODUCT_ID_X2"]}\"")
        buildConfigField("String", "PRODUCT_ID_X3", "\"${properties["PRODUCT_ID_X3"]}\"")
        buildConfigField("String", "SUBSCRIPTION_ID_X1", "\"${properties["SUBSCRIPTION_ID_X1"]}\"")
        buildConfigField("String", "SUBSCRIPTION_ID_X2", "\"${properties["SUBSCRIPTION_ID_X2"]}\"")
        buildConfigField("String", "SUBSCRIPTION_ID_X3", "\"${properties["SUBSCRIPTION_ID_X3"]}\"")
        buildConfigField("String", "SUBSCRIPTION_YEAR_ID_X1", "\"${properties["SUBSCRIPTION_YEAR_ID_X1"]}\"")
        buildConfigField("String", "SUBSCRIPTION_YEAR_ID_X2", "\"${properties["SUBSCRIPTION_YEAR_ID_X2"]}\"")
        buildConfigField("String", "SUBSCRIPTION_YEAR_ID_X3", "\"${properties["SUBSCRIPTION_YEAR_ID_X3"]}\"")
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    signingConfigs {
        if (keystorePropertiesFile.exists()) {
            register("release") {
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
                storeFile = file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
            }
        } else if (hasSigningVars()) {
            register("release") {
                keyAlias = providers.environmentVariable("SIGNING_KEY_ALIAS").get()
                keyPassword = providers.environmentVariable("SIGNING_KEY_PASSWORD").get()
                storeFile = file(providers.environmentVariable("SIGNING_STORE_FILE").get())
                storePassword = providers.environmentVariable("SIGNING_STORE_PASSWORD").get()
            }
        } else {
            logger.warn("Warning: No signing config found. Build will be unsigned.")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (keystorePropertiesFile.exists() || hasSigningVars()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    flavorDimensions.add("variants")
    productFlavors {
        register("core")
        register("foss")
        register("gplay")
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    compileOptions {
        val currentJavaVersionFromLibs = JavaVersion.valueOf(libs.versions.app.build.javaVersion.get().toString())
        sourceCompatibility = currentJavaVersionFromLibs
        targetCompatibility = currentJavaVersionFromLibs
    }

    dependenciesInfo {
        includeInApk = false
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = project.libs.versions.app.build.kotlinJVMTarget.get()
    }

    namespace = libs.versions.app.version.appId.get()

    lint {
        checkReleaseBuilds = false
        abortOnError = true
        warningsAsErrors = true
        baseline = file("lint-baseline.xml")
    }

    bundle {
        language {
            enableSplit = false
        }
    }
}

detekt {
    baseline = file("detekt-baseline.xml")
}

dependencies {
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.print)
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    //Goodwy
    implementation(libs.goodwy.commons)
    implementation(libs.rx.animation)
}

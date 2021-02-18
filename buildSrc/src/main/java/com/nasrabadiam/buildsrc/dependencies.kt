package com.nasrabadiam.buildsrc

object Versions {
    const val ktlint = "0.39.0"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.0-alpha04"
    const val jdkDesugar = "com.android.tools:desugar_jdk_libs:1.0.9"

    const val junit = "junit:junit:4.13"

    const val material = "com.google.android.material:material:1.1.0"

    object Kotlin {
        private const val version = "1.4.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Di {
        private const val version = "2.28-alpha"
        private const val androidXVersion = "1.0.0-alpha01"
        const val hilt = "com.google.dagger:hilt-android:$version"
        const val lifecycle = "androidx.hilt:hilt-lifecycle-viewmodel:$androidXVersion"

        const val daggerHiltCompiler = "com.google.dagger:hilt-android-compiler:$version"
        const val hiltCompiler = "androidx.hilt:hilt-compiler:$androidXVersion"

        const val daggerPlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
    }

    object Coroutines {
        private const val version = "1.4.2"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Accompanist {
        private const val version = "0.4.2"
        const val insets = "dev.chrisbanes.accompanist:accompanist-insets:$version"
    }

    object Glide {
        private const val version = "4.11.0"
        const val core = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Retrofit {
        const val retrofit = "com.squareup.retrofit2:retrofit:2.7.2"
        const val gson = "com.google.code.gson:gson:2.8.5"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:2.5.0"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:3.12.0"

        object XmlConverter {
            private const val tikXmlVersion = "0.8.13"
            const val retrofit = "com.tickaroo.tikxml:retrofit-converter:$tikXmlVersion"

            const val annotation = "com.tickaroo.tikxml:annotation:$tikXmlVersion"
            const val core = "com.tickaroo.tikxml:core:$tikXmlVersion"
            const val annotationProcessor = "com.tickaroo.tikxml:processor:$tikXmlVersion"
        }

        object Chucker {
            private const val version = "3.4.0"
            const val debug = "com.github.chuckerteam.chucker:library:$version"
            const val release = "com.github.chuckerteam.chucker:library-no-op:$version"
        }
    }

    object ExoPlayer {
        private const val version = "2.13.0"
        const val full = "com.google.android.exoplayer:exoplayer:$version"
        const val core = "com.google.android.exoplayer:exoplayer-core:$version"
        const val dash = "com.google.android.exoplayer:exoplayer-dash:$version"
        const val ui = "com.google.android.exoplayer:exoplayer-ui:$version"
        const val smoothStreaming =
            "com.google.android.exoplayer:exoplayer-smoothstreaming:$version"
        const val hls = "com.google.android.exoplayer:exoplayer-hls:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.2.0-rc01"
        const val coreKtx = "androidx.core:core-ktx:1.5.0-beta01"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"

        object Compose {
            const val snapshot = ""
            const val version = "1.0.0-alpha10"

            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val material = "androidx.compose.material:material:$version"
            const val materialIconsExtended =
                "androidx.compose.material:material-icons-extended:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val test = "androidx.compose.ui:ui-test:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:${version}"
            const val viewBinding = "androidx.compose.ui:ui-viewbinding:$version"
        }

        object Room {
            private const val version = "2.2.6"

            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"

            const val coroutineExt = "androidx.room:room-ktx:$version"
        }

        object Navigation {
            private const val version = "2.3.2"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
            const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Test {
            private const val version = "1.2.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.2-rc01"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
        }

        object Lifecycle {
            private const val version = "2.2.0"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }
    }
}

object Urls {
    const val composeSnapshotRepo = "https://androidx.dev/snapshots/builds/" +
            "${Libs.AndroidX.Compose.snapshot}/artifacts/repository/"
    const val accompanistSnapshotRepo = "https://oss.sonatype.org/content/repositories/snapshots"
}

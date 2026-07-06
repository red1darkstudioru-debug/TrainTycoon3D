plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }

android { namespace = "com.traintycoon3d"; compileSdk = 36
    defaultConfig { applicationId = "com.traintycoon3d"; minSdk = 23; targetSdk = 36; versionCode = 1; versionName = "1.0" }
}

val gdxVersion = "1.13.5"
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-backend-android:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")
    implementation("com.badlogicgames.gdx:gdx-bullet:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-armeabi-v7a")
    implementation("com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-arm64-v8a")
    implementation("com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-x86")
    implementation("com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-x86_64")
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("androidx.datastore:datastore-preferences:1.1.7")
}

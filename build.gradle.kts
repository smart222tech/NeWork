plugins {
    id("com.android.application") version "9.3.2" apply false
    id("com.android.library") version "9.3.2" apply false
    id("org.jetbrains.kotlin.android") version "2.2.10" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}

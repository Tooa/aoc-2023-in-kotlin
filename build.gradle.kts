plugins {
    kotlin("jvm") version "1.9.21"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

dependencies {
    implementation("io.arrow-kt:arrow-core:1.2.1")
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}

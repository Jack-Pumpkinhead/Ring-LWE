plugins {
    kotlin("jvm") version "1.6.20"
}

group = "land.oz.munchkin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.ionspin.kotlin:bignum:0.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

    implementation("com.github.microsoft:TSS.Java:0.3.0")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
}
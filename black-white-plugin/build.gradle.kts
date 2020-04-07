plugins {
    `kotlin-dsl`
    groovy
    `maven-publish`
}
dependencies {
    gradleApi()
    localGroovy()
    implementation("com.android.tools.build:gradle:3.6.0")
    implementation("org.ow2.asm:asm:8.0")
    implementation("org.ow2.asm:asm-commons:8.0")
}
publishing {
    repositories {
        maven {
            url = uri("../local-repo")
        }
    }
}
group = "com.devnan.plugin"
version = "1.0.0"

apply {from("../plugin-publish.gradle")}
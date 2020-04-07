# black-white-plugin
### usage
```kotlin
// Top-level buid.gradle
buildscript {
    repositories {
        ...
    }
    dependencies {
        classpath 'com.devnan.plugin:black-white-plugin:1.0.0'
    }
}

// App module buid.gradle
apply plugin: 'black-white-plugin'

```
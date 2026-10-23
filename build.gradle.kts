import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform")
}

val ideLine = providers.gradleProperty("ideLine").orElse("modern").get()
require(ideLine in listOf("legacy", "modern")) {
    "Use -PideLine=legacy or -PideLine=modern"
}
val isLegacy = ideLine == "legacy"

version = providers.gradleProperty(if (isLegacy) "legacyVersion" else "version").get()
layout.buildDirectory.set(layout.projectDirectory.dir("build/$ideLine"))

kotlin {
    jvmToolchain(if (isLegacy) 17 else 21)
    if (!isLegacy) {
        sourceSets.named("main") {
            kotlin.srcDir("src/modern/kotlin")
        }
    }
}

if (!isLegacy) {
    sourceSets.named("main") {
        resources.srcDir("src/modern/resources")
    }
}

dependencies {
    intellijPlatform {
        if (isLegacy) {
            intellijIdeaCommunity("2024.1")
        } else {
            intellijIdea("2025.3.6.1")
            bundledPlugin("org.jetbrains.plugins.terminal")
        }
    }
}

intellijPlatform {
    buildSearchableOptions = false
    sandboxContainer.set(layout.projectDirectory.dir(".intellijPlatform/sandbox/$ideLine"))

    pluginConfiguration {
        ideaVersion {
            sinceBuild = if (isLegacy) "241.14494.240" else "253.33813.55"
            untilBuild = if (isLegacy) "253.*" else "262.*"
        }
    }

    pluginVerification {
        ides {
            current()
            if (isLegacy) {
                create(IntelliJPlatformType.IntellijIdeaCommunity, "2024.2.6")
                create(IntelliJPlatformType.IntellijIdeaCommunity, "2024.3.7.1")
                create(IntelliJPlatformType.IntellijIdeaCommunity, "2025.1.7.2")
                create(IntelliJPlatformType.IntellijIdeaCommunity, "2025.2.6.3")
                create(IntelliJPlatformType.IntellijIdea, "2025.3")
                create(IntelliJPlatformType.IntellijIdea, "2025.3.6.1")
            } else {
                create(IntelliJPlatformType.IntellijIdea, "2026.1.5")
                create(IntelliJPlatformType.IntellijIdea, "2026.2.3")
            }
        }
    }
}

tasks.processResources {
    from("LICENCE.md") {
        into("META-INF")
    }
}
plugins {
    java
    `maven-publish`
    kotlin("jvm") version "2.4.10"
}

// API versioning follows the Blueva convention used by BlueArcade-API:
// two segments (major.api). The BlueSpoof plugin itself uses three segments;
// its first two segments always match the API version it exposes.
group = "net.blueva.spoof"
version = "3.7"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

java {
    // The plugin core emits Java 8 bytecode so the API must too: its classes are
    // shaded unrelocated into the BlueSpoof jar and loaded on every supported server.
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

dependencies {
    // Bukkit API only; the API surface intentionally uses plain Bukkit types
    // (Player, Location, Entity...) because BlueSpoof is a Bukkit-only plugin.
    compileOnly("org.spigotmc:spigot-api:26.1-R0.1-SNAPSHOT")
    testImplementation(kotlin("test"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

kotlin {
    jvmToolchain(8)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("BlueSpoof API")
                description.set("Public API for controlling BlueSpoof fake players from any Bukkit/Paper plugin.")
                url.set("https://github.com/BluevaDevelopment/BlueSpoofAPI")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("blueva")
                        name.set("Blueva Development")
                        url.set("https://github.com/BluevaDevelopment")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/BluevaDevelopment/BlueSpoofAPI.git")
                    developerConnection.set("scm:git:ssh://git@github.com/BluevaDevelopment/BlueSpoofAPI.git")
                    url.set("https://github.com/BluevaDevelopment/BlueSpoofAPI")
                }
            }
        }
    }
    repositories {
        maven {
            name = "BluevaRepo"
            url = uri("https://repo.blueva.net/releases")
            credentials {
                username = providers.environmentVariable("BLUEVA_REPO_USERNAME").orNull
                password = providers.environmentVariable("BLUEVA_REPO_SECRET").orNull
            }
        }
    }
}

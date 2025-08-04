import xyz.jpenilla.runpaper.task.RunServer

plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.mythmc.ovh/releases/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    compileOnly("ovh.mythmc:banco-api:1.0.2")
    compileOnly("ovh.mythmc:callbacks-lib:0.1.2")
}

group = "ovh.mythmc"
version = "1.0.0"
description = "banco-cached-item-extension"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.withType<RunServer>() {
    minecraftVersion("1.21.5")
    downloadPlugins {
        github("myth-MC", "banco", "1.0.3", "banco-modern-1.0.3.jar")
        modrinth("vaultunlocked", "2.15.0")
    }
}
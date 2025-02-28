import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
  `java-library`
  id("xyz.jpenilla.run-paper") version "2.3.1" // Adds runServer and runMojangMappedServer tasks for testing
  id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.2.0" // Generates plugin.yml based on the Gradle config

  // Shades and relocates dependencies into our plugin jar. See https://imperceptiblethoughts.com/shadow/introduction/
  id("com.gradleup.shadow") version "8.3.3"
  kotlin("jvm")
}

group = "io.papermc.paperweight"
version = "1.0.0-SNAPSHOT"
description = "Test plugin for paperweight-userdev"

java {
  // Configure the java toolchain. This allows gradle to auto-provision JDK 21 on systems that only have JDK 11 installed for example.
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

// For 1.20.4 or below, or when you care about supporting Spigot on >=1.20.5:
/*
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

tasks.assemble {
  dependsOn(tasks.reobfJar)
}
 */

dependencies {
  // paperweight.foliaDevBundle("1.21.4-R0.1-SNAPSHOT")
  // paperweight.devBundle("com.example.paperfork", "1.21.4-R0.1-SNAPSHOT")

  // Shadow will include the runtimeClasspath by default, which implementation adds to.
  // Dependencies you don't want to include go in the compileOnly configuration.
  // Make sure to relocate shaded dependencies!
  implementation("dev.jorel:commandapi-bukkit-kotlin:9.7.0")
  implementation("dev.jorel:commandapi-bukkit-shade:9.7.0")
  implementation(kotlin("stdlib-jdk8"))
  compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

}

tasks {
  runServer{
    minecraftVersion("1.21.4")
  }
  compileJava {
    // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
    // See https://openjdk.java.net/jeps/247 for more information.
    options.release = 21
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
  }

  // Only relevant for 1.20.4 or below, or when you care about supporting Spigot on >=1.20.5:
  /*
  reobfJar {
    // This is an example of how you might change the output location for reobfJar. It's recommended not to do this
    // for a variety of reasons, however it's asked frequently enough that an example of how to do it is included here.
    outputJar = layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar")
  }
   */

  shadowJar {
    // helper function to relocate a package into our package
    fun reloc(pkg: String) = relocate(pkg, "io.papermc.paperweight.testplugin.dependency.$pkg")

    // relocate cloud and it's transitive dependencies
    reloc("org.incendo.cloud")
    reloc("io.leangen.geantyref")


    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "io.papermc.paperweight.testplugin.commandapi")
  }
}

// Configure plugin.yml generation
// - name, version, and description are inherited from the Gradle project.
bukkitPluginYaml {
  main = "io.papermc.paperweight.testplugin.TestPlugin"
  load = BukkitPluginYaml.PluginLoadOrder.STARTUP
  authors.add("Author")
  apiVersion = "1.21"
}
repositories {
  mavenCentral()
  maven(url = "https://repo.codemc.org/repository/maven-public/")
  maven("https://repo.papermc.io/repository/maven-public/") {
    name = "papermc-repo"
  }
}
kotlin {
  jvmToolchain(21)
}

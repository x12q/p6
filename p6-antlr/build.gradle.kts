plugins {
    java
    antlr
    `maven-publish`
//    val kv = libs.versions.kotlinVersion.get()
//    kotlin("jvm") version kv
}
val id = "p6-antlr"
val _group = "com.qxdzbc.p6"
group = _group
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    antlr ("org.antlr:antlr4:4.9.3")
}

java {
    val javaVersion = libs.versions.javaVersion.get()
    toolchain {
        this.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}


val outputDir = "/generated-src/antlr4/com/qxdzbc/p6/formula/translator/antlr"

tasks{
    generateGrammarSource{
        this.outputDirectory = File("${buildDir.absolutePath}$outputDir")
        this.arguments = this.arguments +
                listOf("-package" ,"${_group}.formula.translator.antlr") +
                listOf("-visitor")

    }
}
sourceSets {
    main {
        java {
            this.srcDirs("${buildDir.absolutePath}$outputDir")
        }
    }
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = id
            version = version
            from(components["java"])
        }
    }
}

import java.util.Locale

plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":data-source"))
    implementation(project(":sorting"))
}

abstract class CountLettersPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("count-letters") {
            doLast {
                val letterCounts = mutableMapOf<Char, Int>()

                project.rootDir.walkTopDown()
                    .onEnter { it.name != "build" && it.name != ".gradle" }
                    .filter { it.isFile }
                    .forEach { file ->
                        try {
                            file.forEachLine { line ->
                                line.uppercase()
                                    .filter { it in 'A'..'Z' }
                                    .forEach { char ->
                                        letterCounts[char] = letterCounts.getOrDefault(char, 0) + 1
                                    }
                            }
                        } catch (e: Exception) {
                            println("Skipped file (unreadable or locked): ${file.absolutePath}")
                        }
                    }
                println("Letter frequencies (A–Z):")
                letterCounts.entries.sortedBy { it.key }.forEach { (char, count) ->
                    println("$char: $count")
                }
            }

        }
    }
}

apply<CountLettersPlugin>()


tasks.test {
    useJUnitPlatform()
}
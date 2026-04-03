plugins {
    id("java")
    id("io.qameta.allure") version "3.1.0"
}

group = "ru.avito.qa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {


    // Source: https://mvnrepository.com/artifact/io.rest-assured/rest-assured
    testImplementation("io.rest-assured:rest-assured:5.5.6")
    testImplementation("io.rest-assured:json-schema-validator:5.5.6")

    // Source: https://mvnrepository.com/artifact/io.rest-assured/json-schema-validator
    implementation("io.rest-assured:json-schema-validator:5.5.6")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")

    // Source: https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:3.27.7")

    // Source: https://mvnrepository.com/artifact/tools.jackson.core/jackson-databind
    implementation("tools.jackson.core:jackson-databind:3.1.0")

    // Source: https://mvnrepository.com/artifact/org.projectlombok/lombok
    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")

    // Source: https://mvnrepository.com/artifact/io.qameta.allure/allure-junit5
    testImplementation("io.qameta.allure:allure-junit5:2.33.0")
    // Source: https://mvnrepository.com/artifact/com.github.javafaker/javafaker
    implementation("com.github.javafaker:javafaker:1.0.2")



}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Test>().configureEach {
    ignoreFailures = true

    jvmArgs = listOf("-Dfile.encoding=UTF-8")
    systemProperty("file.encoding", "UTF-8")
}
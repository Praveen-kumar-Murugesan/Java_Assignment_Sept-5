plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.apache.logging.log4j:log4j-api:2.17.2")
    implementation ("org.apache.logging.log4j:log4j-core:2.17.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.mockito:mockito-core:5.13.0")
}

tasks.test {
    useJUnitPlatform()
}
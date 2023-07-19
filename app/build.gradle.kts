plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    implementation("org.slf4j:slf4j-simple:1.7.9")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.0")
    implementation("javax.websocket:javax.websocket-client-api:1.1")
    implementation("org.glassfish.tyrus.bundles:tyrus-standalone-client:1.9")
    implementation("software.amazon.awssdk:core:2.20.103")
    implementation("software.amazon.awssdk:s3:2.20.103")
    implementation("software.amazon.awssdk:regions:2.20.103")
}

application {
    // Define the main class for the application.
    mainClass.set("gt.trading.openbook.App")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

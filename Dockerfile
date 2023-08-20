FROM amazoncorretto:17-al2-native-jdk
ADD /target/MyPracticeWithYandexTask-0.0.1-SNAPSHOT.jar springapp.jar

ENTRYPOINT ["java", "-jar", "springapp.jar"]
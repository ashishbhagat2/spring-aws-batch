FROM openjdk:11
EXPOSE 8080
ADD target/springbatch-aws-0.0.1-SNAPSHOT.jar springbatch-aws-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/springbatch-aws-0.0.1-SNAPSHOT.jar"]
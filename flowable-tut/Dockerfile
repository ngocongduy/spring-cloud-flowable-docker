# For Java 8, try this
FROM openjdk:8-jdk-alpine

# For Java 11, try this
#FROM adoptopenjdk/openjdk11:alpine-jre

# Refer to Maven build -> finalName
ARG JAR_FILE=target/flowable-tut-1.0-SNAPSHOT.jar

# Change time zone
RUN apk add tzdata
RUN cp /usr/share/zoneinfo/Asia/Bangkok /etc/localtime
RUN echo "Asia/Bangkok" > /etc/timezone
ENV TZ="Asia/Bangkok"


ENV MYSQL_DATABASE_URL="jdbc:mysql://mysql-db:3306/flowable?characterEncoding=UTF-8"
ENV MYSQL_DATABASE_USERNAME=flowable
ENV MYSQL_DATABASE_PASSWORD=flowable

# cd /opt/app
WORKDIR /opt/app

# cp jar_file to /opt/app/app.jar
COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
# ENTRYPOINT ["java","-jar","app.jar"]
CMD ["java","-jar","app.jar"]
EXPOSE 8081

# For RabbitMq to be at: localhost:5672
#EXPOSE 5672
FROM adoptopenjdk/openjdk11:latest

ARG HEAP_SIZE
ENV HEAP_SIZE=${HEAP_SIZE:-256M}
ARG NEW_SIZE
ENV NEW_SIZE=${NEW_SIZE:-256M}
ARG META_SIZE
ENV META_SIZE=${META_SIZE:-256M}
ARG ACTIVE
ENV ACTIVE=${ACTIVE:-production}
ARG PASSWORD
ENV PASSWORD=${PASSWORD:-1}

ADD . .

RUN ["./gradlew", "clean", "build", "-x","test"]

ENTRYPOINT java -server -Dspring.profiles.active=${ACTIVE} -Djasypt.encryptor.password=${PASSWORD} -Xms${HEAP_SIZE} -Xmx${HEAP_SIZE} -XX:NewSize=${NEW_SIZE} -XX:MaxNewSize=${NEW_SIZE} -XX:MaxMetaspaceSize=${META_SIZE} -Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/./urandom -jar /build/libs/flog-0.0.1-SNAPSHOT.jar
FROM adoptopenjdk/openjdk11:latest

ARG HEAP_SIZE
ENV HEAP_SIZE=${HEAP_SIZE:-256M}
ARG NEW_SIZE
ENV NEW_SIZE=${NEW_SIZE:-256M}
ARG META_SIZE
ENV META_SIZE=${META_SIZE:-256M}

ADD . .

RUN ["./gradlew", "clean", "build", "-x","test"]
RUN mkdir -p /var/log/flog

ENTRYPOINT java -server -Xms${HEAP_SIZE} -Xmx${HEAP_SIZE} -XX:NewSize=${NEW_SIZE} -XX:MaxNewSize=${NEW_SIZE} -XX:MaxMetaspaceSize=${META_SIZE} -Djasypt.encryptor.password=${JASYPT} -Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/./urandom -jar /build/libs/flog-0.0.1-SNAPSHOT.jar
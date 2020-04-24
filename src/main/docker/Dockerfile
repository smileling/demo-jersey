From openjdk:8u131
USER root
ARG CODE_VERSION=0.0.1-SNAPSHOT
ARG TARGET_DIR=.

# RUN apk add tzdata && \
#     cp /usr/share/zoneinfo/MST /etc/localtime && \
#     echo "MST" > /etc/timezone && \
#     apk del tzdata

ENV TZ=America/Los_Angeles
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# RUN apt-get update && apt-get install -y sysstat procps

COPY ${TARGET_DIR}/demo-jersey/target/demo-jersey-${CODE_VERSION}.jar demo-svc.jar
COPY ${TARGET_DIR}/demo-jersey/src/main/resources/data/users.csv /tmp/demo/users.csv

# java -jar target/demo-jersey-0.0.1-SNAPSHOT.jar --server.port=80 -Dlog4j.debug
ENTRYPOINT ["java", "-jar", "demo-svc.jar", "--server.port=80", "-Dlog4j.debug"]
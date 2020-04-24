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

### docker build...
# cd /Users/lingli1/work/spring
# docker build -t <repo>/lingli1/demo:v1 -f demo-jersey/src/main/docker/Dockerfile .
# docker push <repo>>/lingli1/demo:v1
### run local
# java -jar demo-svc.jar --server.port=80 -Dlog4j.debug
# java -cp demo-jersey-0.0.1-SNAPSHOT.jar com.example.demojersey.DemoJerseyApplication --server.port=80
# java -jar demo-jersey-0.0.1-SNAPSHOT.jar --server.port=80 -Dlog4j.debug
### docker run...
# docker run -it --rm -p 80:80 <repo>/lingli1/demo:v1
# docker run --rm -d -p 80:80 <repo>/lingli1/demo:v1

# docker build -t <repo>/lingli1/demo:v2 -f demo-jersey/src/main/docker/Dockerfile_v2 .
# docker run --rm -d -p 80:80 <repo>/lingli1/demo:v2
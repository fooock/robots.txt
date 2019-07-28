FROM adoptopenjdk/openjdk8-openj9:alpine-slim

ENV SERVER_ADDRESS="0.0.0.0"
VOLUME /tmp

COPY build/dependency/BOOT-INF/lib /app/lib
COPY build/dependency/META-INF /app/META-INF
COPY build/dependency/BOOT-INF/classes /app

ENTRYPOINT ["java", "-Xtune:virtualized", "-cp", "app:app/lib/*", "com.fooock.robotstxt.downloader.DownloaderApplication"]

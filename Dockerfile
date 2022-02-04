FROM gradle:7.3.3-jdk17-alpine as buildMvpApp

ENV APP_HOME=/home/gradle/mvp
COPY --chown=gradle:gradle \
    ./settings.gradle.kts \
    ./gradlew \
    ${APP_HOME}/

COPY --chown=gradle:gradle ./app ${APP_HOME}/app

WORKDIR ${APP_HOME}

RUN gradle build --no-daemon --stacktrace
VOLUME [ "/home/gradle/mvp/app/build", "/home/gradle/.gradle" ]
ENTRYPOINT ["gradle", "run", "--stacktrace"]
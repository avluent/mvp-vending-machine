FROM openjdk as runMvpApp

RUN mkdir /app
VOLUME [ "/app" ]
WORKDIR /app

RUN mkdir /app/run /app/log
RUN tar -xf /app/distributions/app.tar --directory /app/run

EXPOSE 8443
ENTRYPOINT [ "/bin/sh", "-c", "./run/app/bin/app" ]
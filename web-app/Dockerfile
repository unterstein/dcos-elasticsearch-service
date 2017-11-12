FROM java:8

MAINTAINER Johannes Unterstein <junterstein@mesosphere.io>

RUN mkdir /app
ADD target/universal/eaas-web-1.0-SNAPSHOT.tgz /app
EXPOSE 9000

CMD ["/app/eaas-web-1.0-SNAPSHOT/bin/eaas-web", "-Dplay.http.secret.key=abcdefghijk"]

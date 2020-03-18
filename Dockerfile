FROM alpine/git as clone
WORKDIR /app
RUN git clone -b ec-sql-modifiedclasses --single-branch https://github.com/rkboyce/iDIA_Rules.git


FROM phusion/baseimage:latest as build

WORKDIR /app
COPY --from=clone /app/iDIA_Rules /app
RUN ["chmod", "+x", "/app/runRules.sh"]
RUN ["chmod", "+x", "/app/dockerRun.sh"]
RUN mkdir simmulated-run
RUN mkdir banner-run

# Need to copy correct config.properties to container
COPY config.properties /app/config.properties

# Install OpenJDK-8
RUN apt-get update && \
  apt-get -y -q install maven &&\
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y ant && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME

RUN mvn install:install-file -Dfile=./lib/opencsv-2.3.jar -DgroupId=org.opencsv -DartifactId=opencsv -Dversion=2.3 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/hibernate-core-3.3.0.SP1.jar -DgroupId=hibernate -DartifactId=hibernate -Dversion=3.3.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/hibernate-annotations-3.4.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-annotations -Dversion=3.4.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/hibernate-commons-annotations-3.1.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-commons-annotations -Dversion=3.1.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/ehcache-core-2.1.0.jar -DgroupId=ehcache -DartifactId=ehcache-core -Dversion=2.1.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./lib/antlr.jar -DgroupId=antlr -DartifactId=antlr -Dversion=unknown -Dpackaging=jar
RUN mvn install

COPY dump-idiarules-201911061128.tar /app/idiarules-dump.tar

# Set up PSQL
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys B97B0AFCAA1A47F044F244A07FCC7D46ACCC4CF8

RUN echo "deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main" > /etc/apt/sources.list.d/pgdg.list
RUN apt-get -y -q install postgresql-9.5 postgresql-client-9.5 postgresql-contrib-9.5

USER postgres

RUN /etc/init.d/postgresql start \
  && psql --command "CREATE USER idiarules WITH SUPERUSER PASSWORD 'idiarules';" \
  && createdb -O idiarules idiarules idiarules \
  && pg_restore --dbname=idiarules --verbose /app/idiarules-dump.tar;

USER root
#RUN systemctl enable postgresql
#RUN update-rc.d postgresql enable
#ENTRYPOINT ["/etc/init.d/postgresql", "start"]
ENTRYPOINT ["/app/dockerRun.sh"]

# Allows user to specify the run of either "simulated" or "banner"
CMD []

# Running:
# -v mounts the container and then the user can specify the local filepath to copy contents from container
# -m sets the memory for the container
# can override properties such as schema, ruleFolder, connectionURL, etc.
# docker run -m 8g -v ~/simulated-run:/app/simulated-run -it --rm maxsibilla/idia_rules simulated schema=simulated
FROM java:8

COPY scripts/wait-for-it.sh /wait-for-it.sh

COPY target/universal/trend-service-1.0-SNAPSHOT.zip /opt/

WORKDIR /opt

RUN unzip /opt/trend-service-1.0-SNAPSHOT.zip

RUN rm /opt/trend-service-1.0-SNAPSHOT.zip

EXPOSE 9000

ENTRYPOINT ["/opt/trend-service-1.0-SNAPSHOT/bin/trend-service"]
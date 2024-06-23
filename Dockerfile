FROM openjdk:17-oracle
COPY authorization-0.0.1-SNAPSHOT.jar /
RUN mv /authorization-0.0.1-SNAPSHOT.jar /authorization.jar


RUN rm -f /etc/localtime
RUN rm -f /etc/timezone
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/timezone
RUN mkdir /oauth2 && mv /usr/java/openjdk-17 /oauth2/java && \
mv authorization.jar /oauth2/ && \
unlink /bin/sh && ln -s /bin/bash /bin/sh


ENV PATH=/oauth2/java/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin JAVA_HOME=/oauth2/java

EXPOSE 27001

ENTRYPOINT "bash" "-c" "java -jar \
           -server \
           -Xms1024m \
           -Xmx2048m \
           -XX:MetaspaceSize=1024m \
           -XX:MaxMetaspaceSize=1024m \
           -verbose:gc \
           -XX:+PrintGCDetails \
           -Xlog:gc*:file=gc.log:time:filecount=7,filesize=8M \
           /oauth2/authorization.jar"
#LDAP Server Image
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/ldapService.jar ldapService.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /ldapService.jar" ]
CMD [“echo”,”Image created”]
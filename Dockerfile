FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]


# Multi-stage
# add custome user name ~ ajitesh -- system 
# groupadd -r <groupname>
# useradd --system -g/G 
# chown a:dfj -R /app

# labels 
# oci

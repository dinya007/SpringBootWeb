FROM tomcat:8-jre8
RUN ["rm", "-r", "-fr", "/usr/local/tomcat/webapps/"]
ADD /certificates/cert.pem /etc/ssl/certs/
ADD /certificates/key.pem /etc/ssl/certs/
ADD build/libs/world-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
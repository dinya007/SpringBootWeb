FROM tomcat:8-jre8
RUN ["rm", "-r", "-fr", "/usr/local/tomcat/webapps/"]
ADD WebSample-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]
set MAVEN_OPTS=-XX:MaxPermSize=512M
mvn install -Dmaven.test.skip=true
@pause
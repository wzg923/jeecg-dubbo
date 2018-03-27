set MAVEN_OPTS=-XX:MaxPermSize=512M
mvn clean install -Ptest -Dmaven.test.skip=true
@pause
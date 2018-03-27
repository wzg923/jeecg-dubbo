@echo -----------------------------------------------------------------------------
@echo 测试环境打包
@echo -----------------------------------------------------------------------------

@rem 设置变量，获取当前目录
set dir=%cd%
@echo 当前目录%dir%
@echo 开始更新代码-ALL

@rem 更新指定目录
svn cleanup
svn update "%dir%"
set MAVEN_OPTS=-XX:MaxPermSize=256M
mvn clean package -Ptest -Dmaven.test.skip=true
@pause
@echo -----------------------------------------------------------------------------
@echo Maven deploy
@echo -----------------------------------------------------------------------------


set dir=%cd%
@echo 开始更新代码-ALL

@rem 更新指定目录
svn cleanup
svn update "%dir%"

@rem maven deploy
set MAVEN_OPTS=-XX:MaxPermSize=256M
mvn deploy -Dmaven.test.skip=true


@pause



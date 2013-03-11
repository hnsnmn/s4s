아래 명령어를 사용해서 src/externalJars에 있는 jar 리포지토리에 등록

mvn install:install-file -Dfile=im4java-1.1.0.jar -DgroupId=im4java -DartifactId=im4java -Dversion=1.1.0 -Dpackaging=jar
mvn install:install-file -Dfile=im4java-1.1.0-sources.jar -DgroupId=im4java -DartifactId=im4java -Dversion=1.1.0 -Dclassifier=sources -Dpackaging=jar

오라클 10g용 JDCB 드라이버를 구해서 아래 명령 실행

mvn install:install-file -Dfile=ojdbc14.jar -DgroupId=com.oracle -DartifactId=ojdbc14 -Dversion=10.2.0.2.0 -Dpackaging=jar

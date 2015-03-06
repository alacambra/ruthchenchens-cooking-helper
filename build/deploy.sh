#!/bin/sh

BUILD_PATH=/home/alacambra/cookinghelper/build

envsubst '${COOKINGHELPER_DB_USR}:${COOKINGHELPER_DB_PSW}' < "standalone-tpl.xml" > "standalone.xml"
envsubst '${COOKINGHELPER_WF_USR}:${COOKINGHELPER_WF_PSW}' < "Dockerfile-tpl" > "Dockerfile"

cp ../src/main/resources/META-INF/persistence.prod.xml ../src/main/resources/META-INF/persistence.xml
cd ..
mvn clean package
cd build
#scp mysql-connector-java-5.1.34-bin.jar
scp ../target/cookinghelper.war build.sh Dockerfile standalone.xml lacambra.de:${BUILD_PATH}
ssh lacambra.de cd ${BUILD_PATH}
ssh -t lacambra.de sudo ${BUILD_PATH}/build.sh
rm standalone.xml
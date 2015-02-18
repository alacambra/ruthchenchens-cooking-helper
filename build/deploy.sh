#!/bin/sh

BUILD_PATH=/home/alacambra/cookinghelper/build

cp ../src/main/resources/META-INF/persistence.prod.xml ../src/main/resources/META-INF/persistence.xml
cd ..
mvn clean package
cd build
scp ../target/cookinghelper.war cookinghelper.h2.db build.sh Dockerfile standalone.xml lacambra.de:${BUILD_PATH}
ssh lacambra.de cd ${BUILD_PATH}
ssh -t lacambra.de sudo ${BUILD_PATH}/build.sh
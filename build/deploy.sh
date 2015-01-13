#!/bin/sh
cp ../src/main/resources/META-INF/persistence.xml ../src/main/resources/META-INF/persistence.tmp.xml
cp ../src/main/resources/META-INF/persistence.prod.xml ../src/main/resources/META-INF/persistence.xml
cd ..
mvn clean package
cd build
cp ../src/main/resources/META-INF/persistence.tmp.xml ../src/main/resources/META-INF/persistence.xml
scp ../target/cookinghelper.war cookinghelper.h2.db build.sh Dockerfile standalone.xml lacambra.de:/home/alacambra/cookinghelper
ssh lacambra.de cd /home/alacambra/cookinghelper
ssh -t lacambra.de sudo /home/alacambra/cookinghelper/build.sh
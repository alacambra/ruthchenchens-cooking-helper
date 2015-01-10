#!/bin/sh
cd ..
mvn clean package
cd build
scp ../target/cookinghelper.war cookinghelper.h2.db build.sh Dockerfile standalone.xml lacambra.de:/home/alacambra/cookinghelper
ssh lacambra.de cd /home/alacambra/cookinghelper
ssh -t lacambra.de sudo /home/alacambra/cookinghelper/build.sh
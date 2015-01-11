#!/bin/sh
#cd ..
#mvn clean package
cd build
cp ../target/app.war app.war
sudo docker build -t cookinghelper .
sudo docker kill cookinghelper 
sudo docker rm cookinghelper 
sudo docker run --name cookinghelper -p 8080:8080 -p 9990:9990 -it cookinghelper
rm app.war
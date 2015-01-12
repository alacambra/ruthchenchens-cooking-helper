#!/bin/bash
mkdir db
sudo docker build --tag=cookinghelper /home/alacambra/cookinghelper/
sudo docker kill cookinghelper
sudo docker kill nginx-hub
sudo docker rm cookinghelper
sudo docker rm nginx-hub

sudo docker run --name cookinghelper \
     -itd cookinghelper \
#    -v /home/alacambra/cookinghelper/db:/opt/jboss/wildfly/db/ \


sudo docker run -d -p 80:80 \
  --name nginx-hub --link nginx-www:www \
  --link cookinghelper:cookinghelper \
  --link uploader:uploader \
  alacambra/nginx:hub nginx
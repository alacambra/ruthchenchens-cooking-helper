#!/bin/bash
COOKINGHELPER_HOME=/home/alacambra/cookinghelper
DB_PATH=${COOKINGHELPER_HOME}/db
BACKUP_PATH=${COOKINGHELPER_HOME}/backup
DOCKER_DB_PATH=/opt/jboss/wildfly/db
DOCKER_BACKUP_PATH=/opt/jboss/wildfly/db/backup

sudo docker build --tag=cookinghelper ${COOKINGHELPER_HOME}/build
sudo docker kill cookinghelper
sudo docker kill nginx-hub
sudo docker rm cookinghelper
sudo docker rm nginx-hub

sudo docker run --name cookinghelper -p 18080:8080 -p 19990:9990\
    -v ${DB_PATH}:${DOCKER_DB_PATH} \
    -v ${BACKUP_PATH}:${DOCKER_BACKUP_PATH} \
    -itd cookinghelper


sudo docker run -d -p 80:80 --name nginx-hub \
  --link cookinghelper:cookinghelper \
  alacambra/nginx:hub nginx


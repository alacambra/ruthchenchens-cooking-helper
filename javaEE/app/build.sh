#!/bin/bash
sudo docker build --tag=cookighelper .
sudo docker stop cookinghelper
sudo docker stop nginx-hub
sudo docker rm cookinghelper
sudo docker rm nginx-hub
sudo docker run --name cookinghelper -it cookinghelper
sudo docker run -d -p 80:80 --name nginx-hub --link nginx-www:www --link cookinghelper:cookinghelper alacambra/nginx:hub  nginx
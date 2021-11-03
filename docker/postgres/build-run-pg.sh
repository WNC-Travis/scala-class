#!/bin/bash
# Running postgres and viewing the log

#cd to this directory

docker rm scala-class-postgres
docker build -t scala-class-postgres .

#docker run -d --name scala-class-postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 scala-class-postgres
docker run -d --name scala-class-postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 scala-class-postgres

#psql -U postgres -p mysecretpassword postgres < 

docker exec -it scala-class-postgres less /var/lib/postgresql/data/log/postgresql.log


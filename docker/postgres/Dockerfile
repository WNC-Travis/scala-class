FROM postgres:latest

RUN apt-get update
RUN apt-get install -y vim less

USER postgres
ADD postgresql.conf.sample /etc/postgresql/postgresql.conf
ADD pg_hba.conf /etc/postgresql/pg_hba.conf
EXPOSE 5432

CMD ["postgres", "-c", "config_file=/etc/postgresql/postgresql.conf"]

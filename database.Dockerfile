FROM postgres:16-alpine

ENV POSTGRES_DB=prod_db
ENV POSTGRES_USER=prod_user
ENV POSTGRES_PASSWORD=prod_password

COPY ./docker/database/postgresql.conf /mnt/sda1/var/lib/postgresql/data/postgresql.conf
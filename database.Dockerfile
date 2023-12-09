FROM postgres:16-alpine

COPY ./docker/database/postgresql.conf /mnt/sda1/var/lib/postgresql/data/postgresql.conf
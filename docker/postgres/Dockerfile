FROM postgres:latest

# localize (debian-baseのpostgresを日本語対応にする。13 baseでは必要だったが 14:debian-11だと設定不要だった)
#RUN apt-get update \
# && apt-get install -y locales \
# && localedef -i ja_JP -c -f UTF-8 -A /usr/share/locale/locale.alias ja_JP.UTF-8 \
# && apt-get clean \
# && rm -rf /var/lib/apt/lists/*
#
#ENV LANG ja_JP.utf8

# https://hub.docker.com/_/postgres
# see Initialization scripts
COPY ./schema/*.sql /docker-entrypoint-initdb.d/
COPY ./table/*.sql /docker-entrypoint-initdb.d/
COPY ./init-data/*.sql /docker-entrypoint-initdb.d/

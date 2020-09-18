#!/bin/bash

# creates new db and new user who is owner of this db
# arg1 db name
# arg2 user name
# arg3 user pass
create_db_user_pass() {
    psql --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" -c "CREATE USER $2 WITH PASSWORD '$3'" && \
    psql --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" -c "CREATE DATABASE $1 OWNER=$2"
    psql --username "$POSTGRES_USER" --dbname "$1" -c "CREATE EXTENSION IF NOT EXISTS pgcrypto"
}

# executes sql on given DB
# arg1 db name
# arg2 sql to execute
exec_sql() {
    psql --username "$POSTGRES_USER" --dbname "$1" -c "$2"
}
create_db_user_pass keycloak       keycloak             password


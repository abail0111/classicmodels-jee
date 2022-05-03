# Classic Models API (JEE)

The Classic Models API project is a Jakarta EE implementation of the
[BIRT](https://eclipse.github.io/birt-website/docs/template-sample-database/) /
[MySQL](https://www.mysqltutorial.org/mysql-sample-database.aspx)
sample database as RESTful and GraphQL web service.


## Setup Database

This project uses a slightly modified version of the
[BIRT](https://eclipse.github.io/birt-website/docs/template-sample-database/) /
[MySQL](https://www.mysqltutorial.org/mysql-sample-database.aspx)
sample database 'ClassicModels'. To create the 'classicmodels' database use the
MySQL scripts from the /misc/mysql directory.

### MySQL

```shell script
# Start the mysql utility
mysql --local-infile=1 -u root -p

# Enabling LOAD DATA LOCAL INFILE in mysql
SET GLOBAL local_infile=1;

# Create the ClassicModels database and load the schema and content
create database classicmodels;
use classicmodels;
source create_classicmodels-auto_increment.sql;
source load_classicmodels.sql;
quit;
```

### PostgreSQL

```shell script
# Start PSQL
psql -U postgres

# Create database
CREATE DATABASE classicmodels WITH ENCODING 'UTF8' LC_COLLATE='German_Germany.1252' LC_CTYPE='German_Germany.1252';
# Connect to the new database
\c classicmodels

# Create tables and import data

# Option 1: Use create script.
# Make sure you have set the correct paths to the data files in the script.
\i {project}/misc/postgres/create_classicmodels.sql

# Option 2: Use the database dump
\i {project}/misc/postgres/classicmodels_dump.sql
```

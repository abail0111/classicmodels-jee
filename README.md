# Classic Models API (JEE)

The Classic Models API project is a Jakarta EE implementation of the
[BIRT](https://eclipse.github.io/birt-website/docs/template-sample-database/) /
[MySQL](https://www.mysqltutorial.org/mysql-sample-database.aspx)
sample database as a RESTful- and GraphQL web service.


## Setup Database

This project uses a slightly modified version of the
[BIRT](https://eclipse.github.io/birt-website/docs/template-sample-database/) /
[MySQL](https://www.mysqltutorial.org/mysql-sample-database.aspx)
sample database 'ClassicModels'. To create the 'classicmodels' database use the
PostgreSQL or MySQL scripts from the `/misc/` directory.
Please update the `persistence.xml` file according to the selected database.


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

## Getting Started
The Application was tested with
`Wildfly 26.1.0.Final` and `Java 11`.<br/>
Package the application using maven:

```
mvn clean package
```

## Testing

The application contains GraphQL test cases based on generated GraphQL-Operations.
In case of significant changes of the schema, the operations and tests must be updated.

### Generate GraphQL Operations

Use [gql-generator](https://github.com/timqian/gql-generator)
to generate queries and mutations from GraphQL Schema.
Copy the Schema file from
[/src/main/resources/schema.graphqls](/src/main/resources/schema.graphqls)
into the test resource directory `src\test\resources\graphql` and run the generator.

```shell script
# Install
npm install gql-generator -g

# Generate queries and mutations from schema file
gqlg --schemaFilePath ./schema.graphql --destDirPath ./operations --depthLimit 2
```

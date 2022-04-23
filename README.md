# Classic Models API (JEE)

The Classic Models API project is a Jakarta EE implementation of the
[BIRT](https://eclipse.github.io/birt-website/docs/template-sample-database/) /
[MySQL](https://www.mysqltutorial.org/mysql-sample-database.aspx)
sample database as RESTful and GraphQL web service.


## Database

This project uses a slightly modified version of the
[BIRT](https://eclipse.github.io/birt-website/docs/template-sample-database/) /
[MySQL](https://www.mysqltutorial.org/mysql-sample-database.aspx)
sample database 'ClassicModels'. To create the 'classicmodels' database use the
MySQL scripts from the /misc/mysql directory.

```shell script
# Start the mysql utility
mysql --local-infile=1 -u root -p

# Enabling LOAD DATA LOCAL INFILE in mysql
mysql> SET GLOBAL local_infile=1;

# Create the ClassicModels database and load the schema and content
mysql> create database classicmodels;
mysql> use classicmodels;
mysql> source create_classicmodels-auto_increment.sql;
mysql> source load_classicmodels.sql;
mysql> quit;
```

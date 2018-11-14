# Project Title

MysqlORM 

## Getting Started

Just add this dependency in your POM file.

<dependency>
  <groupId>io.github.idolofidles</groupId>
  <artifactId>MysqlORM</artifactId>
  <version>1.0.0</version>
</dependency>

add a file in your claspath named database.properties and provide these properties

db.model.package = <package name of models or dtos> 
db.driver = <drivername>
db.user = <database username>
db.password =<database password> 
db.name = <database name>
db.url = jdbc:mysql://localhost:3306/<database name>

example
db.model.package = safayat.orm.model
db.driver = com.mysql.cj.jdbc.Driver
db.user = root
db.password = root
db.name = db
db.url = jdbc:mysql://localhost:3306/db

## Built With

* JDBC
* [Maven](https://maven.apache.org/) - Dependency Management
* HikariCP
* java 8


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details







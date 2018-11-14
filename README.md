# Project Title

MysqlORM 

## Getting Started


```
<dependency>
  <groupId>io.github.idolofidles</groupId>
  <artifactId>MysqlORM</artifactId>
  <version>1.0.0</version>
</dependency>
```



Add a file in your claspath named database.properties and provide these properties

```
db.model.package = <package name of models or dtos> 
db.driver = <drivername>
db.user = <database username>
db.password =<database password> 
db.name = <database name>
db.url = jdbc:mysql://localhost:3306/<database name>
```

Example
```
db.model.package = safayat.orm.model
db.driver = com.mysql.cj.jdbc.Driver
db.user = root
db.password = root
db.name = db
db.url = jdbc:mysql://localhost:3306/db
```
## Built With

* JDBC
* [Maven](https://maven.apache.org/) - Dependency Management
* HikariCP
* java 8


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details


### Tutorial
  ##Query Example
  Make your DAO or repository class Extend the GeneralRepository provided by MysqlORM.
 
```
 public class PersonDAO extends GeneralRepository {
 }
 PersonDAO personDAO = new PersonDAO();
 List<Person> personList = personDAO.getAll(Person.class);
 
```
OneToMany example
```


@Table(name = "category"
        , databaseName = "dbname")
public class Category {
  private Integer id;
  private List<SubCategory> subCategories;


  @OneToMany(
          type = SubCategory.class
          , name = "subCategories")
  public List<SubCategory> getSubCategories() {
    return subCategories;
  }

  public void setSubCategories(List<SubCategory> subCategories) {
    this.subCategories = subCategories;
  }

  public Integer getId(){
    return id;
  }
  public void setId(Integer value){
    this.id=value;
  }
 
}
```
ManyToOne example
```
@Table(name = "subCategory"
        , databaseName = "databaseName")
public class SubCategory {
  private Integer id;
  private String subCategoryName;
  private Integer categoryId;
  private Category Category;

 
  public Category getCategory() {
    return Category;
  }

  @ManyToOne(type = Category.class, name = "category")
  public void setCategory(Category Category) {
    this.Category = Category;
  }

  public Integer getId(){
    return id;
  }
  public void setId(Integer value){
    this.id=value;
  }
  
  public Integer getCategoryId(){
    return categoryId;
  }
  public void setCategoryId(Integer value){
    this.categoryId=value;
  }
}
```







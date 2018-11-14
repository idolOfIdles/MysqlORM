package safayat.orm.model;

import safayat.orm.annotation.OneToMany;
import safayat.orm.annotation.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Person {
  private Integer id;
  private Date creationDate;
  private Date updateDate;
  private Integer age;
  private String firstName;
  private String lastName;
  private String name;

  private List<User> users;


    public Integer getId(){
    return id;
    }
    public void setId(Integer value){
    this.id=value;
    }
    public Date getCreationDate(){
    return creationDate;
    }
    public void setCreationDate(Date value){
    this.creationDate=value;
    }
    public Date getUpdateDate(){
    return updateDate;
    }
    public void setUpdateDate(Date value){
    this.updateDate=value;
    }
    public Integer getAge(){
    return age;
    }
    public void setAge(Integer value){
    this.age=value;
    }
    public String getFirstName(){
    return firstName;
    }
    public void setFirstName(String value){
    this.firstName=value;
    }
    public String getLastName(){
    return lastName;
    }
    public void setLastName(String value){
    this.lastName=value;
    }
    public String getName(){
    return name;
    }
    public void setName(String value){
    this.name=value;
    }

    @OneToMany(name = "users", type = User.class)
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

  @Override
  public String toString() {
    return "Person{" +
            "id=" + id +
            ", creationDate=" + creationDate +
            ", updateDate=" + updateDate +
            ", age=" + age +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", name='" + name + '\'' +
            '}';
  }
}
package safayat.orm.model;

import java.util.Date;

public class Person {
  private Integer id;
  private Date creationDate;
  private Date updateDate;
  private Integer age;
  private String firstName;
  private String lastName;
  private String name;
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
}
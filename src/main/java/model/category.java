package model;

import annotation.OneToMany;
import annotation.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "category", databaseName = "alhelal")
public class Category {
  private Integer id;
  private Date creationDate;
  private Date updateDate;
  private String categoryCode;
  private String categoryName;
  private String description;
  private String status;
  private List<SubCategory> subCategories;

  public Category() {
    subCategories = new ArrayList<SubCategory>();
  }

  @OneToMany(outer = "id"
          , inner = "categoryId"
          , type = SubCategory.class
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
  public String getCategoryCode(){
    return categoryCode;
  }
  public void setCategoryCode(String value){
    this.categoryCode=value;
  }
  public String getCategoryName(){
    return categoryName;
  }
  public void setCategoryName(String value){
    this.categoryName=value;
  }
  public String getDescription(){
    return description;
  }
  public void setDescription(String value){
    this.description=value;
  }
  public String getStatus(){
    return status;
  }
  public void setStatus(String value){
    this.status=value;
  }
}
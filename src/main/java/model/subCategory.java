package model;

import annotation.ManyToOne;
import annotation.OneToMany;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class subCategory{
  private Integer id;
  private Date creationDate;
  private Date updateDate;
  private String description;
  private String status;
  private String subCategoryCode;
  private String subCategoryName;
  private Integer categoryId;
  private category category;

  List<product> productList;

  public subCategory() {
    productList = new ArrayList<product>();
  }

  @OneToMany(outer = "id"
          , inner = "subCategoryId"
          , type = product.class
          , name = "productList")
  public List<product> getProductList() {
    return productList;
  }

  public void setProductList(List<product> productList) {
    this.productList = productList;
  }

  public category getCategory() {
    return category;
  }

  @ManyToOne(inner = "categoryId", outer = "id", type = category.class, name = "category")
  public void setCategory(category category) {
    this.category = category;
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
  public String getSubCategoryCode(){
    return subCategoryCode;
  }
  public void setSubCategoryCode(String value){
    this.subCategoryCode=value;
  }
  public String getSubCategoryName(){
    return subCategoryName;
  }
  public void setSubCategoryName(String value){
    this.subCategoryName=value;
  }
  public Integer getCategoryId(){
    return categoryId;
  }
  public void setCategoryId(Integer value){
    this.categoryId=value;
  }
}
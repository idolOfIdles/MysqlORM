package model;

import annotation.Table;

import java.util.Date;

@Table(name = "product", databaseName = "alhelal")
public class Product {
  private Integer id;
  private Date creationDate;
  private Date updateDate;
  private String description;
  private String productCode;
  private String productName;
  private String status;
  private Integer subCategoryId;
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
  public String getProductCode(){
    return productCode;
  }
  public void setProductCode(String value){
    this.productCode=value;
  }
  public String getProductName(){
    return productName;
  }
  public void setProductName(String value){
    this.productName=value;
  }
  public String getStatus(){
    return status;
  }
  public void setStatus(String value){
    this.status=value;
  }
  public Integer getSubCategoryId(){
    return subCategoryId;
  }
  public void setSubCategoryId(Integer value){
    this.subCategoryId=value;
  }
}
package model;

import java.util.Date;

public class User_role {
  private Integer id;
  private Date creationDate;
  private Date updateDate;
  private Integer roleName;
  private Integer userId;
  private Integer version;
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
  public Integer getRoleName(){
    return roleName;
  }
  public void setRoleName(Integer value){
    this.roleName=value;
  }
  public Integer getUserId(){
    return userId;
  }
  public void setUserId(Integer value){
    this.userId=value;
  }
  public Integer getVersion(){
    return version;
  }
  public void setVersion(Integer value){
    this.version=value;
  }
}
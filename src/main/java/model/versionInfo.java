package model;

import java.util.Date;

public class VersionInfo {
  private String tableName;
  private Date updateDate;
  public String getTableName(){
    return tableName;
  }
  public void setTableName(String value){
    this.tableName=value;
  }
  public Date getUpdateDate(){
    return updateDate;
  }
  public void setUpdateDate(Date value){
    this.updateDate=value;
  }
}
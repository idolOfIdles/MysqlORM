package model;

import java.util.Date;

/**
 * Created by safayat on 10/21/18.
 */
public class Client {

    private int id;
    private java.sql.Date creationDate;
    private String service;
    private String netAmount;
    private String primaryService;
    private String profession;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public java.sql.Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(java.sql.Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getPrimaryService() {
        return primaryService;
    }

    public void setPrimaryService(String primaryService) {
        this.primaryService = primaryService;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

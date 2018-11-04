package safayat.orm.model;

import safayat.orm.annotation.OneToMany;
import safayat.orm.annotation.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private Integer id;
    private Date creationDate;
    private Date updateDate;
    private Date dateOfBirth;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private String password;
    private String status;
    private String userImageUrl;
    private String username;
    private List<Person> persons;

    public User() {
        persons = new ArrayList<>();
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
    public Date getDateOfBirth(){
    return dateOfBirth;
    }
    public void setDateOfBirth(Date value){
    this.dateOfBirth=value;
    }
    public String getEmail(){
    return email;
    }
    public void setEmail(String value){
    this.email=value;
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
    public String getMiddleName(){
    return middleName;
    }
    public void setMiddleName(String value){
    this.middleName=value;
    }
    public String getPassword(){
    return password;
    }
    public void setPassword(String value){
    this.password=value;
    }
    public String getStatus(){
    return status;
    }
    public void setStatus(String value){
    this.status=value;
    }
    public String getUserImageUrl(){
    return userImageUrl;
    }
    public void setUserImageUrl(String value){
    this.userImageUrl=value;
    }
    public String getUsername(){
    return username;
    }
    public void setUsername(String value){
    this.username=value;
    }

    @OneToMany(type = Person.class, name = "persons")
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
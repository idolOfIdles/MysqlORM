package safayat.orm.model;

import safayat.orm.annotation.Table;

@Table(name = "option")
public class Option{
  private Integer id;
  private Integer question_id;
  private String description;

    public Option(Integer question_id, String description) {
        this.question_id = question_id;
        this.description = description;
    }

    public Option() {
    }

    public Integer getId(){
    return id;
  }
  public void setId(Integer value){
    this.id=value;
  }
  public Integer getQuestion_id(){
    return question_id;
  }
  public void setQuestion_id(Integer value){
    this.question_id=value;
  }
  public String getDescription(){
    return description;
  }
  public void setDescription(String value){
    this.description=value;
  }
}
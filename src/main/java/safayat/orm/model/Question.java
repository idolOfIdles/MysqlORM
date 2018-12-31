package safayat.orm.model;

import safayat.orm.annotation.ManyToOne;
import safayat.orm.annotation.OneToMany;
import safayat.orm.model.Option;

import java.util.Date;
import java.util.List;

public class Question{
  private Integer id;
  private Integer questionInfoId;
  private String question;
  private Date createDate;
  private Date updateDate;

  @ManyToOne(nativeColumnName = "questionInfoId", matchingColumnName = "id", name = "questionInfo" , type = QuestionInfo.class)
  QuestionInfo questionInfo;

  @OneToMany(type = Option.class, name = "optionList", matchingColumnName = "question_id", nativeColumnName = "id")
  List<Option> optionList;

  public Integer getId(){
    return id;
  }
  public void setId(Integer value){
    this.id=value;
  }
  public String getQuestion(){
    return question;
  }
  public void setQuestion(String value){
    this.question=value;
  }
  public Date getCreateDate(){
    return createDate;
  }
  public void setCreateDate(Date value){
    this.createDate=value;
  }
  public Date getUpdateDate(){
    return updateDate;
  }
  public void setUpdateDate(Date value){
    this.updateDate=value;
  }

  public Integer getQuestionInfoId() {
    return questionInfoId;
  }

  public void setQuestionInfoId(Integer questionInfoId) {
    this.questionInfoId = questionInfoId;
  }

  public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }

  public QuestionInfo getQuestionInfo() {
    return questionInfo;
  }

  public void setQuestionInfo(QuestionInfo questionInfo) {
    this.questionInfo = questionInfo;
  }
}
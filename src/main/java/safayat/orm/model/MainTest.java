package safayat.orm.model;

import safayat.orm.annotation.OneToMany;
import safayat.orm.config.ConfigManager;
import safayat.orm.crud.Crud;
import safayat.orm.dao.GeneralRepository;
import safayat.orm.query.MysqlQuery;
import safayat.orm.reflect.Util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by safayat on 11/20/18.
 */


public class MainTest {

    public static void main(String[] args) throws Exception {

       /* Question question = new Question();
        question.setQuestion("Why on earth?");
        question.setUpdateDate(new Date());
        question.setCreateDate(new Date());


        List<Option> options = new ArrayList<>();
        Option option = new Option();
        option.setDescription("a");
        options.add(option);

        option = new Option();
        option.setDescription("b");
        options.add(option);

        option = new Option();
        option.setDescription("c");
        options.add(option);

        option = new Option();
        option.setDescription("d");
        options.add(option);

        question.setOptionList(options);

        Crud.save(question);

        question.setQuestion(question.getQuestion() + " " + System.currentTimeMillis());
        for(Option option1 : question.getOptionList()){
            option1.setDescription(option1.getDescription() + " " + System.currentTimeMillis());
        }

        Crud.save(question);
*/

           List<Question> questionList = MysqlQuery.All()
                    .table(Question.class, "qt")
                    .join("online_exam.option", "ot")
                    .on("qt.id","ot.question_id")
                    .join(QuestionInfo.class, "qi")
                    .on("qt.questionInfoId", "qi.id")
                    .filter("qt.id=", 1)
                    .toList(Question.class);

           for (Question q : questionList){
                   System.out.println(q.getQuestion());
                   System.out.println(q.getOptionList().size());
                   System.out.println(q.getQuestionInfo().getStatus());
                   q.setQuestion("Why on earth changed so much?");
                   for(Option option : q.getOptionList()){
                        option.setDescription(option.getDescription() + " " + 100);
                   }
                   q.getQuestionInfo().setYear(2087);
                   Crud.save(q);
           }












    }


}

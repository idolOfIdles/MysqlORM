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

        ExamQuestion question = new ExamQuestion();
        question.setQuestion("Who built the labyrinth for Minotor?");
        question.setUpdateDate(new Date());
        question.setCreateDate(new Date());


        List<Option> options = new ArrayList<>();
        Option option = new Option();
        option.setDescription("Deodellus");
        options.add(option);

        option = new Option();
        option.setDescription("Ekarus");
        options.add(option);

        option = new Option();
        option.setDescription("Orfeus");
        options.add(option);

        option = new Option();
        option.setDescription("Hephasteus");
        options.add(option);

        question.setOptionList(options);

        Crud.save(question);

        Answer answer = new Answer();
        answer.setQt_id(question.getId());
        answer.setOp_id(question.getOptionList()
                .stream()
                .filter(o->o.getDescription().equalsIgnoreCase("Deodellus"))
                .map(o->o.getId())
                .findAny()
                .get());

        Crud.insert(answer);


        List<ExamQuestion> questions = MysqlQuery
                                        .All()
                                        .manyToMany(ExamQuestion.class, Option.class)
                                        .toList(ExamQuestion.class);

        questions.forEach(q->{
            System.out.println(q.optionList.size());
        });











    }


}

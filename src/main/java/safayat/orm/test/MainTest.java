package safayat.orm.test;

import safayat.orm.dao.GeneralRepository;
import safayat.orm.model.Person;

import java.util.List;

/**
 * Created by safayat on 11/14/18.
 */
public class MainTest {

    public static void main(String[] args){
        GeneralRepository generalRepository = new GeneralRepository();
        List<Person> personList = generalRepository.getAll(Person.class, 2, 0);
        for(Person person : personList){
            System.out.println(person);
        }

    }

}

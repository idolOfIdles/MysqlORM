package safayat.orm.crud;

import safayat.orm.dao.GeneralRepositoryManager;

import java.sql.Connection;
import java.util.List;

/**
 * Created by safayat on 11/20/18.
 */


public class Crud {

    public Crud() {

    }

    public UpdateQuery update(String table){
        return new UpdateQuery(table);
    }


    public static void update(Object value) throws Exception {
        GeneralRepositoryManager.getInstance().getGeneralRepository().update(value);
    }
    public static void update(List<Object> values) throws Exception {
        GeneralRepositoryManager.getInstance().getGeneralRepository().update(values);
    }
    public static void update(Object value, Connection connection) throws Exception {
        GeneralRepositoryManager.getInstance().getGeneralRepository().update(value, connection);
    }
    public static void update(List<Object> values, Connection connection) throws Exception {
        GeneralRepositoryManager.getInstance().getGeneralRepository().update(values, connection);
    }

    public static  <T> void save(T t)throws Exception{
        GeneralRepositoryManager.getInstance().getGeneralRepository().insertOrUpdate(t);
    }
    public static  <T> void save(T t, Connection connection)throws Exception{
        GeneralRepositoryManager.getInstance().getGeneralRepository().insertOrUpdate(t, connection);
    }
    public static  <T> void insert(T t)throws Exception{
        GeneralRepositoryManager.getInstance().getGeneralRepository().insert(t);
    }

    public static  <T> void insert(T t, Connection connection) throws Exception {
        GeneralRepositoryManager.getInstance().getGeneralRepository().insert(t, connection);
    }

    public static  <T> int[] insert(List<T> objects)throws Exception{
        return GeneralRepositoryManager.getInstance().getGeneralRepository().insert(objects);
    }

    public static  <T> int[] insert(List<T> objects, Connection connection) throws Exception {
        return GeneralRepositoryManager.getInstance().getGeneralRepository().insert(objects, connection);
    }

}

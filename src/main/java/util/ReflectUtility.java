package util;

import annotation.ManyToOne;
import annotation.OneToMany;
import jdbcUtility.ResultSetMetadataUtility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

/**
 * Created by safayat on 10/22/18.
 */
public class ReflectUtility {

    public static <P,C> void mapRelation(Annotation annotation, P parent, C child  ) throws Exception{

        if(annotation instanceof ManyToOne){
            ManyToOne oneToMany = (ManyToOne) annotation;
            Class type = oneToMany.type();
            ReflectUtility.mapColumn(parent,oneToMany.name(),type, child);
        }else if( annotation instanceof  OneToMany){
            OneToMany oneToMany = (OneToMany) annotation;
            List list = (List)ReflectUtility.getValueFromObject(parent, oneToMany.name());
            list.add(child);
        }

    }

    public static <T> T mapRow(ResultSet rs, List<Integer> columnIndexes, Class<T> clazz) throws Exception{

        T newClazz = clazz.newInstance();
        for(int index : columnIndexes){
            String columnName = rs.getMetaData().getColumnName(index);
            int columnType = rs.getMetaData().getColumnType(index);
            try {
                mapColumn(newClazz, columnName, Util.getClassByType(columnType) , rs.getObject(index));
            }catch (Exception e){}
        }
        return newClazz;

    }

    public static <T> void mapColumn(T row, String columnName, Class columnType, Object value) throws Exception{

        String methodName = Util.toJavaMethodName(columnName, "set");
        Method method = row.getClass().getDeclaredMethod(methodName, columnType);
        if(method!=null){
            method.invoke(row,value);
        }

    }

    public static <T> Object getValueFromObject(T t, String name) throws Exception{

        String methodName = Util.toJavaMethodName(name, "get");
        Method method = t.getClass().getDeclaredMethod(methodName);
        if(method!=null){
            return method.invoke(t);
        }
        return null;
    }

    public static Map<String, Annotation> getAnnotationByTable(Class clazz) throws Exception{
        Map<String, Annotation> annotationByTable = new HashMap<String, Annotation>();
        List<Annotation> annotationList = Util.getMethodAnnotations(clazz);
        for(Annotation annotation : annotationList){
            if( annotation instanceof OneToMany){
                OneToMany oneToMany = (OneToMany) annotation;
                String type = oneToMany.type().getSimpleName();
                annotationByTable.put(type, annotation);
            }
            if( annotation instanceof ManyToOne){
                ManyToOne manyToOne = (ManyToOne)annotation;
                String type = manyToOne.type().getSimpleName();
                annotationByTable.put(type, annotation);
            }
        }
        return annotationByTable;

    }

    public static String getUniqueKeyFromAnnotation(Class clazz) throws Exception{
        List<Annotation> annotationList = Util.getMethodAnnotations(clazz);
        for(Annotation annotation : annotationList){
            if( annotation instanceof OneToMany){
                OneToMany oneToMany = (OneToMany) annotation;
                return oneToMany.outer();
            }
        }
        return "";

    }

    public static int getParentUniqueColumnIndex(ResultSetMetadataUtility resultSetMetadataUtility, Class clazz) throws Exception{
        String parentClassUniqueField = ReflectUtility.getUniqueKeyFromAnnotation(clazz);
        int parentClassUniqueFieldIndex = 0;
        if(!parentClassUniqueField.isEmpty()){
            parentClassUniqueFieldIndex = resultSetMetadataUtility.getColumnIndex(clazz.getSimpleName(), parentClassUniqueField);
        }
        return parentClassUniqueFieldIndex;

    }

}

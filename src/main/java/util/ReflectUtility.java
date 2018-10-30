package util;

import annotation.ManyToOne;
import annotation.OneToMany;
import config.ConfigManager;
import jdbcUtility.ResultSetMetadataUtility;
import jdbcUtility.ResultSetUtility;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by safayat on 10/22/18.
 */
public class ReflectUtility {

    public static <P,C> void mapRelation(Annotation annotation, P parent, C child  ) throws Exception{

        if(annotation instanceof ManyToOne){
            ManyToOne oneToMany = (ManyToOne) annotation;
            Class type = oneToMany.type();
            ReflectUtility.mapValue(parent,oneToMany.name(),type, child);
        }else if( annotation instanceof  OneToMany){
            OneToMany oneToMany = (OneToMany) annotation;
            List list = (List)ReflectUtility.getValueFromObject(parent, oneToMany.name());
            list.add(child);
        }

    }

    public static <T> void mapValue(T row, String columnName, Class columnType, Object value) throws Exception{

        String methodName = Util.toJavaMethodName(columnName, "set");
        Method method = row.getClass().getDeclaredMethod(methodName, columnType);
        if(method!=null){
            method.invoke(row, value);
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

    public static void populateDescentAnnotations(Class clazz, Map<String, Class> visited, Map<Class, RelationAnnotationInfo> parentMap) throws Exception{
        visited.put(clazz.getSimpleName().toLowerCase(), clazz);
        List<Annotation> annotationList = Util.getMethodAnnotations(clazz);
        for(Annotation annotation : annotationList){
            Class type = null;
            if( annotation instanceof OneToMany){
                OneToMany oneToMany = (OneToMany) annotation;
                type = oneToMany.type();
            }
            else if( annotation instanceof ManyToOne){
                ManyToOne manyToOne = (ManyToOne)annotation;
                type = manyToOne.type();
            }
            if(type != null
                    && visited.containsKey(type.getSimpleName().toLowerCase()) == false){
                parentMap.put(type, new RelationAnnotationInfo(annotation, clazz));
                populateDescentAnnotations(type, visited, parentMap);
            }

        }
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

    public static List<Method> getParsedGetMethods(Class clazz){
        return Stream.of(clazz.getDeclaredMethods())
                .filter(m->m.getName()
                    .startsWith("get"))
                            .collect(Collectors.toList());

    }

    public static String createInsertValueSqlString(Object o){
        List<Method> getMethods = getParsedGetMethods(o.getClass());
        List<String> variableNames = getMethods
                .stream()
                    .map(m -> Util.methodToVariableName(m.getName()))
                                                            .collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder("Insert into ")
                                        .append(ConfigManager.getInstance()
                                                .getTableName(o.getClass()))
                                                    .append("(")
                                                        .append(Util.listAsString(variableNames))
                                                            .append(") values(");



        StringBuilder values = new StringBuilder();

        for(int i=0;i<getMethods.size();i++){
            Method method = getMethods.get(i);
            try {
                values.append(method.invoke(o).toString());
            } catch (Exception e) {
                values.append("");
            }
            if(i<getMethods.size()-1)values.append(",");
        }
        values.append(")");
        return stringBuilder.toString();


    }


}

package safayat.orm.reflect;

import safayat.orm.annotation.ManyToOne;
import safayat.orm.annotation.OneToMany;
import safayat.orm.annotation.Transient;
import safayat.orm.config.ConfigManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by safayat on 10/22/18.
 */
public class ReflectUtility {

    public static <P,C> void mapRelation(Annotation annotation, P parent, C child  ) throws Exception{

        if(annotation instanceof ManyToOne){
            ManyToOne manyToOne = (ManyToOne) annotation;
            Class type = manyToOne.type();
            ReflectUtility.mapValue(parent, manyToOne.name(), type, child);
        }else if( annotation instanceof  OneToMany){
            OneToMany oneToMany = (OneToMany) annotation;
            List<C> list = (List)ReflectUtility.getValueFromObject(parent, oneToMany.name());
            if(list == null){
                list = new ArrayList<>();
                ReflectUtility.mapValue(parent, oneToMany.name(), List.class, list );
            }
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

    public static <T> void mapValue(T row, String columnName,Object value) throws Exception{

        Field field = row.getClass().getDeclaredField(columnName);
        String methodName = Util.toJavaMethodName(columnName, "set");
        Method method = row.getClass().getDeclaredMethod(methodName, field.getType());
        if(method!=null){
            method.invoke(row, value);
        }

    }


    public static Object getValueFromObject(Object t, String name) throws Exception{

        String methodName = Util.toJavaMethodName(name, "get");
        Method method = t.getClass().getDeclaredMethod(methodName);
        if(method!=null){
            return method.invoke(t);
        }
        return null;
    }


    public static Map<String, Annotation> getAnnotationByTable(Class clazz) throws Exception{
        Map<String, Annotation> annotationByTable = new HashMap<String, Annotation>();
        List<Annotation> annotationList = Util.getFieldAnnotations(clazz);
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


    public static void populateDescentAnnotations(Class clazz, Map<String
            , Class> visited, Map<Class, RelationAnnotationInfo> parentMap) throws Exception{
        visited.put(clazz.getSimpleName().toLowerCase(), clazz);
        List<Annotation> annotationList = Util.getFieldAnnotations(clazz);
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

    public static boolean isTableFieldMethod(Method method){
        return !(
                method.isAnnotationPresent(OneToMany.class)
                || method.isAnnotationPresent(ManyToOne.class)
                || method.isAnnotationPresent(Transient.class));
    }

    public static boolean isTableField(Field field){
        return !(
                field.isAnnotationPresent(OneToMany.class)
                || field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(Transient.class));
    }

    public static List<Method> getParsedGetMethods(Class clazz){

        List<Method> parsedGetMethods = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for(Field f : fields){
            if(isTableField(f)){
                try {
                    Method method = clazz.getDeclaredMethod(Util.toJavaMethodName(f.getName(), "get"));
                    parsedGetMethods.add(method);
                } catch (NoSuchMethodException e) {

                }
            }
        }
        return parsedGetMethods;
    }

    public static String createInsertSqlString(Object o){
        List<Method> getMethods = getParsedGetMethods(o.getClass());
        List<String> variableNames = new ArrayList<>();
        for(Method m : getMethods){
            variableNames.add(Util.methodToVariableName(m.getName()));
        }
        StringBuilder stringBuilder
                = new StringBuilder("insert into ")
                    .append(ConfigManager.getInstance().getDbName()
                            + "." +ConfigManager.getInstance().getTableName(o.getClass()))
                        .append("(")
                            .append(Util.listAsString(variableNames))
                                .append(") values(");

        for(int i=0;i<getMethods.size();i++){
            Method method = getMethods.get(i);
            try {
                stringBuilder.append(Util.toMysqlString(method.invoke(o)));
            } catch (Exception e) {
                stringBuilder.append("NULL");
            }
            if(i<getMethods.size()-1)stringBuilder.append(",");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();


    }

    public static String createSingleRowUpdateSqlString(Object o, List<String> primaryKeys) throws Exception{
        if(primaryKeys.size() == 0) throw new Exception("Primary key/value not found");
        List<Method> getMethods = getParsedGetMethods(o.getClass());
        StringBuilder stringBuilder
                = new StringBuilder("update ")
                .append(ConfigManager.getInstance().getDbName()
                        + "." +ConfigManager.getInstance().getTableName(o.getClass()))
                .append(" set ");

        for(int i=0;i<getMethods.size();i++){
            Method method = getMethods.get(i);
            String columnName = Util.methodToVariableName(method.getName());
            try {
                if(primaryKeys.indexOf(columnName) < 0){
                    stringBuilder
                            .append(columnName).append("=")
                            .append(Util.toMysqlString(method.invoke(o)));
                    stringBuilder.append(",");
                }
            } catch (Exception e) {
            }
        }
        if(stringBuilder.charAt(stringBuilder.length()-1) == ',')
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append(" WHERE ")
                .append(createFilterByPrimaryKeySqlCondition(o, primaryKeys));

        return stringBuilder.toString();


    }

    public static String createFilterByPrimaryKeySqlCondition(Object row, List<String> primaryKeys) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<primaryKeys.size();i++){
            String keyName = primaryKeys.get(i);
            Object value = ReflectUtility.getValueFromObject(row, keyName);
            stringBuilder.append(keyName).append("=").append(Util.toMysqlString(value));
            if(i<primaryKeys.size()-1){
                stringBuilder.append("AND");
            }
        }
        return stringBuilder.toString();
    }

    public static boolean haveOneToManyRelationData(Object row) throws Exception {
        Map<String,Annotation> annotationByTable = ReflectUtility.getAnnotationByTable(row.getClass());
        for(Annotation annotation : annotationByTable.values()){
            if( annotation instanceof OneToMany){
                OneToMany oneToMany = (OneToMany) annotation;
                List list = (List)ReflectUtility.getValueFromObject(row, oneToMany.name());
                if(list.isEmpty() == false) return true;
            }
        }
        return false;
    }

    public static boolean haveOneToManyRelationInfo(OneToMany oneToMany) throws Exception {
       return !(oneToMany.matchingColumnName().trim().isEmpty()
               || oneToMany.nativeColumnName().trim().isEmpty());

    }

}

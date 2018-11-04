package safayat.orm.reflect;

import safayat.orm.annotation.ManyToOne;
import safayat.orm.annotation.OneToMany;
import safayat.orm.annotation.Table;
import safayat.orm.annotation.Transient;
import safayat.orm.config.ConfigManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
            ReflectUtility.mapValue(parent, oneToMany.name(), type, child);
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

    public static boolean isTableFieldMethod(Method method){
        return !(
                method.isAnnotationPresent(OneToMany.class)
                || method.isAnnotationPresent(ManyToOne.class)
                || method.isAnnotationPresent(Transient.class));
    }
    public static List<Method> getParsedGetMethods(Class clazz){
        return Stream.of(clazz.getDeclaredMethods())
                .filter(m -> m.getName().startsWith("get") && isTableFieldMethod(m))
                    .collect(Collectors.toList());
    }

    public static String createInsertSqlString(Object o){
        List<Method> getMethods = getParsedGetMethods(o.getClass());
        List<String> variableNames = getMethods
                .stream()
                    .map(m -> Util.methodToVariableName(m.getName()))
                        .collect(Collectors.toList());
        StringBuilder stringBuilder
                = new StringBuilder("insert into ")
                    .append(ConfigManager.getInstance()
                            .getTableName(o.getClass()))
                                .append("(")
                                    .append(Util.listAsString(variableNames))
                                        .append(") values(");

        for(int i=0;i<getMethods.size();i++){
            Method method = getMethods.get(i);
            try {
                stringBuilder.append(Util.toQuote(method.invoke(o).toString()));
            } catch (Exception e) {
                stringBuilder.append("NULL");
            }
            if(i<getMethods.size()-1)stringBuilder.append(",");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();


    }

    public static String createSingleRowUpdateSqlString(Object o) throws Exception{
        Table table = o.getClass().getAnnotation(Table.class);
        List<Method> getMethods = getParsedGetMethods(o.getClass());
        StringBuilder stringBuilder
                = new StringBuilder("update ")
                .append(ConfigManager.getInstance().getTableName(o.getClass()))
                .append(" set ");

        String primaryKeyValue = null;
        for(int i=0;i<getMethods.size();i++){
            Method method = getMethods.get(i);
            String columnName = Util.methodToVariableName(method.getName());
            try {
                Object value = Util.toQuote(method.invoke(o).toString());
                if(columnName.equals(table.primaryKey())){
                    primaryKeyValue = value.toString();
                }else {
                    stringBuilder
                            .append(columnName).append("=")
                            .append(Util.toQuote(method.invoke(o).toString()));
                    if(i<getMethods.size()-1)stringBuilder.append(",");
                }
            } catch (Exception e) {
            }
        }
        if(primaryKeyValue == null) throw new Exception("Primary key/value not found");
        stringBuilder.append(" WHERE ")
                .append(table.primaryKeyColumn())
                .append("=")
                .append(primaryKeyValue);
        return stringBuilder.toString();


    }



}

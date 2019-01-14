package safayat.orm.reflect;

import safayat.orm.annotation.ManyToMany;
import safayat.orm.annotation.ManyToOne;
import safayat.orm.annotation.OneToMany;
import safayat.orm.annotation.Transient;
import safayat.orm.config.ConfigManager;
import safayat.orm.jdbcUtility.TableMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by safayat on 10/22/18.
 */
public class ReflectUtility {

    public static <P,C> void mapRelation(RelationInfo relation, P parent, C child  ) throws Exception{

        if(relation.isRelationAnnotation()){
            Class type = relation.getFieldType();
            if(relation.isManyToOne()){
                ReflectUtility.mapValue(parent, relation.getFieldName(), type, child);
            }else {
                List<C> list = (List)ReflectUtility.parseFieldValueFromObject(parent, relation.getFieldName());
                if(list == null){
                    list = new ArrayList<>();
                    ReflectUtility.mapValue(parent, relation.getFieldName(), List.class, list );
                }
                list.add(child);

            }
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
        mapValue(row, columnName, field.getType(), value);
    }


    public static Object parseFieldValueFromObject(Object t, String name) throws Exception{

        String methodName = Util.toJavaMethodName(name, "get");
        Method method = t.getClass().getDeclaredMethod(methodName);
        if(method!=null){
            return method.invoke(t);
        }
        return null;
    }


    public static boolean isTableField(Field field){
        return !(
                field.isAnnotationPresent(OneToMany.class)
                || field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(ManyToMany.class)
                || field.isAnnotationPresent(Transient.class));
    }

    public static boolean isRelationAnnotation(Annotation a){
        return a instanceof OneToMany
                || a instanceof ManyToMany
                || a instanceof ManyToOne;
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
                            + "." + TableMetadata.getTableName(o.getClass()))
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



    public static String createSingleRowUpdateSqlString(Object o) throws Exception{

        List<String> primaryKeys = ConfigManager.getInstance()
                                        .getTableMetadata(o.getClass())
                                        .getPrimaryKeysAsList();
        if(primaryKeys == null || primaryKeys.size() == 0) throw new Exception("Primary key/value not found");
        List<Method> getMethods = getParsedGetMethods(o.getClass());
        StringBuilder stringBuilder
                = new StringBuilder("update ")
                .append(ConfigManager.getInstance().getDbName()
                        + "." + TableMetadata.getTableName(o.getClass()))
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

    public static boolean isPrimaryKeyEmpty(Object row, String primaryKey) throws Exception {
        return ReflectUtility.parseFieldValueFromObject(row, primaryKey) == null;
    }

    public static String createFilterByPrimaryKeySqlCondition(Object row, List<String> primaryKeys) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<primaryKeys.size();i++){
            String keyName = primaryKeys.get(i);
            Object value = ReflectUtility.parseFieldValueFromObject(row, keyName);
            stringBuilder.append(keyName).append("=").append(Util.toMysqlString(value));
            if(i<primaryKeys.size()-1){
                stringBuilder.append("AND");
            }
        }
        return stringBuilder.toString();
    }


    public static boolean haveOneToManyRelationInfo(OneToMany oneToMany) throws Exception {
       return !(oneToMany.matchingColumnName().trim().isEmpty()
               || oneToMany.nativeColumnName().trim().isEmpty());

    }

    public static boolean haveManyToManyRelationInfo(ManyToMany manyToMany) throws Exception {
       return !(manyToMany.matchingColumnName().trim().isEmpty()
               || manyToMany.nativeColumnName().trim().isEmpty()
               || manyToMany.relationTable().trim().isEmpty()
               || manyToMany.matchingRelationColumnName().trim().isEmpty()
               || manyToMany.nativeRelationColumnName().trim().isEmpty()
       );

    }

    public static boolean haveManyToOneRelationInfo(ManyToOne manyToOne) throws Exception {
       return !(manyToOne.matchingColumnName().trim().isEmpty()
               || manyToOne.nativeColumnName().trim().isEmpty());

    }

    public static Object getColumnFromResultByGivenType(Class type, ResultSet resultSet, int index) throws SQLException {
        if(type.getSimpleName().equalsIgnoreCase("int") || type.getSimpleName().equalsIgnoreCase(Integer.class.getSimpleName())){
            return resultSet.getInt(index);
        }
        if(type.getSimpleName().equalsIgnoreCase("long") || type.getSimpleName().equalsIgnoreCase(Long.class.getSimpleName())){
            return resultSet.getLong(index);
        }
        if(type.getSimpleName().equalsIgnoreCase("float") || type.getSimpleName().equalsIgnoreCase(Float.class.getSimpleName())){
            return resultSet.getFloat(index);
        }
        if(type.getSimpleName().equalsIgnoreCase("double") || type.getSimpleName().equalsIgnoreCase(Double.class.getSimpleName())){
            return resultSet.getDouble(index);
        }
        if(type.getSimpleName().equalsIgnoreCase("byte") || type.getSimpleName().equalsIgnoreCase(Byte.class.getSimpleName())){
            return resultSet.getByte(index);
        }
        if(type.getSimpleName().equalsIgnoreCase(String.class.getSimpleName())){
            return resultSet.getString(index);
        }
        if(type.getSimpleName().equalsIgnoreCase(Date.class.getSimpleName())){
            return resultSet.getDate(index);
        }
        return null;

    }

    public static RelationInfo getRelationAnnotation(Class parent, Class annotationType, Class childType) {
       return Util.getFieldAnnotations(parent, annotationType).stream()
              .map(a -> new RelationInfo(a, parent))
              .filter(r -> r.getFieldType() == childType)
              .findFirst()
              .get();
    }

    public static List<RelationInfo> getRelationAnnotations(Class tableClass) {
       return Util.getFieldAnnotations(tableClass)
              .stream()
               .filter(r -> isRelationAnnotation(r))
               .map(a -> new RelationInfo(a, tableClass))
               .collect(Collectors.toList());
    }



}

package safayat.orm.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by safayat on 10/22/18.
 */
public class Util {

    public static DateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String toCamelCase(String str){
        StringBuilder stringBuilder = new StringBuilder(str);
        int  ch = str.charAt(0);
        if(ch >= 'a'){
            stringBuilder.setCharAt(0, (char)(str.charAt(0) & 0x5f));
        }
        return stringBuilder.toString();

    }

    public static String classNameToTable(String str){
        return String.valueOf(str.charAt(0)).toLowerCase() + str.substring(1);

    }

    public static String mysqlFieldtoJavaVariableName(String str){
        String[] splitted = str.split("[_]+");
        StringBuilder stringBuilder = new StringBuilder(splitted[0]);
        for(int i=1;i<splitted.length;i++){
            stringBuilder.append(toCamelCase(splitted[1]));
        }
        return stringBuilder.toString();
    }

    public static String mysqlTabletoJavaClassName(String str){
        String camelCaseName = mysqlFieldtoJavaVariableName(str);
        return camelCaseName.substring(0,1).toUpperCase() + camelCaseName.substring(1);
    }
    public static String toTitle(String str){
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
    public static String toJavaMethodName(String variableName, String prefix){
        return prefix + toTitle(variableName);
    }

    public static List<Annotation> getMethodAnnotations(Class clazz) {

        List<Annotation> list = new ArrayList<Annotation>();

        Method[] methods = clazz.getDeclaredMethods();
        for(Method m : methods){
            for (Annotation annotation : m.getDeclaredAnnotations()){
                list.add(annotation);
            }
        }

        return list;

    }
    public static Class getClassByMysqlType(int type) {
        if(Types.BIGINT == type) return Long.class;
        if(Types.BINARY == type) return Boolean.class;
        if(Types.INTEGER == type) return Integer.class;
        if(Types.DATE == type) return Date.class;
        if(Types.TIMESTAMP == type) return Date.class;
        if(Types.TIME == type) return java.sql.Time.class;
        if(Types.BLOB == type) return java.sql.Blob.class;
        if(Types.FLOAT == type) return Float.class;
        if(Types.DOUBLE == type) return Double.class;
        if(Types.ARRAY == type) return Array.class;
        return String.class;
    }

    public static String methodToVariableName(String methodName) {
        int from = 0;
        if(methodName.startsWith("get")) from = 3;
        return String.valueOf(methodName.charAt(from)).toLowerCase() + methodName.substring(from+1);
    }

    public static String listAsString(List list) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<list.size();i++){
            stringBuilder.append(list.get(i));
            if(i<list.size()-1) stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    public static String toQuote(String str) {
        return "\"" + str + "\"";
    }

    public static String toMysqlString(Object ob) {

        if(ob instanceof Date){
            return toQuote(mysqlDateFormat.format((Date)ob));
        }

        if(ob == null){
            return "NULL";
        }

        return toQuote(ob.toString());
    }




}

package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by safayat on 10/22/18.
 */
public class Util {

    public static String toCamelCase(String str){
        StringBuilder stringBuilder = new StringBuilder(str);
        int  ch = str.charAt(0);
        if(ch >= 'a'){
            stringBuilder.setCharAt(0, (char)(str.charAt(0) & 0x5f));
        }
        return stringBuilder.toString();

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
        if(type == Types.BIGINT) return Long.class;
        if(type == Types.INTEGER) return Integer.class;
        if(type == Types.BINARY) return Boolean.class;
        if(type == Types.DATE) return Date.class;
        if(type == Types.FLOAT) return Float.class;
        if(type == Types.DOUBLE) return Double.class;
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

}

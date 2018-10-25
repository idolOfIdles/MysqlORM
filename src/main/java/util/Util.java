package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

}

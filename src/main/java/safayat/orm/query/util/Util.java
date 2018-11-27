package safayat.orm.query.util;

import java.lang.String;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by safayat on 11/18/18.
 */
public class Util {

    public static DateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static char upper(char c){
        return  c >= 'a' && c <= 'z' ? (char)(c - ( 'a'-'A')) : c;
    }

    public static char lower(char c){
        return  c >= 'A' && c <= 'Z' ? (char)( c + ( 'a'-'A')) : c;
    }

    public static String tableName(Class clazz){
        char[] chars = clazz.getSimpleName().toCharArray();
        chars[0] = lower(chars[0]);
        return new String(chars);
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

    public static String toQuote(String str) {
        return "\"" + str + "\"";
    }

    public static String toTitle(String str) {
        char[] chars = str.toCharArray();
        chars[0] = upper(chars[0]);
        return new String(chars);
    }

    public static void removeLastCharacter(StringBuilder stringBuilder) {
        if(stringBuilder.length()>0) stringBuilder.deleteCharAt(stringBuilder.length()-1);
    }

    public static void rightStripIfExists(StringBuilder stringBuilder, char suffix) {
        if(stringBuilder.charAt(stringBuilder.length()-1) == suffix) removeLastCharacter(stringBuilder);
    }




}

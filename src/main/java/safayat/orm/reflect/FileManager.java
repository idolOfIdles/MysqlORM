package safayat.orm.reflect;

import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by safayat on 10/22/18.
 */
public class FileManager {

    static Class thisClazz = FileManager.class;
    static Logger logger = Logger.getLogger(thisClazz.getName());


    public static void write(String filePath, String fileString){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new java.io.FileWriter(filePath));

            writer.write(fileString);


            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static URL getDirectoryUrl(){
        String me = thisClazz.getName().replace(".", "/")+".class";
        return thisClazz.getClassLoader().getResource(me);
    }

    private static Map<String, String> getPackagePaths(String[] packageNames) throws Exception{

        Map<String, String> packagePathMap = new HashMap<>();
        for(String packageName : packageNames){
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = thisClazz.getClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                packagePathMap.put(packageName, resource.getFile().replaceAll("file:", "").replaceAll("!", ""));
            }
        }
        return packagePathMap;

    }

    private static boolean isJarFile(){
        return getDirectoryUrl().getProtocol().equals("jar");
    }

    private static String getJarPath(){
        String path = getDirectoryUrl().getPath();
        return path.substring(5, path.indexOf("!"));
    }

    private static Map<String, String> createJarEntriesMap() throws Exception{
        Map<String, String> jarEntryMap = new HashMap<>();
        String jarPath = getJarPath();
        logger.log(Level.INFO, "jarpath :" + jarPath);
        JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
        Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
        while(entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            jarEntryMap.put(name, name) ;
        }
        return jarEntryMap;
    }


    public static Class[] getClasses(String packageNamesAsString)
            throws Exception {


        Map<String, String> packagePathMap = getPackagePaths(packageNamesAsString.split(","));
        ArrayList<Class> classes = new ArrayList();

        boolean isJarFile = isJarFile();
        Iterator<String> itr = packagePathMap.keySet().iterator();
        Map<String, String> jarEntryMap = new HashMap<>();
        if(isJarFile){
            jarEntryMap = createJarEntriesMap();
        }
        while (itr.hasNext()){
            String packageName = itr.next();
            String path = packagePathMap.get(packageName);
            if(isJarFile){
                List<String> urlList = getResourceListing(path, jarEntryMap);
                for (String url : urlList) {
                    logger.log(Level.INFO, "class name:" + packageName + url);
                    classes.add(Class.forName(packageName + url));
                }

            }else {
                classes.addAll(findClasses(new File(path), packageName));
            }
        }

        return classes.toArray(new Class[classes.size()]);
    }

    private static List<String> getResourceListing(
            String path, Map<String, String> jarEntryMap) throws URISyntaxException, IOException {

        URL dirURL = getDirectoryUrl();
        logger.log(Level.INFO, dirURL.getPath());

        /* A JAR path */
            String jarPath = getJarPath(); //strip out only the JAR file

            List<String> result = new ArrayList<>(); //avoid duplicates in case it is a subdirectory

            Iterator<String> itr = jarEntryMap.keySet().iterator();
            while(itr.hasNext()) {
                String name = jarPath + "/" + itr.next();

                if (name.endsWith(".class") && name.startsWith(path)) { //filter according to the path
                    logger.log(Level.INFO, "full class path:" + name);
                    String className = name.substring(path.length());
                    result.add(className.replaceAll("/", "\\.").substring(0, className.length() - 6));
                }
            }
            return result;

    }


    private static List findClasses(File directory, String packageName) throws ClassNotFoundException {
        List classes = new ArrayList();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

}

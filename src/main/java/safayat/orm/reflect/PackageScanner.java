package safayat.orm.reflect;

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

/**
 * Created by safayat on 10/22/18.
 */
public class PackageScanner {

    static Logger logger = Logger.getLogger(PackageScanner.class.getName());
    private String packageNames;
    private URL directoryUrl;
    private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();;

    public PackageScanner(String packageName) {
        this.packageNames = packageName;
        directoryUrl = getDirectoryUrl(packageName);
    }

    private URL getDirectoryUrl(String packageName){

        try {
            String firstPackageName = packageName == null ? "" : packageName.split(",")[0].trim();
            String path = firstPackageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                return resources.nextElement();
            }

        }catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage());
        }
        return null;
    }

    private  Map<String, String> getPackagePaths() throws Exception{

        Map<String, String> packagePathMap = new HashMap<>();
        for(String packageName : packageNames.split(",")){
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                packagePathMap.put(packageName, resource.getFile().replaceAll("file:", "").replaceAll("!", ""));
            }
        }
        return packagePathMap;

    }

    private boolean isJarFile(){
        return directoryUrl.getProtocol().equals("jar");
    }

    private String getJarPath(){
        String path = directoryUrl.getPath();
        return path.substring(5, path.indexOf("!"));
    }

    private  Map<String, String> createJarEntriesMap() throws Exception{
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


    public  Class[] getClasses()
            throws Exception {


        Map<String, String> packagePathMap = getPackagePaths();
        ArrayList<Class> classes = new ArrayList();

        boolean isJarFile = isJarFile();
        logger.log(Level.INFO,"isJarFile: " + isJarFile);
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

    private  List<String> getResourceListing(
            String path, Map<String, String> jarEntryMap) throws URISyntaxException, IOException {

        URL dirURL = directoryUrl;
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


    private List findClasses(File directory, String packageName) throws ClassNotFoundException {
        logger.log(Level.INFO, "in classes");
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
                logger.log(Level.INFO, "fileName: " + file.getName());
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

}

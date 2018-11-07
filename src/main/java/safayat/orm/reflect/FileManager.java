package safayat.orm.reflect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by safayat on 10/22/18.
 */
public class FileManager {


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

    public static Class[] getClasses(String packageNamesAsString)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        ArrayList<Class> classes = new ArrayList();
        String[] packageNames = packageNamesAsString.split(",");
        for(String packageName : packageNames){
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(URLDecoder.decode(resource.getFile(), "UTF-8")));
            }
            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName));
            }
        }
        return classes.toArray(new Class[classes.size()]);
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

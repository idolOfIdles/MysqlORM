package util;

import java.io.BufferedWriter;
import java.io.IOException;

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



}

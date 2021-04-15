/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author lambo
 */
public class CSVConverter {
        
/**
 * This method writes a text file based on the text going in, its file destination and path.
 * @param text
 * @param fileDestination
 * @param filePath
 * @return 
 */
    public boolean writeTextFile(String text,String filePath, String fileDestination) {
        if(!fileDestination.endsWith(".csv"))fileDestination+=".csv";
        try {
            Files.createDirectories(Paths.get(filePath));
            
            java.io.FileWriter fw = new java.io.FileWriter(fileDestination);
            BufferedWriter bw = new BufferedWriter(fw);
            
            bw.write(text);
            bw.flush();
            
            bw.close();
        } catch (IOException e) {
            System.out.println("Error Writing Stream");
            
            e.printStackTrace();
            return false;
            
        } catch (Exception e) {
            
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

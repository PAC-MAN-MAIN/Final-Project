/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SaraRomero
 */ 
public class CSVFile {
    private  final String delimiter = "|";
    private  final String separator = "\n";
    private  final String header = "M,T,W,R,F";

   
    
    public void CSVFile(String file) {
       TempEvent t1 = new TempEvent(LocalTime.of(15, 15), 50, "INTD-405, Smith");
       TempEvent t2 = new TempEvent(LocalTime.of(12, 45), 50, "PHIL-215, Krull");
       TempEvent t3 = new TempEvent(LocalTime.of(17, 30), 50, "MUS-131, Lynn");
       TempEvent t4 = new TempEvent(LocalTime.of(16, 30), 50, "MUS-130, Lynn");
       TempEvent t5 = new TempEvent(LocalTime.of(11, 45), 50, "CPTR-422, Mitchell");
       
       List<TempEvent> TempEventsList = new ArrayList<>();
    
       TempEventsList.add(t1);
        TempEventsList.add(t2);
        TempEventsList.add(t3);
        TempEventsList.add(t4);
        TempEventsList.add(t5);
        
        FileWriter fileWriter = null;
        
        //
         try {
            fileWriter = new FileWriter(file);
 
            fileWriter.append(header.toString());
             
            fileWriter.append(separator);
             
            
            for ( TempEvent e : TempEventsList) {
                fileWriter.append(e.getStartTime().format(DateTimeFormatter.ISO_TIME));
                fileWriter.append(delimiter);
                fileWriter.append(e.getDurationMinutesString());
                fileWriter.append(delimiter);
                fileWriter.append(e.getText());
                fileWriter.append(delimiter);
                fileWriter.append(separator);
       
            }
                 System.out.println("CSV file was created ");
             
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
             
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
             
        }
        
    
}
}


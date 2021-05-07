/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author SaraRomero
 */ 
public class CSVCreator {
    private  final String delimiter = "|";
    private  final String separator = "\n";
    private  final String header = "COURSE TERM " + delimiter+ "COURSE NUMBER" + delimiter+ 
            "COURSE TITLE" + delimiter+ "CORE DESIGNATION" + delimiter+ "LARC DESIGNATION"+
            delimiter+"FAC LNAME" + delimiter+ "FAC FNAME" + delimiter+ "SEMESTER HOURS"+ delimiter+
            "MAX CAPACITY"+ delimiter +"DAYS"+delimiter+ "BEGIN TIME"+ delimiter+"END TIME"+ delimiter+"MEETING METHOD"+
            delimiter+"COURSES NOTES"+ delimiter;
   
    private String coursesToCSV(ArrayList<Course> coursesList) {
        String scheduleToStringForm = header + separator;
        
        for (int i = 0; i < coursesList.size(); i++) {
            scheduleToStringForm += coursesList.get(i).getCourseTerm() + delimiter;
            scheduleToStringForm += coursesList.get(i).getCourseNumber() + delimiter;
            scheduleToStringForm += coursesList.get(i).getCourseTitle() + delimiter;
            scheduleToStringForm += coursesList.get(i).getCOREdesignation() + delimiter;
            scheduleToStringForm += coursesList.get(i).getLARCdesignation() + delimiter;
            scheduleToStringForm += coursesList.get(i).getFacultyLname() + delimiter;
            scheduleToStringForm += coursesList.get(i).getFacultyFname() + delimiter;
            scheduleToStringForm += coursesList.get(i).getSemesterHours() + delimiter;
            scheduleToStringForm += coursesList.get(i).getMaxCapacity() + delimiter;
            scheduleToStringForm += getTimeString(coursesList.get(i))+ delimiter;
            scheduleToStringForm += coursesList.get(i).getMeetingMethod() + delimiter;
            scheduleToStringForm += coursesList.get(i).getCourseNotes() + separator;
        }
        
        return removeNull(scheduleToStringForm);
    }
    
    
    private String getTimeString(Course c) {
        String s = "";
        Map<Course.Day, LocalTime[]> map = c.getScheduledTimes();
        for(Course.Day d : map.keySet()) {
            s += d.name();
        } s += delimiter;
        LocalTime[] l = (LocalTime[]) map.values().toArray()[0];
        
        s += l[0].format(DateTimeFormatter.ofPattern("hh:mm a"));
        s += delimiter;
        s += l[1].format(DateTimeFormatter.ofPattern("hh:mm a")); //MWF|10:30 am|11:20 am
        
        return s;
    }
    
    private String removeNull(String input){
        return input.replaceAll("null", "");
    }
    
    
    /**
     * Include filename and filetype in path
     * @param text
     * @param filepath
     * @return 
     */
    private boolean writeCSVFile(String text, String filepath) {
        if(!filepath.endsWith(".csv")) filepath+=".csv";
        try {
            File file = new File(filepath);
                file.createNewFile();
            FileWriter fw = new FileWriter(filepath);
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
 
    public void exportCourses(ArrayList<Course> coursesList, String filepath) {
        String export = coursesToCSV(coursesList);
        writeCSVFile(export, filepath);
    }
    
}

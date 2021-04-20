/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author SaraRomero
 */ 
public class CSVFile {
    private  final String delimiter = "|";
    private  final String separator = "\n";
    private  final String header = "COURSE TERM " + delimiter+ "COURSE NUMBER" + delimiter+ 
            "COURSE TITLE" + delimiter+ "CORE DESIGNATION" + delimiter+ "LARC DESIGNATION"+
            delimiter+"FAC LNAME" + delimiter+ "FAC FNAME" + delimiter+ "SEMESTER HOURS"+ delimiter+
            "MAX CAPACITY"+ delimiter +"DAYS"+delimiter+ "BEGIN TIME"+ delimiter+"END TIME"+ delimiter+"MEETING METHOD"+
            delimiter+"COURSES NOTES"+ delimiter;
    private ArrayList<Course> coursesList;
    private String scheduleToStringForm ="";
     
     private Map<Course.Day, LocalTime[]> tempMap = new HashMap<>();
     private String test = "";
     private Course.Day day;
  
    public CSVFile(ArrayList<Course> coursesList) {
        /*
        Constructor to initialize Course List, whenevery you make a CSV File you will need a course List
        */
           this.coursesList = coursesList;
    }
    
   
    public String CoursestoCSV(ArrayList<Course> coursesList) {
        scheduleToStringForm = header + separator;
        
        for (int i = 0; i < coursesList.size(); i++) {
           //  day+= coursesList.get(i).getStartTime(Course.Day.valueOf());
           
    //      if (checkNull(coursesList)) {  
            
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
//            scheduleToStringForm += (coursesList.get(i).getMeetingMethod() == null ? "" : coursesList.get(i).getMeetingMethod()) + delimiter;
             

            scheduleToStringForm += coursesList.get(i).getCourseNotes() + separator;
            
       //   }
          
        }
        return scheduleToStringForm.replaceAll("null", "");
    }
    
    public String getCourseDays (Course c){ 
      test=  c.getScheduledTimes().keySet().toString();
      test = test.replaceAll("[\\[\\](){}]","");   
      test = test.replaceAll(" ","");
      test = test.replaceAll(",","");
       
        return test;
    } 
//    public Course.Day getCourseDay (Course c){
//        c.getScheduledTimes().values();
//    }
    
    public String getTimeString(Course c) {
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
    
    public String removeNull(String input){
         return input.replaceAll("null", "");
    }
 
}

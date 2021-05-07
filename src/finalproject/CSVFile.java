/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import finalproject.Course.Day;
import finalproject.Course.Day.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author SaraRomero niomi
 */ 
public class CSVFile {
    private  final String delimiter = "|";
    private  final String separator = "\n";
    private  final String irregularSeparator = "/";
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
    String courseTimeAndDate;
  
    public CSVFile(ArrayList<Course> coursesList) {
        /*
        Constructor to initialize Course List, whenevery you make a CSV File you will need a course List
        */
           this.coursesList = coursesList;
    }
    
   
    public String CoursestoCSV(ArrayList<Course> coursesList) {
        scheduleToStringForm = header + separator;
        
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
        return scheduleToStringForm.replaceAll("null", "");
    }
    
    public String getCourseDays (Course c){ 
      test=  c.getScheduledTimes().keySet().toString();
      test = test.replaceAll("[\\[\\](){}]","");   
      test = test.replaceAll(" ","");
      test = test.replaceAll(",","");
       
        return test;
    } 

        public String getTimeString(Course c) {
        
        Map<Course.Day, LocalTime[]> map = c.getScheduledTimes();
        HashMap<LocalTime, ArrayList<Course.Day>> startTimes = new HashMap<>();
        HashMap<LocalTime, ArrayList<Course.Day>> endTimes = new HashMap<>();
        
        for(Course.Day d : map.keySet()) {
            LocalTime[] times = map.get(d);
            if(startTimes.containsKey(times[0])) {
                if(!startTimes.get(times[0]).contains(d)) {
                    startTimes.get(times[0]).add(d);
                    startTimes.get(times[0]).sort(null);
                }
            } else {
                ArrayList<Course.Day> temp = new ArrayList<>();
                    temp.add(d);
                startTimes.put(times[0], temp);
            }
            if(endTimes.containsKey(times[1])) {
                if(!endTimes.get(times[1]).contains(d)) {
                    endTimes.get(times[1]).add(d);
                    endTimes.get(times[1]).sort(null);
                }
            } else {
                ArrayList<Course.Day> temp = new ArrayList<>();
                    temp.add(d);
                endTimes.put(times[1], temp);
            }
        }
        
       
        String s = ""; 
        
        ArrayList<LocalTime> times = new ArrayList<>();
            times.addAll(startTimes.keySet());
            times.sort((t1, t2) -> {
                int value = startTimes.get(t2).size() - startTimes.get(t1).size();
                if(value == 0) value = startTimes.get(t1).get(0).compareTo(startTimes.get(t2).get(0));
                return value;
            });
        ArrayList<Course.Day> days = new ArrayList<>();
        String dayString = "";
            for(LocalTime t : times) {
                days.addAll(startTimes.get(t));
                days.sort(null);
                for(Day d : days) dayString += d;
                dayString += irregularSeparator;
                days.clear();
            } dayString = dayString.replaceFirst("\\/\\Z", "");
        String startString = "";
            for(LocalTime t : times) {
                startString += t.format(DateTimeFormatter.ofPattern("hh:mm a")) + irregularSeparator;
            } startString = startString.replaceFirst("\\/\\Z", "");
        times.clear();
        times.addAll(endTimes.keySet());
        times.sort((t1, t2) -> {
                int value = endTimes.get(t2).size() - endTimes.get(t1).size();
                if(value == 0) value = endTimes.get(t1).get(0).compareTo(endTimes.get(t2).get(0));
                return value;
        });
        String endString = "";
            for(LocalTime t : times) {
                endString += t.format(DateTimeFormatter.ofPattern("hh:mm a")) + irregularSeparator;
            } endString = endString.replaceFirst("\\/\\Z", "");
            
        s = dayString + delimiter + startString + delimiter + endString;
        return s;
   
    }
   

}

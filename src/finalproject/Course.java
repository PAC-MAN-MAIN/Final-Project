/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color; 
import static javafx.scene.paint.Color.WHITE;




/**
 *
 * @author shado
 */

public class Course implements Comparable<Course>,Serializable {
    
    private String courseTerm = "";
    private String courseNumber = "";
    private String courseTitle = "";
    private String COREdesignation = ""; 
    private String LARCdesignation = ""; 
    private boolean FYappropriate = false; 
    private String facultyLname = ""; 
    private String facultyFname = ""; 
    private boolean adjunct = false;
    private double semesterHours = 0;
    private double scheduledHours = 0;
    private int maxCapacity = 0;
    private boolean locked = false;
    private MeetingMethod meetingMethod = null;
    private CourseLength courseLength = null;
    private String courseNotes = "";
    private Map<Day, LocalTime[]> scheduledTimes = new HashMap<>();
    private SerialColor color = new SerialColor(WHITE);
//    private String daysScheduled = "";
//    private LocalTime startTime = null;
//    private LocalTime endTime = null;
    
    public enum MeetingMethod{
        LEC,
        ONL,
        TRVL,
        LAB;
    }
    public enum CourseLength{
        FirstHalf,
        SecondHalf,
        FullSemester;
    }
    public enum Day{
        M("Monday"),
        T("Tuesday"),
        W("Wednesday"),
        R("Thursday"),
        F("Friday");
        
        private final String value;
        Day(String s) {value = s;}
        String getValue() {return value;}
    }
    
    public Course(String term, String courseNum, String courseTitle, String CORE, String LARC, boolean FYappropriate, String facultyLastName, String facultyFirstName, boolean adjunct, double semesterHrs, double scheduledHrs, int capacity, boolean isLocked, MeetingMethod meetingMethod, String notes, CourseLength courseLength){
        courseTerm = term;
        courseNumber = courseNum;
        this.courseTitle = courseTitle;
        COREdesignation = CORE;
        LARCdesignation = LARC;
        this.FYappropriate = FYappropriate;
        facultyLname = facultyLastName;
        facultyFname = facultyFirstName;
        this.adjunct = adjunct;
        semesterHours = semesterHrs;
        scheduledHours = scheduledHrs;
        maxCapacity = capacity;
        locked = isLocked;
        this.meetingMethod = meetingMethod;
        courseNotes = notes;
        this.courseLength = courseLength;
    }
    public Course(){}

    //<editor-fold desc="Getters/Setters" defaultstate="collapsed">
    
    public String getCourseTerm() {
        return courseTerm;
    }
    public void setCourseTerm(String courseTerm) {
        this.courseTerm = courseTerm;
    }

    public String getCourseNumber() {
        return courseNumber;
    }
    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseTitle() {
        return courseTitle;
    }
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCOREdesignation() {
        return COREdesignation;
    }
    public void setCOREdesignation(String COREdesignation) {
        this.COREdesignation = COREdesignation;
    }

    public String getLARCdesignation() {
        return LARCdesignation;
    }
    public void setLARCdesignation(String LARCdesignation) {
        this.LARCdesignation = LARCdesignation;
    }

    public boolean isFYappropriate() {
        return FYappropriate;
    }
    public void setFYappropriate(boolean FYappropriate) {
        this.FYappropriate = FYappropriate;
    }

    public String getFacultyLname() {
        return facultyLname;
    }
    public void setFacultyLname(String FacultyLname) {
        this.facultyLname = FacultyLname;
    }

    public String getFacultyFname() {
        return facultyFname;
    }
    public void setFacultyFname(String FacultyFname) {
        this.facultyFname = FacultyFname;
    }

    public boolean getAdjunct() {
        return adjunct;
    }
    public void setAdjunct(boolean isAdjunct) {
        this.adjunct = isAdjunct;
    }

    public double getSemesterHours() {
        return semesterHours;
    }
    public void setSemesterHours(double semesterHours) {
        this.semesterHours = semesterHours;
    }

    public double getScheduledHours() {
        return scheduledHours;
    }
    public void setScheduledHours(double scheduledHours) {
        this.scheduledHours = scheduledHours;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public boolean getLockedCourse() {
        return locked;
    }
    public void setLockedCourse(boolean isLocked) {
        this.locked = isLocked;
    }

    public MeetingMethod getMeetingMethod() {
        return meetingMethod;
    }
    public void setMeetingMethod(MeetingMethod meetingMethod) {
        this.meetingMethod = meetingMethod;
    }

    public String getCourseNotes() {
        return courseNotes;
    }
    public void setCourseNotes(String courseNotes) {
        this.courseNotes = courseNotes;
    }

    public CourseLength getCourseLength() {
        return courseLength;
    }
    public void setCourseLength(CourseLength courseLength) {
        this.courseLength = courseLength;
    }
    
    public Map<Day, LocalTime[]> getScheduledTimes() {
        return scheduledTimes;
    }
    public void setScheduledTimes(Map<Day, LocalTime[]> scheduledTimes) {
        this.scheduledTimes = scheduledTimes;
    }
    public LocalTime[] getScheduledTimes(Day d) {
        return scheduledTimes.get(d);
    }
    public void setScheuledTimes(Day d, LocalTime[] times) {
        scheduledTimes.put(d, times);
    }
    public LocalTime getStartTime(Day d) {
        return scheduledTimes.get(d)[0];
    }
    public void setStartTime(Day d, LocalTime start) {
        if(scheduledTimes.get(d) != null) scheduledTimes.get(d)[0] = start;
        else scheduledTimes.put(d, new LocalTime[]{start, LocalTime.of(start.getHour(), start.getMinute())});
    }
    public LocalTime getEndTime(Day d) {
        return scheduledTimes.get(d)[1];
    }
    public void setEndTime(Day d, LocalTime end) {
        if(scheduledTimes.get(d) != null) scheduledTimes.get(d)[1] = end;
        else scheduledTimes.put(d, new LocalTime[]{LocalTime.of(end.getHour(), end.getMinute()), end});
    }
    public void removeDay(Day d) {
        scheduledTimes.remove(d);
    }

    public int getDurationMinutes(Day d) {
        LocalTime start = scheduledTimes.get(d)[0];
        LocalTime end = scheduledTimes.get(d)[1];
        return (int)start.until(end, ChronoUnit.MINUTES);
    }
    public void setDurationMinutes(Day d, int minutes) {
        LocalTime start = scheduledTimes.get(d)[0];
        scheduledTimes.get(d)[1] = start.plusMinutes(minutes);
    }
    
    public Color getColor(){
        return color.getColor(); 
    }
    
    public String getColorString(){
        //System.out.println("String: "+ color.toString() + "\n" + "Substring: " + color.toString().substring(2)); 
        return color.getColor().toString().substring(2,8);
    }
    
    
    public void setColor(Color c){
        color = new SerialColor(c);
    }

    //</editor-fold>
    
    /**
     * Checks to see if a given course overlaps with this course on a given day
     * @param c
     * @param d
     * @return 
     */
    public boolean conflictsWith(Course c, Day d) {
        if(c.getScheduledTimes(d) == null) return false;
        LocalTime start1 = getStartTime(d);
        LocalTime start2 = c.getStartTime(d);
        LocalTime end1 = getEndTime(d);
        LocalTime end2 = c.getEndTime(d);
        
        boolean out = false;
        
        while((start1.isBefore(end1) || start1.equals(end1)) && !out) {
            if(start1.isAfter(start2) && start1.isBefore(end2)) out = true;
            if(!out) start1 = start1.plusMinutes(1);
        }
        
        return out;
    }
    
    public String getFormattedText(){
        if(facultyFname == null && facultyLname == null){
            return courseNumber;
        }
        else{
            return  courseNumber + ", " + facultyLname;
        }
    }
    
    @Override
    public String toString(){
        return getFormattedText();
    }
    
    @Override
    public int compareTo(Course o) {
        return courseNumber.compareTo(o.getCourseNumber());
    }
    
}

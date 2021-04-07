/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author shado
 */

public class Course implements Comparable<Course> {
    private String courseTerm;
    private String courseNumber;
    private String courseTitle; 
    private String COREdesignation; 
    private String LARCdesignation; 
    private boolean FYappropriate; 
    private String facultyLname; 
    private String facultyFname; 
    private boolean adjunct;
    private double semesterHours;
    private double scheduledHours;
    private int maxCapacity;
    private String daysScheduled;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean locked;
    private MeetingMethod meetingMethod;
    private String courseNotes;
    private CourseLength courseLength;
    
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
    
    public Course(String term, String courseNum, String courseTitle, String CORE, String LARC, boolean FYappropriate, String facultyLastName, String facultyFirstName, boolean adjunct, double semesterHrs, double scheduledHrs, int capacity, String daysOfWeek, LocalTime startTime, LocalTime endTime, boolean isLocked, MeetingMethod meetingMethod, String notes, CourseLength courseLength){
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
        daysScheduled = daysOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getDaysScheduled() {
        return daysScheduled;
    }
    public void setDaysScheduled(String daysScheduled) {
        this.daysScheduled = daysScheduled;
    }

    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
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

    public int getDurationMinutes() {
        return (int)startTime.until(endTime, ChronoUnit.MINUTES);
    }
    public void setDurationMinutes(int minutes) {
        endTime = startTime.plusMinutes(minutes);
    }

    //</editor-fold>
    
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
        return startTime.compareTo(o.getStartTime());
    }
    
    @Override
    public Course clone() {
        return new Course(courseTerm, courseNumber, courseTitle, COREdesignation, LARCdesignation, FYappropriate, facultyFname, facultyLname, adjunct, semesterHours, scheduledHours, maxCapacity, daysScheduled, LocalTime.of(startTime.getHour(), startTime.getMinute()), LocalTime.of(endTime.getHour(), endTime.getMinute()), locked, meetingMethod, courseNotes, courseLength);
    }
    
}

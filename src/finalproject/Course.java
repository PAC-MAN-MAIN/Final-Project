/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

/**
 *
 * @author shado
 */
enum MeetingMethod{
    inPerson,
    online,
    hybrid;
}
enum CourseLength{
    halfSemester,
    fullSemester,
    JanuaryTerm;
}

public class Course {
    private String courseTerm;
    private String courseNumber;
    private String courseTitle; 
    private String COREdesignation; 
    private String LARCdesignation; 
    private boolean FYappropriate; 
    private String FacultyLname; 
    private String FacultyFname; 
    private String adjunctTeacher;
    private double semesterHours;
    private double scheduledHours;
    private double maxCapacity;
    private String daysScheduled;
    private double startTime;
    private double endTime;
    private String lockedCourse;
    private MeetingMethod meetingMethod;
    private String courseNotes;
    private CourseLength courseLength;
    private String schedulingConflicts;
    
    private String text = "";
    
    public Course(String Term, String CourseNum, String CourseTitle, String CORE, String LARC, boolean forFirstYears, String FacultyLastName, String FacultyFirstName, String AdjunctName, double SemesterHrs, double ScheduledHrs, double Capacity, String DaysOTW, double StartTime, double EndTime, String isLocked, MeetingMethod meetingMethod, String Notes, CourseLength courseLength, String Conflicts){
        courseTerm = Term;
        courseNumber = CourseNum;
        courseTitle = CourseTitle;
        COREdesignation = CORE;
        LARCdesignation = LARC;
        FYappropriate = forFirstYears;
        FacultyLname = FacultyLastName;
        FacultyFname = FacultyFirstName;
        adjunctTeacher = AdjunctName;
        semesterHours = SemesterHrs;
        scheduledHours = ScheduledHrs;
        maxCapacity = Capacity;
        daysScheduled = DaysOTW;
        startTime = StartTime;
        endTime = EndTime;
        lockedCourse = isLocked;
        meetingMethod = this.meetingMethod;
        courseNotes = Notes;
        courseLength = this.courseLength;
        schedulingConflicts = Conflicts;
    }
    
//    public Course(){
//        
//    }

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
        return FacultyLname;
    }

    public void setFacultyLname(String FacultyLname) {
        this.FacultyLname = FacultyLname;
    }

    public String getFacultyFname() {
        return FacultyFname;
    }

    public void setFacultyFname(String FacultyFname) {
        this.FacultyFname = FacultyFname;
    }

    public String getAdjunctTeacher() {
        return adjunctTeacher;
    }

    public void setAdjunctTeacher(String adjunctTeacher) {
        this.adjunctTeacher = adjunctTeacher;
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

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getDaysScheduled() {
        return daysScheduled;
    }

    public void setDaysScheduled(String daysScheduled) {
        this.daysScheduled = daysScheduled;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public String getLockedCourse() {
        return lockedCourse;
    }

    public void setLockedCourse(String lockedCourse) {
        this.lockedCourse = lockedCourse;
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

    public String getSchedulingConflicts() {
        return schedulingConflicts;
    }

    public void setSchedulingConflicts(String schedulingConflicts) {
        this.schedulingConflicts = schedulingConflicts;
    }

    public String formatText(){
        if(getFacultyFname() == null && getFacultyLname() == null){
            return text += getCourseNumber() + ", " + getAdjunctTeacher();
        }
        else{
            return text += getCourseNumber() + ", " + getFacultyLname();
        }
    }
    
    @Override
    public String toString(){
        return formatText();
    }
    
}

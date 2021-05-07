/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import javafx.scene.paint.Color;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author rewil
 */
public class EditClassFXMLController implements Initializable {

    @FXML TextField termField;
    @FXML TextField courseNumberField;
    @FXML TextField courseTitleField;
    @FXML TextField facultyFirstField;
    @FXML TextField facultyLastField;
    @FXML TextField coreField;
    @FXML TextField larcField;
    @FXML TextField capacityField;
    @FXML TextField semesterHourField;
    @FXML TextField scheduledHourField;
    @FXML TextArea notesArea;
    @FXML CheckBox adjunctCheck;
    @FXML CheckBox firstYearCheck;
    @FXML ComboBox methodCombo;
    @FXML ComboBox lengthCombo;
    @FXML ColorPicker colorPicker;
    
    @FXML CheckBox mondayCheck;
    @FXML TextField mondayStartField;
    @FXML TextField mondayEndField;
    @FXML TextField mondayDurationField;
    @FXML CheckBox tuesdayCheck;
    @FXML TextField tuesdayStartField;
    @FXML TextField tuesdayEndField;
    @FXML TextField tuesdayDurationField;
    @FXML CheckBox wednesdayCheck;
    @FXML TextField wednesdayStartField;
    @FXML TextField wednesdayEndField;
    @FXML TextField wednesdayDurationField;
    @FXML CheckBox thursdayCheck;
    @FXML TextField thursdayStartField;
    @FXML TextField thursdayEndField;
    @FXML TextField thursdayDurationField;
    @FXML CheckBox fridayCheck;
    @FXML TextField fridayStartField;
    @FXML TextField fridayEndField;
    @FXML TextField fridayDurationField;
    @FXML CheckBox lockedCheck;
    
    private Course course = new Course();
    
    @FXML public void saveAction() {
        if(!validateData()) return;
        try {
            course.setAdjunct(adjunctCheck.isSelected());
            course.setCOREdesignation(coreField.getText());
            course.setCourseLength((Course.CourseLength) lengthCombo.getSelectionModel().getSelectedItem());
            course.setCourseNotes(notesArea.getText());
            course.setCourseNumber(courseNumberField.getText());
            course.setCourseTerm(termField.getText());
            course.setCourseTitle(courseTitleField.getText());
            course.setFYappropriate(firstYearCheck.isSelected());
            course.setFacultyFname(facultyFirstField.getText());
            course.setFacultyLname(facultyLastField.getText());
            course.setLARCdesignation(larcField.getText());
            course.setMaxCapacity(Integer.parseInt(capacityField.getText()));
            course.setMeetingMethod((Course.MeetingMethod) methodCombo.getSelectionModel().getSelectedItem());
            course.setScheduledHours(Double.parseDouble(scheduledHourField.getText()));
            course.setSemesterHours(Double.parseDouble(semesterHourField.getText()));
            course.setLockedCourse(lockedCheck.isSelected());
            course.setScheduledTimes(getTimes());
            course.setColor(colorPicker.getValue());

            close();
        } catch(Exception e) {}
    }
        private boolean validateData() {
            String message = "";
            boolean out = true;
            try{
                Integer.parseInt(capacityField.getText());
            } catch (Exception e) {
                message += "Capacity must be numerical\n";
                out = false;
            }
            try{
                Double.parseDouble(scheduledHourField.getText());
            } catch (Exception e) {
                message += "Scheduled Hours must be numerical\n";
                out = false;
            }
            try{
                Double.parseDouble(semesterHourField.getText());
            } catch (Exception e) {
                message += "Semester Hours must be numerical\n";
                out = false;
            }
            try{
                getTimes();
            } catch (Exception e) {
                message += "All activated days must have valid times in the pattern hh:mm in the start and end fields\n";
                out = false;
            }
            
            if(!out) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setHeaderText("Invalid Data");
                    a.setContentText(message.trim());
                    a.showAndWait();
            }
            System.out.println(message);
            return out;
        }
        private Map<Course.Day, LocalTime[]> getTimes() throws Exception{
            Map<Course.Day, LocalTime[]> out = new HashMap<>();
            
            if(mondayCheck.isSelected()) {
                LocalTime start = stringToLocalTime(mondayStartField.getText());
                LocalTime end = stringToLocalTime(mondayEndField.getText());
                if(start == null || end == null) throw new Exception();
                out.put(Course.Day.M, new LocalTime[]{start, end});
            }
            if(tuesdayCheck.isSelected()) {
                LocalTime start = stringToLocalTime(tuesdayStartField.getText());
                LocalTime end = stringToLocalTime(tuesdayEndField.getText());
                if(start == null || end == null) throw new Exception();
                out.put(Course.Day.T, new LocalTime[]{start, end});
            }
            if(wednesdayCheck.isSelected()) {
                LocalTime start = stringToLocalTime(wednesdayStartField.getText());
                LocalTime end = stringToLocalTime(wednesdayEndField.getText());
                if(start == null || end == null) throw new Exception();
                out.put(Course.Day.W, new LocalTime[]{start, end});
            }
            if(thursdayCheck.isSelected()) {
                LocalTime start = stringToLocalTime(thursdayStartField.getText());
                LocalTime end = stringToLocalTime(thursdayEndField.getText());
                if(start == null || end == null) throw new Exception();
                out.put(Course.Day.R, new LocalTime[]{start, end});
            }
            if(fridayCheck.isSelected()) {
                LocalTime start = stringToLocalTime(fridayStartField.getText());
                LocalTime end = stringToLocalTime(fridayEndField.getText());
                if(start == null || end == null) throw new Exception();
                out.put(Course.Day.F, new LocalTime[]{start, end});
            }
            
            return out;
        }
    
    @FXML public void startTimeChange(KeyEvent e) {
        TextField source = (TextField)e.getSource();
        LocalTime newTime = stringToLocalTime(source.getText());
            if(newTime == null) return;
        switch(source.getId()) {
            case "Monday": {
                int duration = Integer.parseInt(mondayDurationField.getText());
                mondayEndField.setText(localTimeToString(newTime.plusMinutes(duration)));
            } break;
            case "Tuesday": {
                int duration = Integer.parseInt(tuesdayDurationField.getText());
                tuesdayEndField.setText(localTimeToString(newTime.plusMinutes(duration)));
            } break;
            case "Wednesday": {
                int duration = Integer.parseInt(wednesdayDurationField.getText());
                wednesdayEndField.setText(localTimeToString(newTime.plusMinutes(duration)));
            } break;
            case "Thursday": {
                int duration = Integer.parseInt(thursdayDurationField.getText());
                thursdayEndField.setText(localTimeToString(newTime.plusMinutes(duration)));
            } break;
            case "Friday": {
                int duration = Integer.parseInt(fridayDurationField.getText());
                fridayEndField.setText(localTimeToString(newTime.plusMinutes(duration)));
            } break;
        }
    }
    @FXML public void endTimeChange(KeyEvent e) {
        TextField source = (TextField)e.getSource();
        LocalTime newTime = stringToLocalTime(source.getText());
            if(newTime == null) return;
        switch(source.getId()) {
            case "Monday": {
                LocalTime start = stringToLocalTime(mondayStartField.getText());
                mondayDurationField.setText("" + start.until(newTime, ChronoUnit.MINUTES));
            } break;
            case "Tuesday": {
                LocalTime start = stringToLocalTime(tuesdayStartField.getText());
                tuesdayDurationField.setText("" + start.until(newTime, ChronoUnit.MINUTES));
            } break;
            case "Wednesday": {
                LocalTime start = stringToLocalTime(wednesdayStartField.getText());
                wednesdayDurationField.setText("" + start.until(newTime, ChronoUnit.MINUTES));
            } break;
            case "Thursday": {
                LocalTime start = stringToLocalTime(thursdayStartField.getText());
                thursdayDurationField.setText("" + start.until(newTime, ChronoUnit.MINUTES));
            } break;
            case "Friday": {
                LocalTime start = stringToLocalTime(fridayStartField.getText());
                fridayDurationField.setText("" + start.until(newTime, ChronoUnit.MINUTES));
            } break;
        }
    }
    @FXML public void durationChange(KeyEvent e) {
        TextField source = (TextField)e.getSource();
        if(!source.getText().matches("[0-9]+")) return;
        int duration = Integer.parseInt(source.getText());
        switch(source.getId()) {
            case "Monday": {
                LocalTime start = stringToLocalTime(mondayStartField.getText());
                mondayEndField.setText(localTimeToString(start.plusMinutes(duration)));
            } break;
            case "Tuesday": {
                LocalTime start = stringToLocalTime(tuesdayStartField.getText());
                tuesdayEndField.setText(localTimeToString(start.plusMinutes(duration)));
            } break;
            case "Wednesday": {
                LocalTime start = stringToLocalTime(wednesdayStartField.getText());
                wednesdayEndField.setText(localTimeToString(start.plusMinutes(duration)));
            } break;
            case "Thursday": {
                LocalTime start = stringToLocalTime(thursdayStartField.getText());
                thursdayEndField.setText(localTimeToString(start.plusMinutes(duration)));
            } break;
            case "Friday": {
                LocalTime start = stringToLocalTime(fridayStartField.getText());
                fridayEndField.setText(localTimeToString(start.plusMinutes(duration)));
            } break;
        }
    }
    @FXML public void updateEnabledDays() {
        mondayStartField.setDisable(!mondayCheck.isSelected());
        mondayEndField.setDisable(!mondayCheck.isSelected());
        mondayDurationField.setDisable(!mondayCheck.isSelected());
        
        tuesdayStartField.setDisable(!tuesdayCheck.isSelected());
        tuesdayEndField.setDisable(!tuesdayCheck.isSelected());
        tuesdayDurationField.setDisable(!tuesdayCheck.isSelected());
        
        wednesdayStartField.setDisable(!wednesdayCheck.isSelected());
        wednesdayEndField.setDisable(!wednesdayCheck.isSelected());
        wednesdayDurationField.setDisable(!wednesdayCheck.isSelected());
        
        thursdayStartField.setDisable(!thursdayCheck.isSelected());
        thursdayEndField.setDisable(!thursdayCheck.isSelected());
        thursdayDurationField.setDisable(!thursdayCheck.isSelected());
        
        fridayStartField.setDisable(!fridayCheck.isSelected());
        fridayEndField.setDisable(!fridayCheck.isSelected());
        fridayDurationField.setDisable(!fridayCheck.isSelected());
    }
    
    /**
     * Resets all fields in Course Creator to default states
     */
    public void clearFields() {
        termField.clear();
        courseNumberField.clear();
        courseTitleField.clear();
        facultyFirstField.clear();
        facultyLastField.clear();
        coreField.clear();
        larcField.clear();
        capacityField.clear();
        semesterHourField.clear();
        scheduledHourField.clear();
        notesArea.clear();
        adjunctCheck.setSelected(false);
        firstYearCheck.setSelected(false);
        methodCombo.getSelectionModel().clearSelection();
        lengthCombo.getSelectionModel().clearSelection();
        lockedCheck.setSelected(false);
        colorPicker.setValue(Color.WHITE);
        
        setTimeFields(new HashMap<>());
        
        course = new Course();
    }
    
    public void edit(Course c){
        course = c;
        
        termField.setText(c.getCourseTerm());
        courseNumberField.setText(c.getCourseNumber());
        courseTitleField.setText(c.getCourseTitle());
        facultyFirstField.setText(c.getFacultyFname());
        facultyLastField.setText(c.getFacultyLname());
        coreField.setText(c.getCOREdesignation());
        larcField.setText(c.getLARCdesignation());
        capacityField.setText("" + c.getMaxCapacity());
        semesterHourField.setText("" + c.getSemesterHours());
        scheduledHourField.setText("" + c.getScheduledHours());
        notesArea.setText(c.getCourseNotes());
        adjunctCheck.setSelected(c.getAdjunct());
        firstYearCheck.setSelected(c.isFYappropriate());
        methodCombo.getSelectionModel().select(c.getMeetingMethod());
        lengthCombo.getSelectionModel().select(c.getCourseLength());
        lockedCheck.setSelected(c.getLockedCourse());
        colorPicker.setValue(c.getColor());
        
        setTimeFields(c.getScheduledTimes());
    }
        private void setTimeFields(Map<Course.Day, LocalTime[]> times) {
            String startText = "";
            String endText = "";
            String durationText = "";
            if(times.containsKey(Course.Day.M)) {
                LocalTime[] dayTimes = times.get(Course.Day.M);
                mondayCheck.setSelected(true);
                startText = localTimeToString(dayTimes[0]);
                endText = localTimeToString(dayTimes[1]);
                durationText = "" + dayTimes[0].until(dayTimes[1], ChronoUnit.MINUTES);
            } else {
                mondayCheck.setSelected(false);
                startText = "";
                endText = "";
                durationText = "";
            }
                mondayStartField.setText(startText);
                mondayEndField.setText(endText);
                mondayDurationField.setText(durationText);
            if(times.containsKey(Course.Day.T)) {
                LocalTime[] dayTimes = times.get(Course.Day.T);
                tuesdayCheck.setSelected(true);
                startText = localTimeToString(dayTimes[0]);
                endText = localTimeToString(dayTimes[1]);
                durationText = "" + dayTimes[0].until(dayTimes[1], ChronoUnit.MINUTES);
            } else {
                tuesdayCheck.setSelected(false);
                startText = "";
                endText = "";
                durationText = "";
            }
                tuesdayStartField.setText(startText);
                tuesdayEndField.setText(endText);
                tuesdayDurationField.setText(durationText);
            if(times.containsKey(Course.Day.W)) {
                LocalTime[] dayTimes = times.get(Course.Day.W);
                wednesdayCheck.setSelected(true);
                startText = localTimeToString(dayTimes[0]);
                endText = localTimeToString(dayTimes[1]);
                durationText = "" + dayTimes[0].until(dayTimes[1], ChronoUnit.MINUTES);
            } else {
                wednesdayCheck.setSelected(false);
                startText = "";
                endText = "";
                durationText = "";
            }
                wednesdayStartField.setText(startText);
                wednesdayEndField.setText(endText);
                wednesdayDurationField.setText(durationText);
            if(times.containsKey(Course.Day.R)) {
                LocalTime[] dayTimes = times.get(Course.Day.R);
                thursdayCheck.setSelected(true);
                startText = localTimeToString(dayTimes[0]);
                endText = localTimeToString(dayTimes[1]);
                durationText = "" + dayTimes[0].until(dayTimes[1], ChronoUnit.MINUTES);
            } else {
                thursdayCheck.setSelected(false);
                startText = "";
                endText = "";
                durationText = "";
            }
                thursdayStartField.setText(startText);
                thursdayEndField.setText(endText);
                thursdayDurationField.setText(durationText);
            if(times.containsKey(Course.Day.F)) {
                LocalTime[] dayTimes = times.get(Course.Day.F);
                fridayCheck.setSelected(true);
                startText = localTimeToString(dayTimes[0]);
                endText = localTimeToString(dayTimes[1]);
                durationText = "" + dayTimes[0].until(dayTimes[1], ChronoUnit.MINUTES);
            } else {
                fridayCheck.setSelected(false);
                startText = "";
                endText = "";
                durationText = "";
            }
                fridayStartField.setText(startText);
                fridayEndField.setText(endText);
                fridayDurationField.setText(durationText);
            updateEnabledDays();
        }
    
    private String localTimeToString(LocalTime t) {
        return t.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    private LocalTime stringToLocalTime(String s) {
        if(!s.matches("^[0-2][0-9]:[0-5][0-9]$")) return null;
        String[] temp = s.split(":");
        return LocalTime.of(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
    }
        
  //----------------------------------------------------------------------------
    
    private FXMLDocumentController parentController;
    
    public void setParent(FXMLDocumentController p) {
        parentController = p;
    }
        private void close() {
            if(parentController != null) parentController.closeCourseEditor();
            else System.out.println("Parent is null");
        }
    public Course getCourse() {
        return course;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lengthCombo.setItems(FXCollections.observableArrayList(Course.CourseLength.values()));
        methodCombo.setItems(FXCollections.observableArrayList(Course.MeetingMethod.values()));
    }
    
}

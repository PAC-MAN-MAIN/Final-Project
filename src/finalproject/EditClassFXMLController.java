/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
    @FXML CheckBox lockedCheck;
    
    private Course course = new Course();
    
    @FXML public void saveAction() {
        if(!validateData()) return;
        
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
        
        close();
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
            
            if(!out) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setHeaderText("Invalid Data");
                    a.setContentText(message.trim());
                    a.showAndWait();
            }
            
            return out;
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

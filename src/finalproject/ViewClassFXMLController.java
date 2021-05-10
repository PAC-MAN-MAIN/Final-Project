/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author rewil
 */
public class ViewClassFXMLController implements Initializable {

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
    @FXML TextField methodField;
    @FXML TextField lengthField;
    
    private Course course = new Course();
    
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
        methodField.clear();
        lengthField.clear();
        
        course = new Course();
    }
    
    public void view(Course c){
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
        methodField.setText(c.getMeetingMethod() == null ? "" : c.getMeetingMethod().name());
        lengthField.setText(c.getCourseLength() == null ? "" : c.getCourseLength().name());
    }
    
    @FXML public void editAction() {
        close();
        parentController.openCourseEditor(course);
    }
    
    @FXML public void deleteAction() {
        parentController.deleteCourse(course);
        view(new Course());
        close();
    }
    
  //----------------------------------------------------------------------------
    
    private FXMLDocumentController parentController;
    
    public void setParent(FXMLDocumentController p) {
        parentController = p;
    }
        private void close() {
            if(parentController != null) parentController.closeCourseViewer();
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
    }
    
}

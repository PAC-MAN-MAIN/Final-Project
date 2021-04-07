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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author rewil
 */
public class CreateClassFXMLController implements Initializable {

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
    
    @FXML public void saveAction() {
        System.out.println("Save Action Call");
        close();
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
    }
    
  //----------------------------------------------------------------------------
    
    private FXMLDocumentController parentController;
    
    public void setParent(FXMLDocumentController p) {
        parentController = p;
    }
    private void close() {
        if(parentController != null) parentController.closeCourseCreator();
        else System.out.println("Parent is null");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import finalproject.Course.Day;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author rewil
 */
public class DayGroupFXMLController implements Initializable {

    @FXML ListView groupList;
    
    @FXML CheckBox mondayCheck;
    @FXML CheckBox tuesdayCheck;
    @FXML CheckBox wednesdayCheck;
    @FXML CheckBox thursdayCheck;
    @FXML CheckBox fridayCheck;
    
    @FXML TextField durationField;
    
    private AppConfig config;
    
    @FXML public void addAction() {
        int duration = getDuration();
            if(duration < 0) return;
        DayGroup g = new DayGroup(getSelectedDays(), duration);
        config.addGroup(g);
        
        clearFields();
        updateList();
    }
    @FXML public void updateAction() {
        if(groupList.getSelectionModel().isEmpty()) return;
        if(getDuration() < 0) return;
        deleteAction();
        addAction();
    }
    @FXML public void deleteAction() {
        if(groupList.getSelectionModel().isEmpty()) return;
        config.removeGroup(getSelectedItem());
        updateList();
    }
    
    @FXML public void listSelectAction() {
        if(groupList.getSelectionModel().isEmpty()) return;
        clearFields();
        DayGroup g = getSelectedItem();
        for(Day d : g.getDays()) {
            switch(d) {
                case M: mondayCheck.setSelected(true);
                    break;
                case T: tuesdayCheck.setSelected(true);
                    break;
                case W: wednesdayCheck.setSelected(true);
                    break;
                case R: thursdayCheck.setSelected(true);
                    break;
                case F: fridayCheck.setSelected(true);
                    break;
            }
        }
        durationField.setText("" + g.getDuration());
    }
    
    private Day[] getSelectedDays() {
        ArrayList<Day> days = new ArrayList<>();
        if(mondayCheck.isSelected()) days.add(Day.M);
        if(tuesdayCheck.isSelected()) days.add(Day.T);
        if(wednesdayCheck.isSelected()) days.add(Day.W);
        if(thursdayCheck.isSelected()) days.add(Day.R);
        if(fridayCheck.isSelected()) days.add(Day.F);
        
        return days.toArray(new Day[days.size()]);
    }
    private DayGroup getSelectedItem() {
        return (DayGroup) groupList.getSelectionModel().getSelectedItem();
    }
    private int getDuration() {
        try{
            return Integer.parseInt(durationField.getText());
        } catch (Exception e) {
            return -1;
        }
    }
    
    private void updateList() {
        groupList.setItems(FXCollections.observableArrayList(config.getGroups()));
    }
    private void clearFields() {
        mondayCheck.setSelected(false);
        tuesdayCheck.setSelected(false);
        wednesdayCheck.setSelected(false);
        thursdayCheck.setSelected(false);
        fridayCheck.setSelected(false);
        durationField.clear();
    }
    
    public void setConfig(AppConfig config) {
        this.config = config;
        updateList();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author rewil
 */
public class FXMLDocumentController implements Initializable {
    
    private final double hourHeight = 60.0;
    private final double timeGridFontSize = 15.0;
    
    private final LocalTime minimumTime = LocalTime.of(8, 0);
    private final LocalTime maximumTime = LocalTime.of(20, 0);
    
    @FXML ListView eventList;
    @FXML VBox mondayBox;
    @FXML VBox tuesdayBox;
    @FXML VBox wednesdayBox;
    @FXML VBox thursdayBox;
    @FXML VBox fridayBox;
    
    private ArrayList<TempEvent> mondayEvents = new ArrayList<>();
    private ArrayList<TempEvent> tuesdayEvents = new ArrayList<>();
    private ArrayList<TempEvent> wednesdayEvents = new ArrayList<>();
    private ArrayList<TempEvent> thursdayEvents = new ArrayList<>();
    private ArrayList<TempEvent> fridayEvents = new ArrayList<>();
    
    private ArrayList<TempEvent> unplacedEvents = new ArrayList<>();
    
  //--GUI-Actions---------------------------------------------------------------
    
    @FXML public void timeGridEventAction(ActionEvent e) {
        if(e.getSource() instanceof Button) {
            Button b = (Button) e.getSource();
            System.out.println(b.getText());
        }
    }
    
    @FXML public void createCourseAction() {
        openCourseCreator();
    }
    
  //--Utiliy--------------------------------------------------------------------
    
    /**
     * Provides a button which can be put into a day's VBox to represent an event
     * @param text The text that will show up on the button
     * @param minutes The length of time the event lasts
     * @param disabled - If the button should be disabled (true for fillers)
     * @return 
     */
    private Button getTimeGridEvent(String text, int minutes, boolean disabled) {
        Button b = new Button();
        
        // Default settings
        b.setMaxWidth(Double.MAX_VALUE);
        b.setMinHeight(0);
        b.setMaxHeight(Double.MAX_VALUE);
        b.setFont(new Font(timeGridFontSize));
        b.setAlignment(Pos.TOP_LEFT);
        b.setOnAction((ae) -> timeGridEventAction(ae));
        
        // Custom settings
            double factor = minutes / 60d;
            double height = (hourHeight * factor);
        b.setPrefHeight(height);
            b.setMinHeight(height);
            b.setMaxHeight(height);
        b.setText(text);
        b.setDisable(disabled);
        b.setVisible(!disabled);
        
        return b;
    }
    
    /**
     * Updates the list of Unplaced Events according to the events in the unplacedEvents list
     */
    private void updateUnplacedEvents() {
        eventList.setItems(FXCollections.observableArrayList(unplacedEvents));
    }
    
    /**
     * Updates the Time Grid according to the events in the (day)Events ArrayLists
     */
    private void updateTimeGrid() {
        updateDayTimeGrid(mondayBox, mondayEvents);
        updateDayTimeGrid(tuesdayBox, tuesdayEvents);
        updateDayTimeGrid(wednesdayBox, wednesdayEvents);
        updateDayTimeGrid(thursdayBox, thursdayEvents);
        updateDayTimeGrid(fridayBox, fridayEvents);
    }
    
    /**
     * Given a VBox and an ArrayList of events, will update the VBox to show events
     * @param dayBox
     * @param dayEvents 
     */
    private void updateDayTimeGrid(VBox dayBox, ArrayList<TempEvent> dayEvents) {
        dayEvents.sort(Comparator.naturalOrder());
        ObservableList<Node> dayChildren = dayBox.getChildren();
        dayChildren.clear();
        TempEvent previous = new TempEvent(minimumTime, 0, "");
        for(int i = 0; i < dayEvents.size(); ++i) {
            TempEvent e = dayEvents.get(i);
            int distFromPrevious = getMinuteDistance(previous.getStartTime().plusMinutes(previous.getDurationMinutes()), e.getStartTime());
            if(distFromPrevious > 0) {
                dayChildren.add(getTimeGridEvent("", distFromPrevious, true));
            }
            dayChildren.add(getTimeGridEvent(e.getText(), e.getDurationMinutes(), false));
            previous = e;
        }
    }
    
    /**
     * Returns the number of minutes later than t1 that t2 is
     * @param t1
     * @param t2
     * @return 
     */
    private int getMinuteDistance(LocalTime t1, LocalTime t2) {
        int out = 0;
        LocalTime dist = t2.minusHours(t1.getHour()).minusMinutes(t1.getMinute());
        out += dist.getHour() * 60;
        out += dist.getMinute();
        if(out > (12 * 60)) out -= (24 * 60);
        
//        System.out.println(t1.toString() + " -> " + t2.toString() + " = " + out + " minutes");
        
        return out;
    }
    
  //--Window--------------------------------------------------------------------
    
    private final Stage courseCreatorStage = new Stage();
    private CreateClassFXMLController courseCreatorController;
    
    private void openCourseCreator() {
        courseCreatorController.clearFields();
        courseCreatorStage.showAndWait();
    }
    
    public void closeCourseCreator() {
        courseCreatorStage.close();
    }
    
  //--Init-and-Init-Events------------------------------------------------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeCourseCreator();
            courseCreatorStage.setTitle("Course Scheduler - Course Creator");
        
        // Example classes in every standard timeslot
//        for(int i = 0; i < 12; ++i) {
//            mondayEvents.add(new TempEvent(LocalTime.of(8 + i, 0), 60, "Hour " + (i+1)));
//        }
//        for(int i = 0; i < 8; ++i) {
//            tuesdayEvents.add(new TempEvent(LocalTime.of(8 + (int)Math.floor(i * 1.5), (i % 2) * 30), 90, "Hour " + (i+1)));
//        }
        
        //My schedule
        mondayEvents.add(new TempEvent(LocalTime.of(15, 15), 50, "INTD-405, Smith"));
        mondayEvents.add(new TempEvent(LocalTime.of(12, 45), 50, "PHIL-215, Krull"));
        tuesdayEvents.add(new TempEvent(LocalTime.of(17, 30), 50, "MUS-131, Lynn"));
        mondayEvents.add(new TempEvent(LocalTime.of(16, 30), 50, "MUS-130, Lynn"));
        mondayEvents.add(new TempEvent(LocalTime.of(11, 45), 50, "CPTR-422, Mitchell"));
        wednesdayEvents.addAll(mondayEvents);
        fridayEvents.addAll(mondayEvents);
        thursdayEvents.addAll(tuesdayEvents);
        
        updateTimeGrid();
        
        unplacedEvents.add(new TempEvent(LocalTime.of(0, 0), 0, "UPLD-001"));
        
        updateUnplacedEvents();
                 
    }
   
    private void initializeCourseCreator() {
        try {
            FXMLLoader loader = new FXMLLoader(FinalProject.class.getResource("CreateClassFXML.fxml"));
            Parent root = loader.load();
            courseCreatorController = loader.getController();
            
            courseCreatorStage.setScene(new Scene(root));
            courseCreatorController.setParent(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
       
}

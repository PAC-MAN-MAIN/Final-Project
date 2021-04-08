/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
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
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author rewil
 */
public class FXMLDocumentController implements Initializable {
    
    private final double hourHeight = 60.0;
    private final double minuteIncrementSnap = 5.0;
    private final double timeGridFontSize = 15.0;
    
    private final LocalTime minimumTime = LocalTime.of(8, 0);
    private final LocalTime maximumTime = LocalTime.of(20, 0);
    
    @FXML ListView eventList;
    @FXML VBox mondayBox;
    @FXML VBox tuesdayBox;
    @FXML VBox wednesdayBox;
    @FXML VBox thursdayBox;
    @FXML VBox fridayBox;
    
    private ArrayList<Course> mondayEvents = new ArrayList<>();
    private ArrayList<Course> tuesdayEvents = new ArrayList<>();
    private ArrayList<Course> wednesdayEvents = new ArrayList<>();
    private ArrayList<Course> thursdayEvents = new ArrayList<>();
    private ArrayList<Course> fridayEvents = new ArrayList<>();
    
    private ArrayList<Course> unplacedEvents = new ArrayList<>();
    
  //--GUI-Actions---------------------------------------------------------------
    
    @FXML public void timeGridEventAction(ActionEvent e) {
        if(e.getSource() instanceof Button) {
            Button b = (Button) e.getSource();
            System.out.println(b.getText());
        }
    }
    
    @FXML public void timeGridDragStart(MouseEvent e) {
        if(e.getSource() instanceof Button) {
            Button b = (Button) e.getSource();
            Dragboard db = b.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            
            content.putString("TimeGrid");
            db.setContent(content);
            
            e.consume();
        }
    }
    @FXML public void unplacedListDragStart(MouseEvent e) {
        Dragboard db = eventList.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        
        content.putString("UnplacedList");
        db.setContent(content);
        
        e.consume();
    }
    
    @FXML public void timeGridDragStop(DragEvent e) {
        switch(e.getDragboard().getString()) {
            case "TimeGrid": timeGridDrop(e);
                break;
            case "UnplacedList": unplacedListDrop(e);
                break;
        }
        
        updateTimeGrid();
        updateUnplacedEvents();
    }
        private void timeGridDrop(DragEvent e) {
            double move = e.getY();
                move = Math.round(move / minuteIncrementSnap) * minuteIncrementSnap;
            int hour = (int) (move / 60) + minimumTime.getHour();
            int minute = (int) (move % 60);

            Button source = (Button)e.getGestureSource();
            switch(source.getId()) {
                case "Monday": findAndShiftEvent(source, mondayEvents, hour, minute);
                    break;
                case "Tuesday": findAndShiftEvent(source, tuesdayEvents, hour, minute);
                    break;
                case "Wednesday": findAndShiftEvent(source, wednesdayEvents, hour, minute);
                    break;
                case "Thursday": findAndShiftEvent(source, thursdayEvents, hour, minute);
                    break;
                case "Friday": findAndShiftEvent(source, fridayEvents, hour, minute);
                    break;
            }

            VBox destination = (VBox) e.getSource();
            if(!source.getId().equals(destination.getId())) {
                switch(destination.getId()) {
                    case "Monday": shiftEventDay(source, mondayEvents);
                        break;
                    case "Tuesday": shiftEventDay(source, tuesdayEvents);
                        break;
                    case "Wednesday": shiftEventDay(source, wednesdayEvents);
                        break;
                    case "Thursday": shiftEventDay(source, thursdayEvents);
                        break;
                    case "Friday": shiftEventDay(source, fridayEvents);
                        break;
                }
            }
        }
            private void findAndShiftEvent(Button source, ArrayList<Course> dayEvents, int hour, int minute) {
                for(Course c : dayEvents) {
                    if(c.getFormattedText().equals(source.getText())) {
                        int duration = c.getDurationMinutes();
                        c.setStartTime(LocalTime.of(hour, minute));
                        c.setDurationMinutes(duration);
                    }
                }
            }
            private void shiftEventDay(Button source, ArrayList<Course> destination) {
                switch(source.getId()) {
                    case "Monday": destination.add(removeAndReturnEvent(source.getText(), mondayEvents));
                        break;
                    case "Tuesday": destination.add(removeAndReturnEvent(source.getText(), tuesdayEvents));
                        break;
                    case "Wednesday": destination.add(removeAndReturnEvent(source.getText(), wednesdayEvents));
                        break;
                    case "Thursday": destination.add(removeAndReturnEvent(source.getText(), thursdayEvents));
                        break;
                    case "Friday": destination.add(removeAndReturnEvent(source.getText(), fridayEvents));
                        break;
                }   
            }
                private Course removeAndReturnEvent(String match, ArrayList<Course> dayEvents) {
                    for(Course c : dayEvents) {
                        if(c.getFormattedText().equals(match)) {
                            dayEvents.remove(c);
                            return c;
                        }
                    }
                    return null;
                }
        private void unplacedListDrop(DragEvent e) {
            Course c = (Course)eventList.getSelectionModel().getSelectedItems().get(0);
            int dropPos = (int) e.getY();
                dropPos = (int) (Math.round(dropPos / minuteIncrementSnap) * minuteIncrementSnap);
            int hours = (int) (dropPos / 60) + minimumTime.getHour();
            int minutes = (int) (dropPos % 60);
            
//            System.out.println("Dropping from Unplaced at y" + dropPos + " (" + hours + ":" + minutes + ")");
            
            c.setStartTime(LocalTime.of(hours, minutes));
            
            switch(((VBox)e.getSource()).getId()) {
                case "Monday": {
                    c.setDurationMinutes(AppConfig.DefaultMondayDurationMinutes);
                    mondayEvents.add(c);
                }
                    break;
                case "Tuesday":{
                    c.setDurationMinutes(AppConfig.DefaultTuesdayDurationMinutes);
                    tuesdayEvents.add(c);
                } 
                    break;
                case "Wednesday": {
                    c.setDurationMinutes(AppConfig.DefaultWednesdayDurationMinutes);
                    wednesdayEvents.add(c);
                }
                    break;
                case "Thursday": {
                    c.setDurationMinutes(AppConfig.DefaultThursdayDurationMinutes);
                    thursdayEvents.add(c);
                }
                    break;
                case "Friday": {
                    c.setDurationMinutes(AppConfig.DefaultFridayDurationMinutes);
                    fridayEvents.add(c);
                }
                    break;
            }
            
            unplacedEvents.remove(c);
        }
                
    @FXML public void timeGridDragAccept(DragEvent e) {
        e.acceptTransferModes(TransferMode.ANY);
        e.consume();
    }
    
    @FXML public void createCourseAction() {
        openCourseCreator();
    }
    
    @FXML public void unplacedListEditCourse() {
        openCourseEditor(unplacedEvents.get(eventList.getSelectionModel().getSelectedIndex()));
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
        if(!disabled) b.setOnDragDetected((me) -> timeGridDragStart(me));
        
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
        unplacedEvents.sort((Course c1, Course c2) -> c1.getCourseNumber().compareTo(c2.getCourseNumber()));
        eventList.setItems(FXCollections.observableArrayList(unplacedEvents));
    }
    
    /**
     * Updates the Time Grid according to the events in the (day)Events ArrayLists
     */
    private void updateTimeGrid() {
        updateDayTimeGrid(mondayBox, mondayEvents, "Monday");
        updateDayTimeGrid(tuesdayBox, tuesdayEvents, "Tuesday");
        updateDayTimeGrid(wednesdayBox, wednesdayEvents, "Wednesday");
        updateDayTimeGrid(thursdayBox, thursdayEvents, "Thursday");
        updateDayTimeGrid(fridayBox, fridayEvents, "Friday");
    }
    
    /**
     * Given a VBox and an ArrayList of events, will update the VBox to show events
     * @param dayBox
     * @param dayEvents 
     */
    private void updateDayTimeGrid(VBox dayBox, ArrayList<Course> dayEvents, String id) {
        dayEvents.sort(Comparator.naturalOrder());
        ObservableList<Node> dayChildren = dayBox.getChildren();
        dayChildren.clear();
        Course previous = new Course();
            previous.setStartTime(minimumTime);
            previous.setEndTime(minimumTime);
        for(int i = 0; i < dayEvents.size(); ++i) {
            Course e = dayEvents.get(i);
            int distFromPrevious = (int)previous.getEndTime().until(e.getStartTime(), ChronoUnit.MINUTES);
            if(distFromPrevious > 0) {
                dayChildren.add(getTimeGridEvent("", distFromPrevious, true));
            }
            dayChildren.add(getTimeGridEvent(e.getFormattedText(), e.getDurationMinutes(), false));
            previous = e;
        }
        for(Node n : dayChildren) n.setId(id);
    }
    
  //--Window--------------------------------------------------------------------
    
    private final Stage courseCreatorStage = new Stage();
    private CreateClassFXMLController courseCreatorController;
    
    private void openCourseCreator() {
        courseCreatorController.clearFields();
        courseCreatorStage.showAndWait();
        Course c = courseCreatorController.getCourse();
        if(c.getCourseNumber().equals("")) return;
        unplacedEvents.add(c);
        updateUnplacedEvents();
    }
    public void closeCourseCreator() {
        courseCreatorStage.close();
    }
    
    private final Stage courseEditorStage = new Stage();
    private EditClassFXMLController courseEditorController;
    
    private void openCourseEditor(Course c) {
        courseEditorController.clearFields();
        courseEditorController.edit(c);
        courseEditorStage.showAndWait();
        
        updateTimeGrid();
        updateUnplacedEvents();
    }
    public void closeCourseEditor() {
        courseEditorStage.close();
    }
    
  //--Init-and-Init-Events------------------------------------------------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializePopups();
            courseCreatorStage.setTitle("Course Scheduler - Course Creator");
            courseEditorStage.setTitle("Course Scheduler - Course Editor");
        
        // Example classes in every standard timeslot
//        for(int i = 0; i < 12; ++i) {
//            mondayEvents.add(new TempEvent(LocalTime.of(8 + i, 0), 60, "Hour " + (i+1)));
//        }
//        for(int i = 0; i < 8; ++i) {
//            tuesdayEvents.add(new TempEvent(LocalTime.of(8 + (int)Math.floor(i * 1.5), (i % 2) * 30), 90, "Hour " + (i+1)));
//        }
        
        //My schedule
        Course c1 = new Course("", "INTD-405", "", "", "", false, "Smith", "", false, 0, 0, 0, "", LocalTime.of(15, 15), LocalTime.of(16, 5), false, null, "", null);
        Course c2 = new Course("", "PHIL-215", "", "", "", false, "Krull", "", false, 0, 0, 0, "", LocalTime.of(12, 45), LocalTime.of(13, 35), false, null, "", null);
        Course c3 = new Course("", "MUS-131", "", "", "", false, "Lynn", "", false, 0, 0, 0, "", LocalTime.of(17, 30), LocalTime.of(18, 20), false, null, "", null);
        Course c4 = new Course("", "MUS-130", "", "", "", false, "Lynn", "", false, 0, 0, 0, "", LocalTime.of(16, 30), LocalTime.of(17, 20), false, null, "", null);
        Course c5 = new Course("", "CPTR-422", "", "", "", false, "Mitchell", "", false, 0, 0, 0, "", LocalTime.of(11, 45), LocalTime.of(12, 35), false, null, "", null);
        mondayEvents.add(c1);
        mondayEvents.add(c2);
        tuesdayEvents.add(c3);
        mondayEvents.add(c4);
        mondayEvents.add(c5);
        mondayEvents.forEach((c) -> {
            wednesdayEvents.add(c.clone());
            fridayEvents.add(c.clone());
        });
        tuesdayEvents.forEach((c) -> {
            thursdayEvents.add(c.clone());
        });
        
        updateTimeGrid();
        
        Course c6 = new Course();
            c6.setCourseNumber("UNPLD-001");
            c6.setFacultyLname("STAFF");
        unplacedEvents.add(c6);
        
        updateUnplacedEvents();
    }
    
    private void initializePopups() {
        try {
            FXMLLoader loader = new FXMLLoader(FinalProject.class.getResource("CreateClassFXML.fxml"));
            Parent root = loader.load();
            courseCreatorController = loader.getController();
            
            courseCreatorStage.setScene(new Scene(root));
            courseCreatorController.setParent(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(FinalProject.class.getResource("EditClassFXML.fxml"));
            Parent root = loader.load();
            courseEditorController = loader.getController();
            
            courseEditorStage.setScene(new Scene(root));
            courseEditorController.setParent(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

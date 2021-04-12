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
    
//    private ArrayList<Course> mondayEvents = new ArrayList<>();
//    private ArrayList<Course> tuesdayEvents = new ArrayList<>();
//    private ArrayList<Course> wednesdayEvents = new ArrayList<>();
//    private ArrayList<Course> thursdayEvents = new ArrayList<>();
//    private ArrayList<Course> fridayEvents = new ArrayList<>();
    
    private ArrayList<Course> placedEvents = new ArrayList<>();
    
    private ArrayList<Course> unplacedEvents = new ArrayList<>();
    
  //--GUI-Actions---------------------------------------------------------------
    
    @FXML public void timeGridEventAction(ActionEvent e) {
        if(e.getSource() instanceof Button) {
            Button b = (Button) e.getSource();
            openCourseViewer(getPlacedEventFromText(b.getText()));
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
            Course c = getPlacedEventFromText(source.getText());
                if(c == null) return;
            Course.Day fromDay = getDayFromId(source.getId());
                if(fromDay == null) return;
            c.setScheuledTimes(fromDay, new LocalTime[]{LocalTime.of(hour, minute), LocalTime.of(hour, minute).plusMinutes(c.getDurationMinutes(fromDay))});

            VBox destination = (VBox) e.getSource();
            if(!source.getId().equals(destination.getId())) {
                Course.Day toDay = getDayFromId(destination.getId());
                shiftEventDay(fromDay, toDay, c);
            }
        }
            private Course getPlacedEventFromText(String text) {
                for(Course c : placedEvents) {
                    if(c.getFormattedText().equals(text)) return c;
                }
                return null;
            }
            private void shiftEventDay(Course.Day from, Course.Day to, Course c) {
                c.setScheuledTimes(to, c.getScheduledTimes(from));
                c.removeDay(from);
            }
        private void unplacedListDrop(DragEvent e) {
            Course c = (Course)eventList.getSelectionModel().getSelectedItems().get(0);
            int dropPos = (int) e.getY();
                dropPos = (int) (Math.round(dropPos / minuteIncrementSnap) * minuteIncrementSnap);
            int hours = (int) (dropPos / 60) + minimumTime.getHour();
            int minutes = (int) (dropPos % 60);
            
//            System.out.println("Dropping from Unplaced at y" + dropPos + " (" + hours + ":" + minutes + ")");
            
            int duration = 0;
            Course.Day day = getDayFromId(((VBox)e.getSource()).getId());
                if(day == null) return;
            switch(day) {
                case M: duration = AppConfig.DefaultMondayDurationMinutes;
                    break;
                case T: duration = AppConfig.DefaultTuesdayDurationMinutes;
                    break;
                case W: duration = AppConfig.DefaultWednesdayDurationMinutes;
                    break;
                case R: duration = AppConfig.DefaultThursdayDurationMinutes;
                    break;
                case F: duration = AppConfig.DefaultFridayDurationMinutes;
                    break;
            }
            
            c.setStartTime(day, LocalTime.of(hours, minutes));
            c.setDurationMinutes(day, duration);
            placedEvents.add(c);
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
    private Button getTimeGridEvent(String text, int minutes, boolean disabled, boolean locked) {
        Button b = new Button();
        
        // Default settings
        b.setMaxWidth(Double.MAX_VALUE);
        b.setMinHeight(0);
        b.setMaxHeight(Double.MAX_VALUE);
        b.setFont(new Font(timeGridFontSize));
        b.setAlignment(Pos.TOP_LEFT);
        b.setOnAction((ae) -> timeGridEventAction(ae));
        if(!disabled && !locked) b.setOnDragDetected((me) -> timeGridDragStart(me));
        
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
     * Returns the Course.Day with a value equal to the id. If no day matches the id, this returns null.
     * @param id
     * @return 
     */
    private Course.Day getDayFromId(String id) {
        for(Course.Day d : Course.Day.values()) {
            if(d.getValue().equals(id)) return d;
        }
        return null;
    }
    
    /**
     * Updates the Time Grid according to the events in the (day)Events ArrayLists
     */
    private void updateTimeGrid() {
        for(Course.Day d : Course.Day.values()) {
            ArrayList<Course> dayEvents = new ArrayList<>();
            for(Course c : placedEvents) {
                if(c.getScheduledTimes(d) != null) dayEvents.add(c);
            }
            switch(d) {
                case M: updateDayTimeGrid(d, mondayBox, dayEvents);
                    break;
                case T: updateDayTimeGrid(d, tuesdayBox, dayEvents);
                    break;
                case W: updateDayTimeGrid(d, wednesdayBox, dayEvents);
                    break;
                case R: updateDayTimeGrid(d, thursdayBox, dayEvents);
                    break;
                case F: updateDayTimeGrid(d, fridayBox, dayEvents);
                    break;
            }
        }
    }
    
    /**
     * Given a VBox and an ArrayList of events, will update the VBox to show events
     * @param dayBox
     * @param dayEvents 
     */
    private void updateDayTimeGrid(Course.Day d, VBox dayBox, ArrayList<Course> dayEvents) {
        dayEvents.sort((Course c1, Course c2) -> c1.getStartTime(d).compareTo(c2.getStartTime(d)));
        ObservableList<Node> dayChildren = dayBox.getChildren();
        dayChildren.clear();
        LocalTime prevEnd = minimumTime;
        
        for(int i = 0; i < dayEvents.size(); ++i) {
            Course e = dayEvents.get(i);
            int distFromPrevious = (int)prevEnd.until(e.getStartTime(d), ChronoUnit.MINUTES);
            if(distFromPrevious > 0) {
                dayChildren.add(getTimeGridEvent("", distFromPrevious, true, false));
            }
            dayChildren.add(getTimeGridEvent(e.getFormattedText(), e.getDurationMinutes(d), false, e.getLockedCourse()));
            prevEnd = e.getEndTime(d);
        }
        for(Node n : dayChildren) n.setId(d.getValue());
    }
    
  //--Window--------------------------------------------------------------------
    
    private final Stage courseCreatorStage = new Stage();
    private CreateClassFXMLController courseCreatorController;
    
    public void openCourseCreator() {
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
    
    public void openCourseEditor(Course c) {
        courseEditorController.clearFields();
        courseEditorController.edit(c);
        courseEditorStage.showAndWait();
        
        updateTimeGrid();
        updateUnplacedEvents();
    }
    public void closeCourseEditor() {
        courseEditorStage.close();
    }
    
    private final Stage courseViewerStage = new Stage();
    private ViewClassFXMLController courseViewerController;
    
    public void openCourseViewer(Course c) {
        courseViewerController.clearFields();
        courseViewerController.view(c);
        courseViewerStage.show();
    }
    public void closeCourseViewer() {
        courseViewerStage.close();
    }
    
  //--Init-and-Init-Events------------------------------------------------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializePopups();
            courseCreatorStage.setTitle("Course Scheduler - Course Creator");
            courseEditorStage.setTitle("Course Scheduler - Course Editor");
            courseViewerStage.setTitle("Course Scheduler - Course Viewer");
        
        // Example classes in every standard timeslot
//        for(int i = 0; i < 12; ++i) {
//            mondayEvents.add(new TempEvent(LocalTime.of(8 + i, 0), 60, "Hour " + (i+1)));
//        }
//        for(int i = 0; i < 8; ++i) {
//            tuesdayEvents.add(new TempEvent(LocalTime.of(8 + (int)Math.floor(i * 1.5), (i % 2) * 30), 90, "Hour " + (i+1)));
//        }
        
        //My schedule
        Course c1 = new Course();
            c1.setCourseNumber("INTD-405");
            c1.setFacultyLname("Smith");
            c1.setScheuledTimes(Course.Day.M, new LocalTime[]{LocalTime.of(15, 15), LocalTime.of(16, 5)});
            c1.setScheuledTimes(Course.Day.W, new LocalTime[]{LocalTime.of(15, 15), LocalTime.of(16, 5)});
            c1.setScheuledTimes(Course.Day.F, new LocalTime[]{LocalTime.of(15, 15), LocalTime.of(16, 5)});
        Course c2 = new Course();
            c2.setCourseNumber("PHIL-215");
            c2.setFacultyLname("Krull");
            c2.setScheuledTimes(Course.Day.M, new LocalTime[]{LocalTime.of(12, 45), LocalTime.of(13, 35)});
            c2.setScheuledTimes(Course.Day.W, new LocalTime[]{LocalTime.of(12, 45), LocalTime.of(13, 35)});
            c2.setScheuledTimes(Course.Day.F, new LocalTime[]{LocalTime.of(12, 45), LocalTime.of(13, 35)});
        Course c3 = new Course();
            c3.setCourseNumber("MUS-131");
            c3.setFacultyLname("Lynn");
            c3.setScheuledTimes(Course.Day.T, new LocalTime[]{LocalTime.of(17, 30), LocalTime.of(18, 20)});
            c3.setScheuledTimes(Course.Day.R, new LocalTime[]{LocalTime.of(17, 30), LocalTime.of(18, 20)});
        Course c4 = new Course();
            c4.setCourseNumber("MUS-130");
            c4.setFacultyLname("Lynn");
            c4.setScheuledTimes(Course.Day.M, new LocalTime[]{LocalTime.of(16, 30), LocalTime.of(17, 20)});
            c4.setScheuledTimes(Course.Day.W, new LocalTime[]{LocalTime.of(16, 30), LocalTime.of(17, 20)});
            c4.setScheuledTimes(Course.Day.F, new LocalTime[]{LocalTime.of(16, 30), LocalTime.of(17, 20)});
        Course c5 = new Course();
            c5.setCourseNumber("CPTR-422");
            c5.setFacultyLname("Mitchell");
            c5.setScheuledTimes(Course.Day.M, new LocalTime[]{LocalTime.of(11, 45), LocalTime.of(12, 35)});
            c5.setScheuledTimes(Course.Day.W, new LocalTime[]{LocalTime.of(11, 45), LocalTime.of(12, 35)});
            c5.setScheuledTimes(Course.Day.F, new LocalTime[]{LocalTime.of(11, 45), LocalTime.of(12, 35)});
        
        placedEvents.add(c1);
        placedEvents.add(c2);
        placedEvents.add(c3);
        placedEvents.add(c4);
        placedEvents.add(c5);
        
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
        try {
            FXMLLoader loader = new FXMLLoader(FinalProject.class.getResource("ViewClassFXML.fxml"));
            Parent root = loader.load();
            courseViewerController = loader.getController();
            
            courseViewerStage.setScene(new Scene(root));
            courseViewerController.setParent(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

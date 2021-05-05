/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.File;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author rewil
 */
public class FXMLDocumentController implements Initializable {
    
    private final double minuteIncrementSnap = 5.0;
    
    private final LocalTime minimumTime = LocalTime.of(8, 0);
    private final LocalTime maximumTime = LocalTime.of(20, 0);
    
    @FXML ListView eventList;
    @FXML VBox mondayBox;
    @FXML VBox tuesdayBox;
    @FXML VBox wednesdayBox;
    @FXML VBox thursdayBox;
    @FXML VBox fridayBox;
    
    private ArrayList<Course> placedEvents = new ArrayList<>();
    private ArrayList<Course> unplacedEvents = new ArrayList<>();
    private AppConfig config = new AppConfig();
    
    private TimeGridFormatter tgf = new TimeGridFormatter(this, minimumTime);
    private FilterGUI filter = new FilterGUI();
    
    private String saveFilepath = "";
    private String saveFilename = "";
    private ExtensionFilter saveExtension = new ExtensionFilter("Save Files", "*" + SaveFile.filetype);
    private String exportFilepath = "";
    private String exportFilename = "";
    private CSVCreator csv = new CSVCreator();
    private ExtensionFilter exportExtension = new ExtensionFilter("CSV Files", "*.csv");
    
  //--GUI-Actions---------------------------------------------------------------
    
    @FXML public void listViewAction() {
        Course c = (Course) eventList.getSelectionModel().getSelectedItem();
        openCourseViewer(c);
    }
    @FXML public void timeGridEventAction(ActionEvent e) {
        if(e.getSource() instanceof Button) {
            Button b = (Button) e.getSource();
            openCourseViewer(getPlacedEventFromText(b.getText()));
        }
    }
    @FXML public void timeGridDeleteAction(Button source) {
        Course.Day day = this.getDayFromId(source.getId());
        for(Course c : placedEvents) {
            if(c.getFormattedText().equals(source.getText())) {
                c.removeDay(day);
                if(c.getScheduledTimes().isEmpty()) {
                    unplacedEvents.add(c);
                    placedEvents.remove(c);
                    return;
                }
            }
        }
        updateTimeGrid();
        updateUnplacedEvents();
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
            if(e.getSource() instanceof ListView) {
                toUnplacedListDrop(e);
                return;
            }
            double move = e.getY();
                move = Math.round(move / minuteIncrementSnap) * minuteIncrementSnap;
            int hour = (int) (move / 60) + minimumTime.getHour();
            int minute = (int) (move % 60);

            Button source = (Button)e.getGestureSource();
            Course c = getPlacedEventFromText(source.getText());
                if(c == null) return;
            Course.Day fromDay = getDayFromId(source.getId());
                if(fromDay == null) return;
            LocalTime[] fromTimes = c.getScheduledTimes(fromDay);
            
            c.setScheuledTimes(fromDay, new LocalTime[]{LocalTime.of(hour, minute), LocalTime.of(hour, minute).plusMinutes(c.getDurationMinutes(fromDay))});

            VBox destination = (VBox) e.getSource();
            Course.Day toDay = fromDay;
            if(!source.getId().equals(destination.getId())) {
                toDay = getDayFromId(destination.getId());
                shiftEventDay(fromDay, toDay, c);
            }
            
            ArrayList<Course> conflicts = getConflicts(c, toDay);
            if(!conflicts.isEmpty()) {
                String content = "";
                    for(Course check : conflicts) {
                        content += check.getFormattedText() + " " + Arrays.toString(check.getScheduledTimes(toDay)) + "\n";
                    }
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setHeaderText("This event will conflict with other placed event(s)");
                    a.setContentText(content);
                    a.showAndWait();
                if(a.getResult().equals(ButtonType.CANCEL)) {
                    shiftEventDay(toDay, fromDay, c);
                    c.setScheuledTimes(fromDay, fromTimes);
                }
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
            
            Course.Day day = getDayFromId(((VBox)e.getSource()).getId());
                if(day == null) return;
                
            ArrayList<DayGroup> groupOptions = config.getGroups(day);
            Alert a = new Alert(Alert.AlertType.NONE);
                String cancel = "Just this";
                for(DayGroup g : groupOptions) a.getButtonTypes().add(new ButtonType(g.toString()));
                a.getButtonTypes().add(new ButtonType(cancel));
                a.setHeaderText("Day Grouping");
                a.setContentText("Select the group you want to auto place or 'Just this'");
                a.showAndWait();
            DayGroup group = getChosenGroup(a.getResult(), groupOptions);
            if(group == null) {
                group = groupOptions.get(0);
                group = new DayGroup(new Course.Day[] {day}, group.getDuration());
            }
            
            for(Course.Day d : group.getDays()) {
                c.setStartTime(d, LocalTime.of(hours, minutes));
                c.setDurationMinutes(d, group.getDuration());
            }
            
            // Check Conflicts
            ArrayList<Course> conflicts = getConflicts(c, group.getDays().toArray(new Course.Day[group.getDays().size()]));
            if(!conflicts.isEmpty()) {
                String content = "";
                    for(Course check : conflicts) {
                        LocalTime[] times = null;
                        for(Course.Day d : group.getDays()) {
                            if(c.conflictsWith(check, d)) {
                                times = check.getScheduledTimes(d);
                                break;
                            }
                        }
                        if(times != null) content += check.getFormattedText() + " " + Arrays.toString(times) + "\n";
                    }
                a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setHeaderText("This event will conflict with other placed events");
                    a.setContentText(content);
                    a.showAndWait();
                if(a.getResult().equals(ButtonType.CANCEL)) {
                    for(Course.Day d : group.getDays()) c.removeDay(d);
                    return;
                }
            }
            
            placedEvents.add(c);
            unplacedEvents.remove(c);
        }
            private DayGroup getChosenGroup(ButtonType t, ArrayList<DayGroup> groupOptions) {
                for(DayGroup g : groupOptions) if(t.getText().equals(g.toString())) return g;
                return null;
            }
        private void toUnplacedListDrop(DragEvent e) {
            String text = ((Button) e.getGestureSource()).getText();
            for(Course c : placedEvents) {
                if(c.getFormattedText().equals(text)) {
                    c.setScheduledTimes(new HashMap<>());
                    unplacedEvents.add(c);
                    placedEvents.remove(c);
                    return;
                }
            }
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
    
    @FXML public void menuSaveAction() {
        if(saveFilepath.isEmpty()) {
            menuSaveAsAction();
            return;
        }
        saveAction();
    }
    @FXML public void menuSaveAsAction() {
        FileChooser fc = new FileChooser();
            if(!saveFilepath.isEmpty()) {
                fc.setInitialDirectory(new File(saveFilepath.replaceAll(saveFilename, "")));
                fc.setInitialFileName(saveFilename);
            }
            fc.getExtensionFilters().add(saveExtension);
        File file = fc.showSaveDialog(new Stage());
            if(file == null) return;
            saveFilepath = file.getAbsolutePath();
            saveFilename = file.getName();
        
        saveAction();
    }
        private void saveAction() {
            SaveFile sf = new SaveFile(placedEvents, unplacedEvents, config);
            sf.writeFile(saveFilepath);
        }
    @FXML public void menuLoadAction() {
        FileChooser fc = new FileChooser();
            if(!saveFilepath.isEmpty()) {
                fc.setInitialDirectory(new File(saveFilepath.replaceAll(saveFilename, "")));
                fc.setInitialFileName(saveFilename);
            }
            fc.getExtensionFilters().add(saveExtension);
            
        File file = fc.showOpenDialog(new Stage());
            if(file == null) return;
            saveFilepath = file.getAbsolutePath();
            saveFilename = file.getName();
            
            loadAction();
    }
        private void loadAction() {
            SaveFile sf = SaveFile.readObject(saveFilepath);
                if(sf == null) return;
                
            this.placedEvents = sf.getPlacedEvents();
            this.unplacedEvents = sf.getUnPlacedEvents();
            this.config = sf.getAppConfig();
            
            updateTimeGrid();
            updateUnplacedEvents();
        }
    @FXML public void menuExportAction() {
        if(exportFilepath.isEmpty()) {
            menuExportAsAction();
            return;
        }
        exportAction();
    }
    @FXML public void menuExportAsAction() {
        FileChooser fc = new FileChooser();
            if(!exportFilepath.isEmpty()) {
                fc.setInitialDirectory(new File(exportFilepath.replaceAll(exportFilename, "")));
                fc.setInitialFileName(exportFilename);
            }
            fc.getExtensionFilters().add(exportExtension);
        File file = fc.showSaveDialog(new Stage());
            if(file == null) return;
            exportFilepath = file.getAbsolutePath();
            exportFilename = file.getName();
        
        exportAction();
    }
        private void exportAction() {
            csv.exportCourses(placedEvents, exportFilepath);
        }
    
    @FXML public void changeFilterAction() {
        this.openFilterGUIViewer();
    }
    
    @FXML public void dayGroupMenuAction() {
        openDayGroupViewer();
    }
    
  //--Utiliy--------------------------------------------------------------------
    
    /**
     * Returns a list of courses a given course (c) conflicts with
     * @param c - the course we are checking
     * @param d - the days the course will meet
     * @return 
     */
    private ArrayList<Course> getConflicts(Course c, Course.Day... d){
        ArrayList<Course> conflicts = new ArrayList<>();
        for(Course current: placedEvents){
            for(Course.Day D: d){
             if(conflicts.contains(current)){
                 continue;
             }//only adds each course once
             if(c.conflictsWith(current,D)){
                conflicts.add(current);
             }//if they conflict
            }//inner loops through days
        }//loops through the placed courses
        return conflicts;
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
                if(c.getScheduledTimes(d) != null && filter.matches(c)) dayEvents.add(c);
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
        
        dayChildren.addAll(tgf.formatDay(d, dayEvents));
    }
    
    /**
     * Entirely removes a course, not just placed instances of it
     * @param c 
     */
    public void deleteCourse(Course c) {
        placedEvents.remove(c);
        unplacedEvents.remove(c);
        
        updateTimeGrid();
        updateUnplacedEvents();
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
        
        if(unplacedEvents.contains(c) && !c.getScheduledTimes().isEmpty()) {
            unplacedEvents.remove(c);
            placedEvents.add(c);
        } else if (placedEvents.contains(c) && c.getScheduledTimes().isEmpty()) {
            placedEvents.remove(c);
            unplacedEvents.add(c);
        }
        
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
    
    private final Stage filterGUIStage = new Stage();
    private FilterGUIController filterGUIController;
    
    public void openFilterGUIViewer() {
        filterGUIController.setAtttributes(placedEvents, filter);
        filterGUIStage.showAndWait();
        updateTimeGrid();
    }
    public void closeFilterGUI() {
        filterGUIStage.close();
    }
    
    private final Stage dayGroupStage = new Stage();
    private DayGroupFXMLController dayGroupController;
    
    public void openDayGroupViewer() {
        dayGroupController.setConfig(config);
        dayGroupStage.showAndWait();
    }
    public void closeDayGroupViewer() {
        dayGroupStage.close();
    }
    
  //--Init-and-Init-Events------------------------------------------------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializePopups();
            courseCreatorStage.setTitle("Course Scheduler - Course Creator");
            courseEditorStage.setTitle("Course Scheduler - Course Editor");
            courseViewerStage.setTitle("Course Scheduler - Course Viewer");
            filterGUIStage.setTitle("Course Scheduler - Filter Editor");
            dayGroupStage.setTitle("Course Scheduler - Day Group Editor");
            
        config.addGroup(new DayGroup(new Course.Day[] {Course.Day.M, Course.Day.W, Course.Day.F}, 50));
        config.addGroup(new DayGroup(new Course.Day[] {Course.Day.T, Course.Day.R}, 75));
        
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
            c5.setFacultyFname("Robin");
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
        try {
            FXMLLoader loader = new FXMLLoader(FinalProject.class.getResource("FilterGUI.fxml"));
            Parent root = loader.load();
            filterGUIController = loader.getController();
            
            filterGUIStage.setScene(new Scene(root));
            filterGUIController.setStage(filterGUIStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(FinalProject.class.getResource("DayGroupFXML.fxml"));
            Parent root = loader.load();
            dayGroupController = loader.getController();
            
            dayGroupStage.setScene(new Scene(root));
//            dayGroupController.setStage(dayGroupStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

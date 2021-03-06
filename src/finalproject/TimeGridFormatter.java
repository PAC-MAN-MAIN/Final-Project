
package finalproject;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author rewil
 */
public class TimeGridFormatter {
    
    private final FXMLDocumentController parent;
    private final LocalTime minimumTime;
    private final LocalTime maximumTime;
    
    private final double hourHeight = 60.0;
    private final double timeGridFontSize = 15.0;
    
    private double ratio = 1.0;
    
    public TimeGridFormatter(FXMLDocumentController parent, LocalTime minimumTime, LocalTime maximumTime) {
        this.parent = parent;
        this.minimumTime = minimumTime;
        this.maximumTime = maximumTime;
    }
    
    public ArrayList<Node> formatDay(Course.Day d, ArrayList<Course> dayEvents, AppConfig a) {
        ArrayList<Node> nodes = new ArrayList<>();
        LocalTime previousEnd = minimumTime;
        
        for(int i = 0; i < dayEvents.size(); ++i) {
            if(dayEvents.get(i).getStartTime(d).isAfter(previousEnd)) nodes.add(getTimeGridBuffer(previousEnd.until(dayEvents.get(i).getStartTime(d), ChronoUnit.MINUTES)));
            
            ArrayList<Course> conflicts = new ArrayList<>();
            if(i+1 < dayEvents.size() && dayEvents.get(i).conflictsWith(dayEvents.get(i+1), d)) {
                conflicts.add(dayEvents.get(i));
                while(i+1 < dayEvents.size() && dayEvents.get(i).conflictsWith(dayEvents.get(i+1), d)) {
                    conflicts.add(dayEvents.get(++i));
                }
            }
            if(conflicts.isEmpty()) {
                nodes.add(getTimeGridCourse(dayEvents.get(i), d, a));
                previousEnd = dayEvents.get(i).getEndTime(d);
            } else {
                nodes.add(getTimeGridConflictNode(conflicts, d, conflicts.get(0).getStartTime(d), a));
                for(Course c : conflicts) {
                    if(c.getEndTime(d).isAfter(previousEnd)) previousEnd = c.getEndTime(d);
                }
            }
            conflicts.clear();
        }
        nodes.add(getTimeGridBuffer(previousEnd.until(maximumTime, ChronoUnit.MINUTES)));
        
        return nodes;
    }
    
    private Button getTimeGridBuffer(long duration) {
        return getTimeGridNode("", (int)duration, true, false, "",null, null);
    }
    private Button getTimeGridCourse(Course c, Course.Day d, AppConfig a) {
        Color color = c.getColor();
        if(color.equals(Color.WHITE)){
            color = a.getColor(c);
            if(color == null){
                color = Color.WHITE;
            }
        }
       //System.out.println(c.getColorString());
        
        return getTimeGridNode(c.getFormattedText(), c.getDurationMinutes(d), false, c.getLockedCourse(), d.getValue(), color.toString().substring(2,8), color);
    }
    
    /**
     * Provides a button which can be put into a day's VBox to represent an event
     * @param text The text that will show up on the button
     * @param minutes The length of time the event lasts
     * @param disabled - If the button should be disabled (true for fillers)
     * @return 
     */
    private Button getTimeGridNode(String text, int minutes, boolean disabled, boolean locked, String id,String courseColor, Color colorObject) {
        Button b = new Button();
        
        // Default settings
        b.setMaxWidth(Double.MAX_VALUE);
        b.setMinHeight(3);
        b.setMaxHeight(Double.MAX_VALUE);
        b.setFont(new Font(timeGridFontSize));
        b.setAlignment(Pos.TOP_LEFT);
        b.setWrapText(true);
        b.setId(id);
        b.setOnAction((ae) -> parent.timeGridEventAction(ae));
        if(!disabled){
            b.setStyle("-fx-background-color: #"+ courseColor);
            Color textColor = Color.BLACK;
            double luma = 0.299 * colorObject.getRed() + 0.587 * colorObject.getGreen() + 0.114 * colorObject.getBlue(); // Magic numbers are stolen from stackoverflow and are tested and proven really good values and are only used here
            if(luma < 0.5) textColor = Color.WHITE;
            b.setTextFill(textColor);
        }
        if(!disabled && !locked) b.setOnDragDetected((me) -> parent.timeGridDragStart(me));
        if(!disabled) {
            MenuItem deleteAction = new MenuItem("Delete Instance");
                deleteAction.setOnAction((e) -> parent.timeGridDeleteAction(b));
            ContextMenu menu = new ContextMenu();
                menu.getItems().add(deleteAction);
            b.setOnContextMenuRequested((e) -> menu.show(b, e.getScreenX(), e.getScreenY()));
        }
        
        // Custom settings
            double factor = minutes / 60d;
            double height = hourHeight * factor * ratio;
        b.setPrefHeight(height);
        b.setText(text);
        b.setDisable(disabled);
        b.setVisible(!disabled);
        
        return b;
    }
    
    /**
     * Provides an HBox containing the minimal number of VBoxes needed to show all given courses without directly overlapping them, and the given courses in the form of Buttons within said VBoxes. (This HBox can be placed in a day's VBox to represent these events)
     * @param conflictingCourses
     * @param d
     * @param previousEnd
     * @return 
     */
    private HBox getTimeGridConflictNode(ArrayList<Course> conflictingCourses, Course.Day d, LocalTime previousEnd, AppConfig a) {
        HBox container = new HBox();
        ArrayList<ArrayList<Course>> sets = new ArrayList<>();
        
        // Identify sets of conflicts to minimize number of columns needed
        for(Course c : conflictingCourses) {
            boolean placed = false;
            for(ArrayList<Course> list : sets) {
                if(!list.get(list.size() - 1).conflictsWith(c, d)) {
                    list.add(c);
                    placed = true;
                    break;
                }
            }
            if(!placed) {
                sets.add(new ArrayList<>());
                sets.get(sets.size() - 1).add(c);
            }
        }
        
        // Create and fill VBoxes
        ArrayList<VBox> boxes = new ArrayList<>();
        for(int i = 0; i < sets.size(); ++i) {
            VBox box = new VBox();
            boxes.add(box);
            
            ArrayList<Node> items = new ArrayList<>();
            LocalTime prevEnd = previousEnd;
            for(Course c : sets.get(i)) {
                if(c.getStartTime(d).isAfter(prevEnd)) items.add(getTimeGridBuffer(prevEnd.until(c.getStartTime(d), ChronoUnit.MINUTES)));
                items.add(getTimeGridCourse(c, d, a));
                prevEnd = c.getEndTime(d);
            }
            
            box.getChildren().addAll(items);
            container.getChildren().add(box);
        }
        
        return container;
    }
    
    public void modifyRatio(double r) {
        ratio *= r;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author rewil
 */
public class FXMLDocumentController implements Initializable {
    
    private final double hourHeight = 65.0;
    private final double timeGridFontSize = 15.0;
    
    @FXML ListView eventList;
    @FXML VBox mondayBox;
    @FXML VBox tuesdayBox;
    @FXML VBox wednesdayBox;
    @FXML VBox thursdayBox;
    @FXML VBox fridayBox;
    
  //--GUI-Actions---------------------------------------------------------------
    
    @FXML
    public void timeGridEventAction(ActionEvent e) {
        if(e.getSource() instanceof Button) {
            Button b = (Button) e.getSource();
            System.out.println(b.getText());
        }
    }
    
  //--Utiliy--------------------------------------------------------------------
    
    /**
     * Provides a button which can be put into a day's VBox to represent an event
     * @param text The text that will show up on the button
     * @param minutes The length of time the event lasts
     * @param disabled - If the button should be disabled (true for fillers)
     * @return 
     */
    private Button getEvent(String text, int minutes, boolean disabled) {
        Button b = new Button();
        
        // Default settings
        b.setMaxWidth(Double.MAX_VALUE);
        b.setMinHeight(0);
        b.setMaxHeight(Double.MAX_VALUE);
        b.setFont(new Font(timeGridFontSize));
        b.setOnAction((ae) -> timeGridEventAction(ae));
        
        // Custom settings
        b.setPrefHeight((hourHeight * ((double)minutes / 60d)) - 0.5);
        b.setText(text);
        b.setDisable(disabled);
        
        
        return b;
    }
    
  //----------------------------------------------------------------------------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tuesdayBox.getChildren().add(getEvent("TEST-150, Mitchell", 50, false));
        tuesdayBox.getChildren().add(getEvent("", 10, true));
        tuesdayBox.getChildren().add(getEvent("TEST-175, Brumbaugh-Smith", 75, false));
        tuesdayBox.getChildren().add(getEvent("", 45, true));
        tuesdayBox.getChildren().add(getEvent("TEST-220, Brauch", 120, false));
        
    }
    
}

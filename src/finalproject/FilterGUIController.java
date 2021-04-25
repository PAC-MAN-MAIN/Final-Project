/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rewil
 */
public class FilterGUIController implements Initializable {

    private ArrayList<Course> placedList;
    private FilterGUI filter;
    private Stage stage;

    @FXML AnchorPane pane;
    @FXML ComboBox attributeCombo;
    @FXML ComboBox filterCombo;
    @FXML Button removeButton;
    
    @FXML public void pickAttributeAction() {
        filterCombo.setItems(FXCollections.observableArrayList(filter.getOptions(getType(attributeCombo), placedList)));
        filterCombo.setDisable(false);
    }
    @FXML public void pickFilterAction() {
        FilterGUI.Type type = getType(attributeCombo);
        Object filter = getFilter(filterCombo);
            if(filter == null) return;
        this.filter.putFilter(type, filter);
        attributeCombo.setDisable(true);
        removeButton.setDisable(false);
        addRow();
    }
    @FXML public void removeFilterAction() {
        filter.removeFilter(getType(attributeCombo));
        attributeCombo.getSelectionModel().clearSelection();
        attributeCombo.setDisable(false);
        filterCombo.getSelectionModel().clearSelection();
        filterCombo.setDisable(true);
        removeButton.setDisable(true);
        
        if(!extraAttributeBoxes.isEmpty()) {
            attributeCombo.getSelectionModel().select(getType(extraAttributeBoxes.get(0)));
                attributeCombo.setDisable(false);
                attributeCombo.setItems(extraAttributeBoxes.get(0).getItems());
                filterCombo.setItems(extraFilterBoxes.get(0).getItems());
            filterCombo.getSelectionModel().select(getFilter(extraFilterBoxes.get(0)));
                if(!attributeCombo.getSelectionModel().isEmpty()) {
                    filterCombo.setDisable(false);
                    removeButton.setDisable(false);
                    if(!filterCombo.getSelectionModel().isEmpty()) attributeCombo.setDisable(true);
                }
            pane.getChildren().removeAll(extraAttributeBoxes.remove(0), extraFilterBoxes.remove(0), extraRemoveButtons.remove(0));
            formatRows();
        }
    }
    
    private ArrayList<ComboBox> extraAttributeBoxes = new ArrayList<>();
    private ArrayList<ComboBox> extraFilterBoxes = new ArrayList<>();
    private ArrayList<Button> extraRemoveButtons = new ArrayList<>();
    
    private void addRow() {
        if(filter.getUnfilteredTypes().isEmpty()) return;
        ComboBox attributeBox = new ComboBox();
            attributeBox.setOnAction((e) -> pickAttributeAction(attributeBox));
            extraAttributeBoxes.add(attributeBox);
        ComboBox filterBox = new ComboBox();
            filterBox.setOnAction((e) -> pickFilterAction(filterBox));
            extraFilterBoxes.add(filterBox);
        Button removeButton = new Button();
            removeButton.setOnAction((e) -> removeFilterAction(removeButton));
            extraRemoveButtons.add(removeButton);
        
        formatRows();
        pane.getChildren().addAll(attributeBox, filterBox, removeButton);
    }
        private void formatRows() {
            for(ComboBox attributeBox : extraAttributeBoxes) {
                    ObservableList list = FXCollections.observableArrayList(filter.getUnfilteredTypes());
                        list.add(attributeBox.getSelectionModel().getSelectedItem());
                attributeBox.setItems(list);
                attributeBox.setPromptText(attributeCombo.getPromptText());
                attributeBox.setPrefWidth(attributeCombo.getPrefWidth());
                attributeBox.setLayoutX(attributeCombo.getLayoutX());
                attributeBox.setLayoutY((extraAttributeBoxes.indexOf(attributeBox) + 1) * (attributeCombo.getLayoutY() + attributeCombo.getHeight()) + 10);
            }
            for(ComboBox filterBox : extraFilterBoxes) {
                filterBox.setDisable(true);
                filterBox.setPromptText(filterCombo.getPromptText());
                filterBox.setPrefWidth(filterCombo.getPrefWidth());
                filterBox.setLayoutX(filterCombo.getLayoutX());
                filterBox.setLayoutY((extraFilterBoxes.indexOf(filterBox) + 1) * (filterCombo.getLayoutY() + filterCombo.getHeight()) + 10);
            }
            for(Button removeButton : extraRemoveButtons) {
                removeButton.setDisable(true);
                removeButton.setText(this.removeButton.getText());
                removeButton.setPrefWidth(this.removeButton.getPrefWidth());
                removeButton.setLayoutX(this.removeButton.getLayoutX());
                removeButton.setLayoutY((extraRemoveButtons.indexOf(removeButton) + 1) * (this.removeButton.getLayoutY() + this.removeButton.getHeight()) + 10);
            }
            double height = (attributeCombo.getPrefHeight() + (attributeCombo.getLayoutY() * 1.5)) * (extraAttributeBoxes.size() + 2) + 10;
            stage.setHeight(height);
            pane.setPrefHeight(height);
        }
    private void pickAttributeAction(ComboBox cb) {
        int index = extraAttributeBoxes.indexOf(cb);
        ComboBox fb = extraFilterBoxes.get(index);
        fb.setItems(FXCollections.observableArrayList(filter.getOptions(getType(cb), placedList)));
        fb.setDisable(false);
    }
    private void pickFilterAction(ComboBox cb) {
        int index = extraFilterBoxes.indexOf(cb);
        ComboBox ab = extraAttributeBoxes.get(index);
        FilterGUI.Type type = getType(ab);
        Object filter = getFilter(cb);
            if(filter == null) return;
        this.filter.putFilter(type, filter);
        ab.setDisable(true);
        extraRemoveButtons.get(index).setDisable(false);
        addRow();
    }
    private void removeFilterAction(Button b) {
        int index = extraRemoveButtons.indexOf(b);
        filter.removeFilter(getType(extraAttributeBoxes.get(index)));
        pane.getChildren().removeAll(extraAttributeBoxes.remove(index), extraFilterBoxes.remove(index), extraRemoveButtons.remove(index));
        formatRows();
    }
    
    private FilterGUI.Type getType(ComboBox box) {
        return (FilterGUI.Type) box.getSelectionModel().getSelectedItem();
    }
    private Object getFilter(ComboBox box) {
        return box.getSelectionModel().getSelectedItem();
    }
    
  //----------------------------------------------------------------------------
    
    public void setAtttributes(ArrayList<Course> placedList, FilterGUI filter) {
        this.placedList = placedList;
        this.filter = filter;
        
        if(attributeCombo.getSelectionModel().isEmpty()) attributeCombo.setItems(FXCollections.observableArrayList(filter.getUnfilteredTypes()));
    }
    public void clearGUI() {
        while(!extraAttributeBoxes.isEmpty()) {
            removeFilterAction();
        }
    }
    
    public void setStage(Stage s) {
        stage = s;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

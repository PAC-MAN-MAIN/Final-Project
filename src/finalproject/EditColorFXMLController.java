
package finalproject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author levil
 */
public class EditColorFXMLController implements Initializable {
    private AppConfig config = null; 
    private Stage stage;
    private ArrayList<String> professors = new ArrayList<>();
    private String selectedProf = null;
    private Color selectedColor = null; 
    
    @FXML AnchorPane colorPane;
    @FXML ComboBox professorList;
    @FXML ColorPicker colorPicker;
    @FXML Button applyBtn;
    
    
    public void setStage(Stage s) {
        stage = s;
    } 
    
    public void setConfig(AppConfig appConfig){
        this.config = appConfig;
    }
    
    public void addProf(String p){
        professors.add(p);
    }
    
    @FXML public void selectColorAction(){
        if(colorPicker.getValue() != null){
            selectedColor = colorPicker.getValue();
        }
    }
    
    @FXML public void applyBtnAction(){
        if(config.contains(selectedProf)){
            config.editColor(selectedProf,selectedColor);
        }else{
            config.setProfessorColor(selectedProf,selectedColor);        
        }
    }
    
    @FXML public void selectProfessorAction(){
        selectedProf = professorList.getValue().toString();
        if(config.getColor(selectedProf) != null){
            colorPicker.setValue(config.getColor(selectedProf));
        }else{
            colorPicker.setValue(Color.WHITE);
        }
    }
    
    public void setProfessors(ArrayList<Course> placedEvents){
        for(Course c: placedEvents){
          if(professors.contains(c.getFacultyLname())){
              continue;
          }
          if(c.getFacultyLname() != "" || c.getFacultyLname() != null){
            addProf(c.getFacultyLname());
          }
        }
        professorList.getItems().clear();
        professorList.getItems().addAll(professors);
    }
    


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }
}
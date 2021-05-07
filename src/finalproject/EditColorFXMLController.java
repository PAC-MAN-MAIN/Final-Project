
package finalproject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author levil
 */
public class EditColorFXMLController implements Initializable {
    private Stage stage;
    private ArrayList<String> professors;
    
    @FXML AnchorPane colorPane;
    @FXML ComboBox professorList;
    @FXML ColorPicker colorPicker;
    
    
    public void setStage(Stage s) {
        stage = s;
    } 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }
}


package finalproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 *
 * @author rewil
 */
public class SaveFile implements Serializable{
    
    private ArrayList<Course> placedEvents;
    private ArrayList<Course> unPlacedEvents;
    private AppConfig appConfig;
    
    public static final String filetype = ".ScheduleSaveFile";
    
    
    public SaveFile(ArrayList<Course> placedEvents, ArrayList<Course> unplacedEvents, AppConfig appConfig) {
        this.placedEvents = placedEvents;
        this.unPlacedEvents = unplacedEvents;
        this.appConfig = appConfig;
    }

    public ArrayList<Course> getPlacedEvents() {
        return placedEvents;
    }

    public ArrayList<Course> getUnPlacedEvents() {
        return unPlacedEvents;
    }
    
    public AppConfig getAppConfig() {
        return appConfig;
    }
    
    /**
     * Include filename and filetype at end of filepath
     * @param filepath
     * @param object
     * @param name
     * @return 
     */
    public boolean writeFile(String filepath) {
        try {
            File file = new File(filepath);
//            if(file.exists()) {
//                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
//                    a.setHeaderText("File Already Exists");
//                    a.setContentText("This will overwrite the existing save file");
//                    a.showAndWait();
//                if(a.getResult().equals(ButtonType.CANCEL)) return false;
//            }
            file.createNewFile();
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream o = new ObjectOutputStream(f);
            
            o.writeObject(this);
            
            o.close();
            f.close();
            
        } catch(FileNotFoundException e) {
            System.out.println("File Not Found - Write");
            e.printStackTrace();
            return false;
        } catch(IOException e) {
            System.out.println("Error Initializing Stream - Write");
            e.printStackTrace();
            return false;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /**
     * Include filename and filetype in filepath
     * @param filepath
     * @param name
     * @return 
     */
    public static SaveFile readObject(String filepath) {
        SaveFile output = null;
        try {
            File file = new File(filepath);
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream o = new ObjectInputStream(f);
            
            output = (SaveFile) o.readObject();
            
            o.close();
            f.close();
            
        } catch(FileNotFoundException e) {
            System.out.println("File Not Found - Read");
            e.printStackTrace();
            return null;
        } catch(IOException e) {
            System.out.println("IOException - Read");
            Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Imported file is not a compatible Save File");
                a.setContentText("This may be caused by it representing an older version of the application or by selecting the wrong file");
                a.showAndWait();
            e.printStackTrace();
            return null;
        } catch(ClassNotFoundException e) {
            System.out.println("Class Not Found - Read");
            e.printStackTrace();
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return output;
    }
    
}

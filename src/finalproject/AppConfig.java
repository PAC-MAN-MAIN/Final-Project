package finalproject;

import finalproject.Course.Day;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;


/**
 *
 * @author rewil
 */
public class AppConfig implements Serializable{
    
    private ArrayList<DayGroup> groups = new ArrayList<>();
    private Map<String, Color> professorColors = new HashMap<>();
    
    public void addGroup(DayGroup g) {
        groups.add(g);
    }
    public ArrayList<DayGroup> getGroups() {
        return groups;
    }
    /**
     * Returns all registered DayGroups that include the given Day
     * @param d
     * @return 
     */
    public ArrayList<DayGroup> getGroups(Day d) {
        ArrayList<DayGroup> out = new ArrayList<>();
            for(DayGroup g : groups) if(g.getDays().contains(d)) out.add(g);
        return out;
    }
    
    public void removeGroup(DayGroup g) {
        groups.remove(g);
    }
    
    /**
     * 
     * @param p - the professor
     * @return - color associated 
     */
    public Color getColor(String p){
        return professorColors.get(p);
    }
    
    public void setProfessorColor(String p, Color c){
        professorColors.put(p, c);
    }
    
    public void editColor(String p, Color c){
        professorColors.replace(p, c);
    }
    
    public boolean contains(String p){
        return professorColors.containsKey(p);
    }
    
}

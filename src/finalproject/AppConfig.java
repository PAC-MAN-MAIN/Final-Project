package finalproject;

import finalproject.Course.Day;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author rewil
 */
public class AppConfig implements Serializable{

    public static int DefaultMondayDurationMinutes = 50;
    public static int DefaultTuesdayDurationMinutes = 75;
    public static int DefaultWednesdayDurationMinutes = 50;
    public static int DefaultThursdayDurationMinutes = 75;
    public static int DefaultFridayDurationMinutes = 50;
    
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
    
}

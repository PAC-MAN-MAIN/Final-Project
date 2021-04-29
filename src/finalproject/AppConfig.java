package finalproject;

import finalproject.Course.Day;
import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author rewil
 */
public class AppConfig implements Serializable{
    
    private ArrayList<DayGroup> groups = new ArrayList<>();
    
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
    
}

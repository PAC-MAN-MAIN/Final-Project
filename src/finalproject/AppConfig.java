package finalproject;

import finalproject.Course.Day;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javafx.scene.paint.Color;


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
    
    public AppConfig(){
        professorColors.put("", Color.WHITE);
    }
    
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
    public String getFullName(Course c){
        return c.getFacultyFname()+" "+c.getFacultyLname();
    }
    public Color getColor(Course c){
        return professorColors.get(getFullName(c));
    }
    
    public Color getColor(String s){
        return professorColors.get(s);
    }
    
    public void setProfessorColor(Course c, Color cl){
        professorColors.put(getFullName(c), cl);
    }
    
    public void setProfessorColor(String s, Color c){
        professorColors.put(s,c);
    }
    
    public void editColor(String s, Color cl){
        professorColors.replace(s, cl);
    }
    
    public boolean isRegistered(Course c){
        return professorColors.containsKey(getFullName(c));
    }
    
    public Set<String> getProfessors(){
        return professorColors.keySet();
    }
    
    public void registerCourse(Course c){
        if(isRegistered(c)){
            return;
        } else{
            setProfessorColor(c,getRandomColor());
        }
    }
    
    public Color getRandomColor(){
        Random R = new Random();
        Color c = Color.WHITE;
        while(professorColors.values().contains(c)){
            c = Color.color(R.nextDouble(), R.nextDouble(), R.nextDouble());
        }
        return c;
    }
}

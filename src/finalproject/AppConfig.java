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
    
    private ArrayList<DayGroup> groups = new ArrayList<>();
    private Map<String, SerialColor> professorColors = new HashMap<>();
    
    public AppConfig(){
        professorColors.put("", new SerialColor(Color.WHITE));
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
    
    
    
    private String getFullName(Course c){
        return c.getFacultyFname()+" "+c.getFacultyLname();
    }
    public Color getColor(Course c){
        SerialColor sc = professorColors.get(getFullName(c));
        if(sc == null) return null;
        return sc.getColor();
    }
    
    public Color getColor(String s){
        SerialColor sc = professorColors.get(s);
        if(sc == null) return null;
        return sc.getColor();
    }
    
    public void setProfessorColor(Course c, Color cl){
        professorColors.put(getFullName(c), new SerialColor(cl));
    }
    
    public void setProfessorColor(String s, Color c){
        professorColors.put(s, new SerialColor(c));
    }
    
    public void editColor(String s, Color cl){
        professorColors.replace(s, new SerialColor(cl));
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
            SerialColor temp = new SerialColor(c);
        while(professorColors.values().contains(temp)){
            c = Color.color(R.nextDouble(), R.nextDouble(), R.nextDouble());
            temp = new SerialColor(c);
        }
        return c;
    }
    
}

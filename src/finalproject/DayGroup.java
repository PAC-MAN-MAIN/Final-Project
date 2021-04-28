package finalproject;

import finalproject.Course.Day;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author rewil
 */
public class DayGroup implements Serializable {
    private final Day[] days;
    private final int duration;

    public DayGroup(Day[] days, int duration) {
        this.days = days;
        this.duration = duration;
    }

    public ArrayList<Day> getDays() {
        return new ArrayList<>(Arrays.asList(days));
    }
    public int getDuration() {
        return duration;
    }
    
    @Override
    public String toString() {
        String out = "";
        for(Day d : days) out += d + " ";
        out += "(" + duration + ")";
        
        return out;
    }

}
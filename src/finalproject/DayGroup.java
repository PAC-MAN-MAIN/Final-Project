package finalproject;

import finalproject.Course.Day;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author rewil
 */
public class DayGroup implements Serializable {
    private final Day[] days;
    private final LocalTime[] startTimes;
    private final int duration;
    private final String name;

    public DayGroup(Day[] days, LocalTime[] startTimes, int duration) {
        this(days, startTimes, duration, "");
    }
    public DayGroup(Day[] days, LocalTime[] startTimes, int duration, String name) {
        this.days = days;
        this.startTimes = startTimes;
        this.duration = duration;
        this.name = name;
    }
    
    public ArrayList<Day> getDays() {
        return new ArrayList<>(Arrays.asList(days));
    }
    public ArrayList<LocalTime> getStartTimes() {
        return new ArrayList<>(Arrays.asList(startTimes));
    }
    public int getDuration() {
        return duration;
    }
    
    @Override
    public String toString() {
        if(!name.isEmpty()) return name;
        String out = "";
        for(Day d : days) out += d + " ";
        out += "(" + duration + ")";
        
        return out;
    }

}
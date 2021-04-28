
package finalproject;

import finalproject.Course.Day;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author rewil
 */
public class AutoPlacer {
    
    private boolean avoidNameConflicts = true;
    private boolean avoidLockConflicts = true;
    private boolean avoidAllConflicts = true;
    
    private final LocalTime minTime;
    private final LocalTime maxTime;
    
    private final int minuteInc = 5;
    
    public AutoPlacer(LocalTime minTime, LocalTime maxTime) {
        this.minTime = minTime;
        this.maxTime = maxTime;
    }
    
    public void setAvoidNameConflicts() {
        avoidNameConflicts = true;
        avoidLockConflicts = false;
        avoidAllConflicts = false;
    }
    public void setAvoidLockConflicts() {
        avoidNameConflicts = true;
        avoidLockConflicts = true;
        avoidAllConflicts = false;
    }
    public void setAvoidAllConflicts() {
        avoidNameConflicts = true;
        avoidLockConflicts = true;
        avoidAllConflicts = true;
    }
    
    public void place(ArrayList<Course> unplacedList, ArrayList<Course> placedList, AppConfig config) {
        ArrayList<DayGroup> groups = config.getGroups();
        for(Course c : unplacedList) {
            LocalTime time = LocalTime.of(minTime.getHour(), minTime.getMinute());
            boolean placed = false;
            while(!placed) {
                for(DayGroup g : groups) {
                    if(time.plusMinutes(g.getDuration()).isAfter(maxTime)) continue; // Check next group if would extend  past Max Time
                    ArrayList<Course> conflicts = timeConflicts(placedList, time, g.getDuration(), g.getDays().toArray(new Day[g.getDays().size()]));
                    if(avoidAllConflicts && !conflicts.isEmpty()) continue;  // Check next group if no conflicts are allowed
                    boolean issue = false;
                    for(Course con : conflicts) {
                        if(avoidLockConflicts && 
                                con.getLockedCourse()) { // Check next group if Locked classes cannot be conflicted
                            issue = true;
                            break;
                        } 
                        if(avoidNameConflicts && 
                                con.getFacultyLname().equals(c.getFacultyLname()) && 
                                con.getFacultyFname().equals(c.getFacultyFname())) { // Check next group if same Faculty
                            issue = true;
                            break;
                        }
                    }
                    if(!issue) {
                        for(Day d : g.getDays()) {
                            c.setScheuledTimes(d, new LocalTime[] {time, time.plusMinutes(g.getDuration())}); // With no issues accoding to current rules, schedule course for days in DayGroup
                        } placed = true;
                    }
                    if(placed) break;
                }
                if(!placed) {
                    time = time.plusMinutes(minuteInc); // If unable to place at current time, increment by time step
                    if(time.isAfter(maxTime)) break; // If time is later than maxTime, stop trying to place
                }
            }
            if(placed) { // If successfully placed, update lists
                placedList.add(c);
            }
        }
        unplacedList.removeAll(placedList);
    }
        private ArrayList<Course> timeConflicts(ArrayList<Course> placedList, LocalTime time, int duration, Day... days) {
            ArrayList<Course> conflicts = new ArrayList<>();
            for(Course c : placedList) {
                for(Day d : days) {
                    if(conflicts.contains(c)) continue;
                    if(c.getScheduledTimes(d) == null) continue;
                    if(rangeConflicts(time, duration, c.getScheduledTimes(d))) conflicts.add(c);
                } 
            }
            
            return conflicts;
        }
        private boolean rangeConflicts(LocalTime check, int duration, LocalTime[] range) {
            if(range[0].isAfter(range[1])) {
                LocalTime temp = range[1];
                range[1] = range[0];
                range[0] = temp;
            }
            boolean out = false;
            
            for(int i = 0; i < duration + 1 && !out && check.isBefore(maxTime); ++i) {
                LocalTime time = check.plusMinutes(i);
                if(time.isAfter(range[0]) && time.isBefore(range[1])) out = true;
            }
            
            return out;
        }
    
}

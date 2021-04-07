
package finalproject;

import java.time.LocalTime;

/**
 *
 * @author rewil
 */
public class TempEvent implements Comparable<TempEvent> {
    
    private LocalTime startTime;
    private int durationMinutes;
    private String text = "";
    
    public TempEvent(LocalTime startTime, int durationMinutes, String text) {
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.text = text;
    }

    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getText() {
        return text;
    }
    
    public int compareTo(TempEvent other) {
        return startTime.compareTo(other.startTime);
    }
    
    @Override
    public String toString() {
        return text;
    }

}

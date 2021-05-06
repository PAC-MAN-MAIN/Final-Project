
package finalproject;

import finalproject.Course.Day;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rewil
 */
public class AutoPlacer2 {
    
    private final static int sequentialInitialIncrement = 3;
    
    private LocalTime prefTime = LocalTime.of(12, 0);
    
    public void setPrefTime(LocalTime prefTime) {
        this.prefTime = prefTime;
    }
    public LocalTime getPrefTime() {
        return prefTime;
    }
    
    public void place(ArrayList<Course> unplacedList, ArrayList<Course> placedList, AppConfig config) {
        ArrayList<Timeslot> slots = listPossibleSlots(config);
        for(Course c : unplacedList) {
            HashMap<Timeslot, Integer> pointMap = new HashMap<>();
            HashMap<Timeslot, ArrayList<Course>> courseMap = mapPlacedList(placedList);
            for(Timeslot t : slots) {
                pointMap.put(t, scoreTimeslot(t, c, courseMap, slots));
            }
            // Find lowest average time for a timeslot across its group
            double lowestAve = Double.MAX_VALUE;
            DayGroup lowestGroup = null;
            LocalTime lowestTime = null;
            for(DayGroup group : config.getGroups()) {
                for(LocalTime t : group.getStartTimes()) {
                    double temp = 0;
                    for(Day d : group.getDays()) {
                        Timeslot ts = new Timeslot(d, t);
                        temp += pointMap.get(ts);
                    }
                    temp /= group.getDays().size();
                    if(temp < lowestAve) {
                        lowestAve = temp;
                        lowestGroup = group;
                        lowestTime = t;
                    }
                }
            }
            for(Day d : lowestGroup.getDays()) {
                c.setScheuledTimes(d, new LocalTime[] {lowestTime, lowestTime.plusMinutes(lowestGroup.getDuration()) });
            }
            placedList.add(c);
        }
        unplacedList.removeAll(placedList);
    }
        private HashMap<Timeslot, ArrayList<Course>> mapPlacedList(ArrayList<Course> placedList) {
            HashMap<Timeslot, ArrayList<Course>> outMap = new HashMap<>();
            for(Course c : placedList) {
                Map<Day, LocalTime[]> map = c.getScheduledTimes();
                for(Day d : map.keySet()) {
                    LocalTime[] times = map.get(d);
                    for(int i = 0; i < times.length; ++i) {
                        Timeslot t = new Timeslot(d, times[i]);
                        if(outMap.containsKey(t)){
                            outMap.get(t).add(c);
                        } else {
                            ArrayList<Course> al = new ArrayList<>();
                            al.add(c);
                            outMap.put(t, al);
                        }
                    }
                }
            }
            return outMap;
        }
        private ArrayList<Timeslot> listPossibleSlots(AppConfig config) {
            ArrayList<Timeslot> out = new ArrayList<>();
            for(DayGroup group : config.getGroups()) {
                for(Day d : group.getDays()) {
                    for(LocalTime t : group.getStartTimes()) {
                        Timeslot ts = new Timeslot(d, t);
                        if(!out.contains(ts)) out.add(ts);
                    }
                }
            }
            return out;
        }
        /**
         * Slots should already be sorted to avoid needing to sort it every usage
         * @param t
         * @param placedList
         * @param placing
         * @param slots
         * @return 
         */
        private int scoreTimeslot(Timeslot t, Course placing, HashMap<Timeslot, ArrayList<Course>> courseMap, ArrayList<Timeslot> slots) {
            Fibonacci conflicts = new Fibonacci();
            Fibonacci sequential = null;
            ArrayList<Course> list = courseMap.get(t);
                if(list == null) list = new ArrayList<>();
            for(Course c : list) {
                if(facultyMatches(c, placing)) return Integer.MAX_VALUE;
                conflicts.increment();
            }
            int index = slots.indexOf(t);
            int temp = index;
            boolean matching = true;
            while(matching) {
                boolean matched = false;
                if(--temp < 0) break;
                ArrayList<Course> tempList = courseMap.get(slots.get(temp));
                    if(tempList == null) tempList = new ArrayList<>();
                for(Course c : tempList) if(facultyMatches(c, placing)) {
                    matched = true;
                    if(sequential == null) sequential = new Fibonacci(sequentialInitialIncrement);
                    else sequential.increment();
                }
                if(!matched) matching = false;
            }
            matching = true;
            while(matching) {
                boolean matched = false;
                if(++temp > slots.size()) break;
                ArrayList<Course> tempList = courseMap.get(slots.get(temp));
                    if(tempList == null) tempList = new ArrayList<>();
                for(Course c : tempList) if(facultyMatches(c, placing)) {
                    matched = true;
                    if(sequential == null) sequential = new Fibonacci(sequentialInitialIncrement);
                    else sequential.increment();
                }
                if(!matched) matching = false;
            }
            int dist = (int) Math.abs(t.time.until(prefTime, ChronoUnit.HOURS));
            return conflicts.getValue() + (sequential == null ? 0 : sequential.getValue()) + dist;
        }
            private boolean facultyMatches(Course c1, Course c2) {
                return (c1.getFacultyFname() + c1.getFacultyLname()).equals(c2.getFacultyFname() + c2.getFacultyLname());
            }
    
    
    private class Fibonacci {
        private int v1 = 1;
        private int v2 = 1;
        
        public Fibonacci(){}
        public Fibonacci(int increments) {increment(increments);}
        
        public int getValue() {
            return v2;
        }
        public void increment() {
            int temp = v2;
            v2 += v1;
            v1 = temp;
            v2 = Integer.max(v2, v1); // Overflow protection
        }
        public void increment(int iterations) {
            for(int i = 0; i < iterations; ++i) increment();
        }
    }
    private class Timeslot implements Comparable<Timeslot> {
        public final Day day;
        public final LocalTime time;
        
        public Timeslot(Day d, LocalTime t) {
            day = d;
            time = t;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Timeslot) {} else return false;
            Timeslot ts = (Timeslot) obj;
            boolean isEqual = day.equals(ts.day) && time.equals(ts.time);
            return isEqual;
        }

        @Override
        public int hashCode() {
            return day.hashCode() * time.hashCode();
        }
        
        @Override
        public int compareTo(Timeslot t) {
            int out = day.compareTo(t.day);
            if(out == 0) out = time.compareTo(t.time);
            return out;
        }
        
        @Override
        public String toString() {
            return day.getValue() + " " + time;
        }
        
    }    
    
}

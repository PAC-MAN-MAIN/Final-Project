
package finalproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author rewil
 */
public class FilterGUI {
    
    public enum Type {
        name("Faculty Name", String.class),
        adjunct("Is Adjunct", Boolean.class);
        
        private String properName;
        private Class type;
        Type(String name, Class type) {
            properName = name;
            this.type = type;
        }
        
        public Class getType() {
            return type;
        }
        
        @Override
        public String toString() {
            return properName;
        }
    }
    
    private HashMap<Type, Object> filters = new HashMap<>();
    
    public void putFilter(Type t, Object value) {
        if(t.getType().isInstance(value)) {
            filters.put(t, value);
        }
    }
    public void removeFilter(Type t) {
        filters.remove(t);
    }
    
    public boolean matches(Course c) {
        boolean out = true;
        for(Type t : filters.keySet()) {
            switch(t) {
                case name: {
                    String check = c.getFacultyLname() + ", " + c.getFacultyFname();
                    out = out && check.equals(filters.get(t));
                } break;
                case adjunct: {
                    out = out && (c.getAdjunct() == Boolean.class.cast(filters.get(t)));
                } break;
            } if(!out) break;
        }
        return out;
    }
    
    public ArrayList<Type> getUnfilteredTypes() {
        ArrayList<Type> out = new ArrayList<>();
            out.addAll(Arrays.asList(Type.values()));
            out.removeAll(filters.keySet());
        return out;
    }
    public ArrayList getOptions(Type t, ArrayList<Course> courses) {
        if(t == null) return new ArrayList<>();
        ArrayList<Object> list = new ArrayList<>();
        for(Course c : courses) {
            Object value = null;
            switch(t) {
                case name: {
                    value = c.getFacultyLname() + ", " + c.getFacultyFname();
                } break;
                case adjunct: {
                    value = c.getAdjunct();
                }
            }
            boolean placed = false;
            for(Object o : list) if(!placed && o.equals(value)) placed = true;
            if(!placed) list.add(value);
        }
        return list;
    }
    
}

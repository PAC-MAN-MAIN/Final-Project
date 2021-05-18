
package finalproject;

import java.io.Serializable;
import javafx.scene.paint.Color;

/**
 *
 * @author rewil
 */
 public class SerialColor implements Serializable,Comparable<SerialColor> {
        
        private final double[] values;
        
        public SerialColor(Color c) {
            values = new double[] {c.getRed(), c.getGreen(), c.getBlue(), c.getOpacity()};
        }
        
        public Color getColor() {
            return Color.color(values[0], values[1], values[2], values[3]);
        }
        
        @Override
        public int compareTo(SerialColor sc) {
            String s = "" + values[0] + values[1] + values[2] + values[3];
            String s1 = "" + sc.values[0] + sc.values[1] + sc.values[2] + sc.values[3];
            return s.compareTo(s1);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof SerialColor) {
                SerialColor sc = (SerialColor) obj;
                return toString().equals(sc.toString());
            } else return false;
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }
        
        @Override
        public String toString() {
            return getColor().toString();
        }
        
    }

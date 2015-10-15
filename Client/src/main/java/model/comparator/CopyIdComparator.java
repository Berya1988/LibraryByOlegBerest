package model.comparator;

import model.Copy;
import java.util.Comparator;

/**
 * Created by Oleg on 10.09.2015.
 */
public class CopyIdComparator {
    public static Comparator<Copy> CopyIdComparator = new Comparator<Copy>(){
        public int compare(Copy copy1, Copy copy2) {
            int id1 = copy1.getId();
            int id2 = copy2.getId();
            return id1-id2;
        }
    };
}

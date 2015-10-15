package model.comparator;

import model.Copy;
import java.util.Comparator;

/**
 * Created by Oleg on 10.09.2015.
 */
public class CopyBookYearComparator {
    public static Comparator<Copy> CopyBookYearComparator = new Comparator<Copy>(){
        public int compare(Copy copy1, Copy copy2) {
            int year1 = copy1.getBook().getYear();
            int year2 = copy2.getBook().getYear();
            return year1-year2;
        }
    };
}

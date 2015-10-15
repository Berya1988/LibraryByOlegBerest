package model.comparator;

import model.Copy;
import java.util.Comparator;

/**
 * Created by Oleg on 10.09.2015.
 */
public class CopyBookTitleComparator {
    public static Comparator<Copy> CopyBookTitleComparator = new Comparator<Copy>(){
        public int compare(Copy copy1, Copy copy2) {
            String title1 = copy1.getBook().getTitle();
            String title2 = copy2.getBook().getTitle();
            return title1.compareTo(title2);
        }
    };
}

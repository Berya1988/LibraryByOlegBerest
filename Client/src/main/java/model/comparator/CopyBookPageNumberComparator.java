package model.comparator;

import model.Copy;
import java.util.Comparator;

/**
 * Created by Oleg on 10.09.2015.
 */
public class CopyBookPageNumberComparator {
    public static Comparator<Copy> CopyBookPageNumberComparator = new Comparator<Copy>(){
        public int compare(Copy copy1, Copy copy2) {
            int pageNumber1 = copy1.getBook().getPageNumber();
            int pageNumber2 = copy2.getBook().getPageNumber();
            return pageNumber1-pageNumber2;
        }
    };
}

package model.comparator;

import model.Copy;
import java.util.Comparator;

/**
 * Created by Oleg on 10.09.2015.
 */
public class CopyBookAuthorComparator {
    public static Comparator<Copy> CopyBookAuthorComparator = new Comparator<Copy>(){
        public int compare(Copy copy1, Copy copy2) {
            String author1 = copy1.getBook().getAuthor();
            String author2 = copy2.getBook().getAuthor();
            return author1.compareTo(author2);
        }
    };
}

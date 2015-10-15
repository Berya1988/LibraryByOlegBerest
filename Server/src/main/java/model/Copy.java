package model;

/**
 * Copy is the public class
 * represents one instance of book in the library
 *
 * @author      Oleg Berest
 * @version     %I%, %G%
 */

public class Copy {
    private Book book;
    private int id;
    private boolean isPresent;
    /**
     * Class constructor.
     *  @param book       contains information about book
     *  @param number     contains information about book id
     *  @param isPresent  determines if the book is present in the library
     *                    in current moment
     *  @see Book
     */
    public Copy(Book book, int number, boolean isPresent){
        this.book = book;
        this.id = number;
        this.isPresent = isPresent;
    }
    /**
     * Sets author's name
     * @param book        contains information about book
     */
    public void setBook(Book book){
        this.book = book;
    }
    /**
     * Returns book with all information about it.
     * @return book with all information about it
     * @see Book
     */
    public Book getBook() {
        return book;
    }
    /**
     * Sets id of the book
     * @param id        contains information about unique number of the book inn the library
     */
    public void setId(int id){
        this.id = id;
    }
    /**
     * Returns id of the book.
     * @return id of the book
     */
    public int getId() {
        return id;
    }
    /**
     * Sets presence of the book in the library
     * @param isPresent      determines if the book is present in the library
     *                       in current moment
     */
    public void setPresent(boolean isPresent){
        this.isPresent = isPresent;
    }
    /**
     * Returns presence of the book in the library.
     * @return presence of the book in the library
     */
    public boolean getPresent() {
        return isPresent;
    }
    /**
     * Gives full information about copy of the book in the library
     */
    @Override
    public String toString() {
        return "Copy{" + "book=" + book + ", id=" + id + ", isPresent=" + isPresent + '}';
    }
}

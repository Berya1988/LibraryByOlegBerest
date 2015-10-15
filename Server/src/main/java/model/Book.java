package model;

/**
 * Book is the public class
 * represents real book
 *
 * @author      Oleg Berest
 * @version     %I%, %G%
 */

public class Book {
    private String author;
    private String title;
    private int pageNumber;
    private int year;

    /**
     * Class constructor.
     *  @param author       contains information about author's name and surname
     *  @param title        contains information about book title
     *  @param pageNumber   contains information about amount of pages in the book
     *  @param year         contains information about year when book was published
     */
    public Book(String author, String title, int pageNumber, int year){
        this.author = author;
        this.title = title;
        this.pageNumber = pageNumber;
        this.year = year;
    }

    /**
     * Sets author's name
     * @param author        contains information about author's name and surname
     */
    public void setAuthor(String author){
        this.author = author;
    }
    /**
     * Returns author's name.
     * @return author's name
     */
    public String getAuthor() {
        return author;
    }
    /**
     * Sets author's name
     * @param title        contains information about book title
     */
    public void setTitle(String title){
        this.title = title;
    }
    /**
     * Returns book title.
     * @return book title
     */
    public String getTitle() {
        return title;
    }
    /**
     * Sets author's name
     * @param pageNumber        contains information about amount of pages in the book
     */
    public void setPageNumber(int pageNumber){
        this.pageNumber = pageNumber;
    }
    /**
     * Returns amount of pages in the book.
     * @return amount of pages in the book
     */
    public int getPageNumber() {
        return pageNumber;
    }
    /**
     * Sets author's name
     * @param year        contains information about year when book was published
     */
    public void setYear(int year){
        this.year = year;
    }
    /**
     * Returns year when book was published.
     * @return year when book was published
     */
    public int getYear() {
        return year;
    }
    /**
     * Gives full information about book
     */
    @Override
    public String toString() {
        return "Book{" + "author='" + author + '\'' + ", title='" + title + '\'' + ", pageNumber=" + pageNumber + ", year=" + year + '}';
    }
}


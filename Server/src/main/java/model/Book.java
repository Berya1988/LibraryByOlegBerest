package model;

/**
 * Created by Oleg on 26.08.2015.
 */
public class Book {
    private String author;
    private String title;
    private int pageNumber;
    private int year;

    public Book(String author, String title, int pageNumber, int year){
        this.author = author;
        this.title = title;
        this.pageNumber = pageNumber;
        this.year = year;
    }

    /**
     * Method sets author name
     * @param author name of author
     */
    public void setAuthor(String author){
        this.author = author;
    }
    /**
     *
     * @return author name
     */
    public String getAuthor() {
        return author;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setPageNumber(int pageNumber){
        this.pageNumber = pageNumber;
    }
    public int getPageNumber() {
        return pageNumber;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getYear() {
        return year;
    }
    @Override
    public String toString() {
        return "Book{" + "author='" + author + '\'' + ", title='" + title + '\'' + ", pageNumber=" + pageNumber + ", year=" + year + '}';
    }
}


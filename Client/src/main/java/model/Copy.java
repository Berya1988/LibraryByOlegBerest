package model;
/**
 * Created by Oleg on 26.08.2015.
 * Library: Copy of a book (inventary number(id), book, present or not in the library),
 */
public class Copy {
    private Book book;
    private int id;
    private boolean isPresent;

    public Copy(Book book, int number, boolean isPresent){
        this.book = book;
        this.id = number;
        this.isPresent = isPresent;
    }

    public void setBook(Book book){
        this.book = book;
    }
    public Book getBook() {
        return book;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setPresent(boolean isPresent){
        this.isPresent = isPresent;
    }
    public boolean getPresent() {
        return isPresent;
    }

    @Override
    public String toString() {
        return book + "{Identificational  id: " + id + ". Presence: " + isPresent + "}.";
    }
}


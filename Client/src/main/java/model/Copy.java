package model;

import java.util.Comparator;

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

    public static Comparator<Copy> CopyIdComparator = new Comparator<Copy>(){
        @Override
        public int compare(Copy copy1, Copy copy2) {
            int id1 = copy1.getId();
            int id2 = copy2.getId();
            return id1-id2;
        }};

    public static Comparator<Copy> CopyBookTitleComparator = new Comparator<Copy>(){
        @Override
        public int compare(Copy copy1, Copy copy2) {
            String title1 = copy1.getBook().getTitle();
            String title2 = copy2.getBook().getTitle();
            return title1.compareTo(title2);
        }};

    public static Comparator<Copy> CopyBookAuthorComparator = new Comparator<Copy>(){
        @Override
        public int compare(Copy copy1, Copy copy2) {
            String author1 = copy1.getBook().getAuthor();
            String author2 = copy2.getBook().getAuthor();
            return author1.compareTo(author2);
        }};

    public static Comparator<Copy> CopyBookYearComparator = new Comparator<Copy>(){
        @Override
        public int compare(Copy copy1, Copy copy2) {
            int year1 = copy1.getBook().getYear();
            int year2 = copy2.getBook().getYear();
            return year1-year2;
        }};

    public static Comparator<Copy> CopyBookPageNumberComparator = new Comparator<Copy>(){
        @Override
        public int compare(Copy copy1, Copy copy2) {
            int pageNumber1 = copy1.getBook().getPageNumber();
            int pageNumber2 = copy2.getBook().getPageNumber();
            return pageNumber1-pageNumber2;
        }};
}


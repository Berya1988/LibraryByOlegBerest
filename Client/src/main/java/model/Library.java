package model;

/**
 * Created by Oleg on 26.08.2015.
 */
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;

public class Library implements Serializable {
    private LinkedList<Copy> books = new LinkedList<Copy>();
    private HashSet<Integer> ids = new HashSet<Integer>();
    private int length = 0;

    public LinkedList<Copy> getBooks() {
        return books;
    }

    public void add(Copy element){
        if (!ids.contains(element.getId())) {
            ids.add(element.getId());
            length++;
            books.add(element);
        }
    }
    public void addAt(int index, Copy element){
        if (!ids.contains(element.getId())) {
            ids.add(element.getId());
            length++;
            books.add(index, element);
        }
    }

    public void remove(int index) {
        ids.remove(books.get(index).getId());
        books.remove(index);
        length--;
    }

    public Copy getElement(int index){
        return books.get(index);
    }

    public int getIndexOfElementById(int id){
        for (int i = 0; i < length(); i++) {
            if(getElement(i).getId() == id){
                return books.indexOf(getElement(i));
            }
        }
        return -1;
    }

    public boolean checkId(String checkedId) {
        return ids.contains(Integer.parseInt(checkedId));
    }

    @Override
    public String toString() {
        return "Library{" +
                "books =" + books +
                '}';
    }

    public int length(){
        return length;
    }

}


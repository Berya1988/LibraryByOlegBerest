package model;
/**
 * Library is the public class
 * which can hold some number of books using LinkedList
 *
 * @author      Oleg Berest
 * @version     %I%, %G%
 * @see Copy
 * @see java.util.LinkedList
 */
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;

public class Library {
    /**
     * The linked list buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer.
     */
    private LinkedList<Copy> books = new LinkedList<Copy>();
    /**
     * The size of the ArrayList (the number of elements it contains).
     */
    private int length = 0;
    /**
     * The set of unique ids of books in the library.
     */
    private static HashSet<Integer> ids = new HashSet<Integer>();
    /**
     * Class constructor.
    */
    public Library() {
    }
    /**
     * Adds copy of the book to the library
     * checking unique id
     * @param element        copy of the book which is added to the library
     * @see Book
     * @see Copy
     */
    public void add(Copy element){
        if (!ids.contains(element.getId())) {
            ids.add(element.getId());
            length++;
            books.add(element);
        }
    }
    /**
     * Adds copy of the book to the library
     * checking unique id on special position
     * @param index        determines special position of the book in the library
     * @param element      copy of the book which is added to the library
     * @see Book
     * @see Copy
     */
    public void addAt(int index, Copy element){
        if (!ids.contains(element.getId())) {
            ids.add(element.getId());
            length++;
            books.add(index, element);
        }
    }
    /**
     * Returns position of the book in the library using its id
     * @param  id   determines unique number of the book in the library
     * @return      position of the book in the library using its id
     *              or -1 if there is no book with such id
     */
    public int getIndexOfElementById(int id){
        for (int i = 0; i < length(); i++) {
            if(getElement(i).getId() == id){
                return books.indexOf(getElement(i));
            }
        }
        return -1;
    }
    /**
     * Removes copy of the book from the library using its id
     * @param index     determines special position of the book in the library
     */
    public void remove(int index) {
        ids.remove(books.get(index).getId());
        books.remove(index);
        length--;
    }
    /**
     * Returns copy of the book from library by its id
     * @param index     determines unique number of the book in the library
     * @return          copy of the book from library by its id
     */
    public Copy getElement(int index){
        return books.get(index);
    }
    /**
     * Returns information about the library
     * @return information about the library
     */
    @Override
    public String toString() {
        return "Library{" + "books =" + books + '}';
    }
    /**
     * Returns capacity of the library
     * @return capacity of the library
     */
    public int length(){
        return length;
    }
}

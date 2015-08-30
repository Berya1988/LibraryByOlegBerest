package controller;

import model.Copy;
import model.Library;

import javax.swing.table.DefaultTableModel;
import java.util.LinkedList;
/**
 * Created by Oleg on 26.08.2015.
 */
public class ConnectModelWithView {
    private DefaultTableModel model;
    private Library library;

    public ConnectModelWithView(DefaultTableModel model, Library library) {
        this.model = model;
        this.library = library;
    }

    public void setConnection(){
        System.out.println("There are " + library.length() + " books in the clientLibrary while connecting to the form.");
        for (int i = 0; i < library.length(); i++) {
            model.addRow(new Object[]{i + 1,
                    library.getElement(i).getBook().getAuthor(),
                    library.getElement(i).getBook().getTitle(),
                    library.getElement(i).getBook().getYear(),
                    library.getElement(i).getBook().getPageNumber(),
                    library.getElement(i).getId(),
                    library.getElement(i).getPresent()});
        }
    }

    public void setSortedList(LinkedList<Copy> linkedList){
        for (int i = library.length()-1; i >=0 ; i--) {
            model.removeRow(i);
        }
        for (int i = 0; i < linkedList.size(); i++) {
            model.addRow(new Object[]{i + 1,
                    linkedList.get(i).getBook().getAuthor(),
                    linkedList.get(i).getBook().getTitle(),
                    linkedList.get(i).getBook().getYear(),
                    linkedList.get(i).getBook().getPageNumber(),
                    linkedList.get(i).getId(),
                    linkedList.get(i).getPresent()});
        }
    }

    public void setList(LinkedList<Copy> linkedList){
        for (int i = 0; i < linkedList.size(); i++) {
            model.addRow(new Object[]{i + 1,
                    linkedList.get(i).getBook().getAuthor(),
                    linkedList.get(i).getBook().getTitle(),
                    linkedList.get(i).getBook().getYear(),
                    linkedList.get(i).getBook().getPageNumber(),
                    linkedList.get(i).getId(),
                    linkedList.get(i).getPresent()});
        }
    }

    public void updateList(){
        for (int i = library.length()-1; i >=0 ; i--) {
            model.removeRow(i);
        }
        setConnection();
    }

    public static void findWords(LinkedList<Copy> linkedList, DefaultTableModel model){
        for (int i = 0; i < linkedList.size(); i++) {
            model.addRow(new Object[]{i + 1,
                    linkedList.get(i).getBook().getAuthor(),
                    linkedList.get(i).getBook().getTitle(),
                    linkedList.get(i).getBook().getYear(),
                    linkedList.get(i).getBook().getPageNumber(),
                    linkedList.get(i).getId(),
                    linkedList.get(i).getPresent()});
        }
    }
}
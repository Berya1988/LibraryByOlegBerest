package controller;

import model.Copy;
import model.Library;
import org.apache.log4j.Logger;

import javax.swing.table.DefaultTableModel;
import java.util.LinkedList;

/**
 * Created by Oleg on 26.08.2015.
 */
public class ConnectModelWithView {
    private static final Logger log = Logger.getLogger(ConnectModelWithView.class);
    private DefaultTableModel model;
    private Library library;
    private static int sizeOfList;
    private int sizeOfTable;

    public ConnectModelWithView(DefaultTableModel model, Library library) {
        this.model = model;
        this.sizeOfTable = this.model.getRowCount();
        this.library = library;
        this.sizeOfList = library.length();
    }

    public void setConnection() {
        log.info("There are " + library.length() + " books in the clientLibrary while connecting to the form.");
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

    public void setSortedList(LinkedList<Copy> linkedList) {
        for (int i = library.length() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        for (int i = 0; i < linkedList.size(); i++) {
            Copy currentCopy = linkedList.get(i);
            model.addRow(new Object[]{i + 1,
                    currentCopy.getBook().getAuthor(),
                    currentCopy.getBook().getTitle(),
                    currentCopy.getBook().getYear(),
                    currentCopy.getBook().getPageNumber(),
                    currentCopy.getId(),
                    currentCopy.getPresent()});
        }
    }

    public void updateList() {
        for (int i = sizeOfTable - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        setConnection();
    }

    public static void findWords(LinkedList<Copy> linkedList, DefaultTableModel model) {
        for (int i = sizeOfList - 1; i >= 0; i--) {
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

}
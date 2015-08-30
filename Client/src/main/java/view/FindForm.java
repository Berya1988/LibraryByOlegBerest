package view;

import controller.ClientXMLHandler;
import controller.ConnectModelWithView;
import controller.MyCellRendererAlign;
import model.Copy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by Oleg on 30.08.2015.
 */
public class FindForm {
    private JFrame frame;
    public DefaultTableModel model;
    private LinkedList<Copy> sortLibrary;
    private ArrayList<Integer> indeces;
    private JTable table;
    private JScrollPane scrollPane;

    public FindForm(ArrayList<Integer> indeces, String findWord) {
        this.indeces = indeces;

        sortLibrary = new LinkedList<Copy>();
        for (int i = 0; i < indeces.size(); i++) {
            sortLibrary.add(ClientXMLHandler.clientLibrary.getElement(indeces.get(i)));
        }

        model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("№");
        model.addColumn("Authors");
        model.addColumn("Title");
        model.addColumn("Year of publishing");
        model.addColumn("Pages");
        model.addColumn("ID number");
        model.addColumn("Presence");

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(4).setPreferredWidth(10);
        table.getColumnModel().getColumn(5).setPreferredWidth(12);
        table.getColumnModel().getColumn(6).setPreferredWidth(15);
        table.setDefaultRenderer(Object.class, new MyCellRendererAlign());

        ConnectModelWithView.findWords(sortLibrary, model);

        scrollPane = new JScrollPane(table);

        frame = new JFrame("Find results: " + findWord);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(800, 400);
        frame.setVisible(true);
    }
}

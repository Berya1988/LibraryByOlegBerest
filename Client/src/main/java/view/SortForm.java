package view;

/**
 * Created by Oleg on 26.08.2015.
 */
import controller.ClientXMLHandler;
import controller.ConnectModelWithView;
import controller.MyCellRendererAlign;
import model.Copy;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SortForm {

    private JFrame frame;
    private JPanel panel;
    private JComboBox comboBox;
    public DefaultTableModel model;
    private LinkedList<Copy> sortLibrary;
    private JTable table;
    private ConnectModelWithView connection;
    private final String labels[] = {"By title", "By author", "By id", "By year", "By pages" };
    private final DefaultComboBoxModel modelCombo = new DefaultComboBoxModel(labels);

    public SortForm() {
        sortLibrary = new LinkedList<Copy>();
        for (int i = 0; i < ClientXMLHandler.clientLibrary.length(); i++) {
            sortLibrary.add(ClientXMLHandler.clientLibrary.getElement(i));
        }
        Collections.sort(sortLibrary, Copy.CopyBookTitleComparator);

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

        JScrollPane scrollPane = new JScrollPane(table);

        comboBox = new JComboBox(modelCombo);

        panel = new JPanel();
        panel.add(comboBox);

        connection = new ConnectModelWithView(model, ClientXMLHandler.clientLibrary);
        connection.setList(sortLibrary);

        frame = new JFrame("Library: sorting");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(800, 400);
        frame.setVisible(true);

        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                String kindOfSort = itemEvent.getItem().toString();
                if(kindOfSort.equals("By title")) {
                    Collections.sort(sortLibrary, Copy.CopyBookTitleComparator);
                } else if (kindOfSort.equals("By author")) {
                        Collections.sort(sortLibrary, Copy.CopyBookAuthorComparator);
                } else if (kindOfSort.equals("By id")) {
                        Collections.sort(sortLibrary, Copy.CopyIdComparator);
                } else if (kindOfSort.equals("By year")) {
                        Collections.sort(sortLibrary, Copy.CopyBookYearComparator);

                } else if (kindOfSort.equals("By pages")) {
                        Collections.sort(sortLibrary, Copy.CopyBookPageNumberComparator);
                }
                connection.setSortedList(sortLibrary);
            }
        });
    }
}

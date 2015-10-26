package view;

/**
 * Created by Oleg on 26.08.2015.
 */
import controller.ConnectModelWithView;
import controller.ClientXMLHandler;
import controller.MyCellRendererAlign;
import model.ACTIONS;
import model.Copy;
import model.comparator.*;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewForm extends JFrame {
    private static final Logger Log = Logger.getLogger(ViewForm.class);
    private static final long serialVersionUID = 1L;
    private static DefaultTableModel model;
    private  static JButton findBtn;
    private JButton addBtn;
    private JButton editBtn;
    private JButton viewBtn;
    private JButton removeBtn;
    private JButton cancelBtn;

    private Container pane;
    private JTextField textFieldFind;
    private static JTextField textFieldStatus;
    private JLabel labelFind;
    private JLabel labelStatus;
    private JLabel labelSort;
    private JScrollPane scrollPane;
    private GroupLayout layout;
    private static JTable table;
    private JComboBox comboBox;
    private ConnectModelWithView connection;
    private final String labels[] = {"Default", "By title", "By author", "By id", "By year", "By pages" };
    private final DefaultComboBoxModel modelCombo = new DefaultComboBoxModel(labels);
    private Client client;

    private static boolean flagStartSort = false;

    public ViewForm(Client client) {
        this.client = client;
        initializeCompoments();
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelFind)
                        .addComponent(textFieldFind)
                        .addComponent(findBtn))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollPane)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(addBtn)
                                .addComponent(editBtn)
                                .addComponent(viewBtn)
                                .addComponent(labelSort)
                                .addComponent(comboBox)
                                .addComponent(removeBtn)
                                .addComponent(cancelBtn)))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelStatus)
                        .addComponent(textFieldStatus)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelFind)
                        .addComponent(textFieldFind)
                        .addComponent(findBtn))
                .addGroup(layout.createParallelGroup()
                        .addComponent(scrollPane)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addBtn)
                                .addComponent(editBtn)
                                .addComponent(viewBtn)
                                .addComponent(labelSort)
                                .addComponent(comboBox)
                                .addComponent(removeBtn)
                                .addComponent(cancelBtn)))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelStatus)
                        .addComponent(textFieldStatus)));

        layout.linkSize(SwingConstants.HORIZONTAL, findBtn, addBtn, editBtn, viewBtn, removeBtn, cancelBtn, labelSort, comboBox);
        layout.linkSize(SwingConstants.VERTICAL, textFieldFind, textFieldStatus, comboBox);
        pack();
        setTitle("Library:" + client.getNameOfClient());
        setLocationRelativeTo(null);
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeCompoments(){
        textFieldFind = new JTextField(60);
        textFieldStatus = new JTextField(60);
        textFieldStatus.setEditable(false);

        pane = getContentPane();
        layout = new GroupLayout(pane);
        pane.setLayout(layout);

        labelFind = new JLabel("Find what?");
        labelStatus = new JLabel("Status message:");
        labelSort = new JLabel("Sort:");

        createButtons();
        createTable();
        scrollPane = new JScrollPane(table);

        connection = new ConnectModelWithView(model, ClientXMLHandler.getClientLibraryLibrary());

        comboBox = new JComboBox(modelCombo);
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                String kindOfSort = itemEvent.getItem().toString();
                if (kindOfSort.equals("Default")) {
                    client.getPrintWriterOut().println("UPDATEDATABASE" + client.getNameOfClient());
                    //connection.updateList();
                } else if (kindOfSort.equals("By title")) {
                    Collections.sort(ClientXMLHandler.getClientLibraryLibrary().getBooks(), new CopyBookTitleComparator());
                } else if (kindOfSort.equals("By author")) {
                    Collections.sort(ClientXMLHandler.getClientLibraryLibrary().getBooks(), new CopyBookAuthorComparator());
                } else if (kindOfSort.equals("By id")) {
                    Collections.sort(ClientXMLHandler.getClientLibraryLibrary().getBooks(), new CopyIdComparator());
                } else if (kindOfSort.equals("By year")) {
                    Collections.sort(ClientXMLHandler.getClientLibraryLibrary().getBooks(), new CopyBookYearComparator());
                } else if (kindOfSort.equals("By pages")) {
                    Collections.sort(ClientXMLHandler.getClientLibraryLibrary().getBooks(), new CopyBookPageNumberComparator());
                }
                connection.setSortedList(ClientXMLHandler.getClientLibraryLibrary().getBooks());
            }
        });
    }

    private void createTable() {
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

        ConnectModelWithView connectionModel = new ConnectModelWithView(model, ClientXMLHandler.getClientLibraryLibrary());
        connectionModel.setConnection();
    }

    private void createButtons() {
        findBtn = new JButton("Find");
        addBtn = new JButton("Add");
        editBtn = new JButton("Edit");
        viewBtn = new JButton("View");
        removeBtn = new JButton("Remove");
        cancelBtn = new JButton("Cancel");
        cancelBtn.setVisible(false);

        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddEditViewForm form = new AddEditViewForm(ACTIONS.ADD, -1, client);
                form.showForm();
            }
        });

        editBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int index = (Integer) table.getValueAt(table.getSelectedRow(), 5);
                    client.getPrintWriterOut().println("EDITREQUEST" + index + "+" + client.getNameOfClient());
                    Log.info("EDITREQUEST" + index + "+" + client.getNameOfClient());
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "You should select item", "System message", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        });

        removeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               StringBuilder sb = new StringBuilder();
                sb.append("REMOVE");
                int[] arr = table.getSelectedRows();
                int[] arrIndices = new int[arr.length];
                for (int i = 0; i < arr.length; i++) {
                    arrIndices[i] = ClientXMLHandler.getIdOfCopyByIndexOfList(arr[i]);
                }
                for (int i = 0; i < arr.length - 1; i++) {
                    sb.append(arrIndices[i] + "+");
                }
                sb.append(arrIndices[arr.length - 1]);
                client.getPrintWriterOut().println(sb.toString());
            }
        });

        findBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                flagStartSort = true;
                cancelBtn.setVisible(true);
                LinkedList<Copy> findLibrary = new LinkedList<Copy>();
                int count = 0;
                boolean flagFind = false;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        if(model.getValueAt(i, j).toString().contains(textFieldFind.getText())) {
                            findLibrary.add(ClientXMLHandler.getClientLibraryLibrary().getElement(i));
                            sb.append((i + 1) + ", ");
                            flagFind = true;
                            count++;
                            break;
                        }
                    }
                }
                if(textFieldFind.getText().isEmpty()){
                    textFieldStatus.setText("Enter key words for search.");
                }
                else if(flagFind){
                    sb.deleteCharAt(sb.length() - 1);
                    sb.deleteCharAt(sb.length() - 1);
                    textFieldStatus.setText("Find " + count + " match(es) in the next row(s): " + sb.toString());
                    connection = new ConnectModelWithView(model, ClientXMLHandler.getClientLibraryLibrary());
                    connection.findWords(findLibrary, model);
                }
                else {
                    textFieldStatus.setText("There are no matches.");
                }
            }
        });

        viewBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    int selectedRow = table.getSelectedRow();
                    AddEditViewForm form = new AddEditViewForm(ACTIONS.VIEW, selectedRow, client);
                    form.showForm();
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "You should select item", "System message", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                cancelBtn.setVisible(false);
                connection = new ConnectModelWithView(model, ClientXMLHandler.getClientLibraryLibrary());
                connection.updateList();
                flagStartSort = false;
                textFieldFind.setText("");
            }
        });
    }

    public static void setNewStatus(String message) {
        textFieldStatus.setText(message);
    }


    public static DefaultTableModel getModel(){
        return model;
    }

    public static JTable getTable(){
        return table;
    }

}


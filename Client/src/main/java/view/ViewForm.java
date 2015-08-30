package view;

/**
 * Created by Oleg on 26.08.2015.
 */
import controller.ConnectModelWithView;
import controller.ClientXMLHandler;
import controller.MyCellRendererAlign;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewForm extends JFrame {
    private static final long serialVersionUID = 1L;
    public static DefaultTableModel model;
    private JButton findBtn;
    private JButton addBtn;
    private JButton editBtn;
    private JButton viewBtn;
    private JButton removeBtn;
    private JButton sortBtn;

    private Container pane;
    private JTextField textFieldFind;
    private static JTextField textFieldStatus;
    private JLabel labelFind;
    private JLabel labelStatus;
    private JScrollPane scrollPane;
    private GroupLayout layout;
    public static JTable table;

    public ViewForm() {
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
                                .addComponent(sortBtn)
                                .addComponent(removeBtn)))
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
                                .addComponent(sortBtn)
                                .addComponent(removeBtn)))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelStatus)
                        .addComponent(textFieldStatus)));

        layout.linkSize(SwingConstants.HORIZONTAL, findBtn, addBtn, editBtn, viewBtn, removeBtn, sortBtn);
        layout.linkSize(SwingConstants.VERTICAL, textFieldFind, textFieldStatus);
        pack();
        setTitle("Library:" + Client.name);
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

        createButtons();

        labelFind = new JLabel("Find what?");
        labelStatus = new JLabel("Status message:");

        createTable();
        scrollPane = new JScrollPane(table);
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

        ConnectModelWithView connectionModel = new ConnectModelWithView(model, ClientXMLHandler.clientLibrary);
        connectionModel.setConnection();
    }

    private void createButtons() {
        findBtn = new JButton("Find");
        addBtn = new JButton("Add");
        editBtn = new JButton("Edit");
        viewBtn = new JButton("View");
        removeBtn = new JButton("Remove");
        sortBtn = new JButton("Sort");

        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddEditViewForm form = new AddEditViewForm(1, -1);
            }
        });

        editBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                Client.out.println("EDITREQUEST" + ClientXMLHandler.getIdOfCopyByIndexOfList(selectedRow)+ "+" + Client.name);
                System.out.println("EDITREQUEST" + ClientXMLHandler.getIdOfCopyByIndexOfList(selectedRow));
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
                Client.out.println(sb.toString());
            }
        });

        findBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ArrayList<Integer> arrayList = new ArrayList<Integer>();
                int count = 0;
                boolean flagFind = false;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        if(model.getValueAt(i, j).toString().contains(textFieldFind.getText())) {
                            arrayList.add(i);
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
                    FindForm find = new FindForm(arrayList, textFieldFind.getText());
                }
                else {
                    textFieldStatus.setText("There are no matches.");
                }
            }
        });

        sortBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                SortForm sort = new SortForm();
            }
        });

        viewBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int selectedRow = table.getSelectedRow();
                System.out.println(selectedRow);
                AddEditViewForm form = new AddEditViewForm(3, selectedRow);
            }
        });
    }

    public static void setNewStatus(String message) {
        textFieldStatus.setText(message);
    }
}


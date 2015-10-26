package view;

import controller.ClientXMLHandler;
import model.ACTIONS;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Oleg on 27.08.2015.
 */
public class AddEditViewForm extends JFrame {
    private static final Logger Log = Logger.getLogger(AddEditViewForm.class);
    private static final long serialVersionUID = 1L;
    private Container pane;
    private GroupLayout layout;
    private ACTIONS action;                             //1 - add, 2 - edit, 3 - view

    private int indexOfItem;

    private JLabel labelId;
    private JLabel labelAuthor;
    private JLabel labelTitle;
    private JLabel labelPages;
    private JLabel labelYear;
    private JLabel labelPresence;

    private JTextField textFieldId;
    private JTextField textFieldAuthor;
    private JTextField textFieldTitle;
    private JTextField textFieldPages;
    private JTextField textFieldYear;

    private JComboBox comboBox;

    private JButton okBtn;
    private JButton cancelBtn;

    private Date initialDate;
    private Date currentDate;

    private Client client;

    public AddEditViewForm(ACTIONS action, int index, Client client) {
        this.action = action;
        this.indexOfItem = index;
        this.client = client;
    }
    public void showForm() {
        initializeComponents();
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelId)
                        .addComponent(labelAuthor)
                        .addComponent(labelTitle)
                        .addComponent(labelPages)
                        .addComponent(labelYear)
                        .addComponent(labelPresence)
                        .addComponent(okBtn))
                .addGroup(layout.createParallelGroup()
                        .addComponent(textFieldId)
                        .addComponent(textFieldAuthor)
                        .addComponent(textFieldTitle)
                        .addComponent(textFieldPages)
                        .addComponent(textFieldYear)
                        .addComponent(comboBox)
                        .addComponent(cancelBtn)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelId)
                        .addComponent(textFieldId))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelAuthor)
                        .addComponent(textFieldAuthor))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelTitle)
                        .addComponent(textFieldTitle))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelPages)
                        .addComponent(textFieldPages))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelYear)
                        .addComponent(textFieldYear))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelPresence)
                        .addComponent(comboBox))
                .addGroup(layout.createParallelGroup()
                        .addComponent(okBtn)
                        .addComponent(cancelBtn)));

        layout.linkSize(SwingConstants.HORIZONTAL, labelId, labelAuthor, labelTitle, labelPages, labelYear, labelPresence, textFieldId, textFieldAuthor,
                textFieldTitle, textFieldPages, textFieldYear, comboBox, okBtn, cancelBtn);
        layout.linkSize(SwingConstants.VERTICAL, labelId, labelAuthor, labelTitle, labelPages, labelYear, labelPresence, textFieldId, textFieldAuthor,
                textFieldTitle, textFieldPages, textFieldYear, comboBox, okBtn, cancelBtn);

        switch (action) {
            case ADD:
                setTitle("Add new book");
                break;
            case EDIT:
                initialDate = new Date();
                setTitle("Edit book information");
                textFieldId.setText(Integer.toString(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getId()));
                textFieldAuthor.setText(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getBook().getAuthor());
                textFieldTitle.setText(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getBook().getTitle());
                textFieldPages.setText(Integer.toString(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getBook().getPageNumber()));
                textFieldYear.setText(Integer.toString(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getBook().getYear()));
                if(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getPresent()) {
                    comboBox.setSelectedIndex(0);
                }
                else {
                    comboBox.setSelectedIndex(1);
                }
                break;
            case VIEW:
                setTitle("View book information");
                okBtn.setVisible(false);
                textFieldId.setText(Integer.toString(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getId()));
                textFieldId.setEditable(false);
                textFieldAuthor.setText(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getBook().getAuthor());
                textFieldAuthor.setEditable(false);
                textFieldTitle.setText(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getBook().getTitle());
                textFieldTitle.setEditable(false);
                textFieldPages.setText(Integer.toString(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getBook().getPageNumber()));
                textFieldPages.setEditable(false);
                textFieldYear.setText(Integer.toString(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getBook().getYear()));
                textFieldYear.setEditable(false);
                if(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getPresent()) {
                    comboBox.setSelectedIndex(0);
                }
                else {
                    comboBox.setSelectedIndex(1);
                }
                comboBox.setEditable(false);
                comboBox.setEnabled(false);
                break;
            default:
                return;
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setSize(300, 280);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(action.equals(ACTIONS.EDIT))
                    client.getPrintWriterOut().println("EDITFINISH" + ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getId());
            }
        });
    }

    private void initializeComponents() {
        pane = getContentPane();
        layout = new GroupLayout(pane);
        pane.setLayout(layout);

        labelId = new JLabel("Identification number");
        labelAuthor = new JLabel("Authors");
        labelTitle = new JLabel("Title");
        labelPages = new JLabel("Number of pages");
        labelYear = new JLabel("Year");
        labelPresence = new JLabel("Presence");

        textFieldId = new JTextField();
        textFieldId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char a = e.getKeyChar();
                if(!checkEditTime()) {
                    return;
                }
                if (!Character.isDigit(a)) {
                    e.consume();
                }
            }
        });
        textFieldAuthor = new JTextField(8);
        textFieldAuthor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                e.getKeyChar();
                if (checkEditTime()) {
                    initialDate = new Date();
                }
            }
        });
        textFieldTitle = new JTextField(8);
        textFieldTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                e.getKeyChar();
                if (checkEditTime()) {
                    initialDate = new Date();
                }
            }
        });
        textFieldPages = new JTextField();
        textFieldPages.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                e.getKeyChar();
                if (checkEditTime()) {
                    initialDate = new Date();
                }
            }
        });
        textFieldPages.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char a = e.getKeyChar();
                if(checkEditTime()) {
                    initialDate = new Date();
                }
                if (!Character.isDigit(a)) {
                    e.consume();
                }
            }
        });
        textFieldYear = new JTextField();
        textFieldYear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char a = e.getKeyChar();
                if(checkEditTime()) {
                    initialDate = new Date();
                }
                if (!Character.isDigit(a)) {
                    e.consume();
                }
            }
        });

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(true);
        model.addElement(false);
        comboBox = new JComboBox(model);
        okBtn = new JButton("Ok");
        cancelBtn = new JButton("Cancel");

        okBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!checkEditTime()) {
                    return;
                }

                String[] answers = {textFieldId.getText().trim(), textFieldAuthor.getText().trim(), textFieldTitle.getText().trim(),
                        textFieldPages.getText().trim(), textFieldYear.getText().trim(), comboBox.getSelectedItem().toString()};

                if(textFieldId.getText().isEmpty() || textFieldAuthor.getText().isEmpty() || textFieldTitle.getText().isEmpty() ||
                        textFieldPages.getText().isEmpty() || textFieldYear.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Wrong data. You should fill all empty fields", "System message", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    return;
                }
                if(action.equals(ACTIONS.EDIT))
                    Log.info("Id: " + Integer.toString(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getId()));
                if((action.equals(ACTIONS.ADD) && ClientXMLHandler.getClientLibraryLibrary().checkId(textFieldId.getText()))
                        ||(action.equals(ACTIONS.EDIT) && !textFieldId.getText().equals(Integer.toString(ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getId())) && ClientXMLHandler.getClientLibraryLibrary().checkId(textFieldId.getText()))) {
                    JOptionPane.showMessageDialog(null, "Identification number is already in use", "System message", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    try {
                        if (action.equals(ACTIONS.ADD)) {
                            client.getPrintWriterOut().println("ADDBOOK" + ClientXMLHandler.createXMLRequest(answers));
                        } else if (action.equals(ACTIONS.EDIT)){
                            client.getPrintWriterOut().println("EDITBOOK" + ClientXMLHandler.getIdOfCopyByIndexOfList(indexOfItem) + "+" + ClientXMLHandler.createXMLRequest(answers));
                            Log.info("EDITBOOK" + ClientXMLHandler.getIdOfCopyByIndexOfList(indexOfItem) + "+" + ClientXMLHandler.createXMLRequest(answers));
                        }
                    } catch (ParserConfigurationException e1) {
                            e1.printStackTrace();
                        } catch (TransformerException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                dispose();
                if(action.equals(ACTIONS.EDIT))
                    client.getPrintWriterOut().println("EDITFINISH" + ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getId());
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                if(action.equals(ACTIONS.EDIT))
                    client.getPrintWriterOut().println("EDITFINISH" + ClientXMLHandler.getClientLibraryLibrary().getElement(indexOfItem).getId());
            }
        });
    }

    private Boolean checkEditTime(){
        if(action.equals(ACTIONS.EDIT)) {
            currentDate = new Date();
            if(currentDate.getTime() - initialDate.getTime() >= 10000) {
                dispose();
                JOptionPane.showMessageDialog(null, "You was disconnected by timeout. Try again", "System message", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }
}

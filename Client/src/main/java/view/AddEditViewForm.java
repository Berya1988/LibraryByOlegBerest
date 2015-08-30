package view;

import controller.ClientXMLHandler;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Oleg on 27.08.2015.
 */
public class AddEditViewForm extends JFrame {
    private static final long serialVersionUID = 1L;
    private Container pane;
    private GroupLayout layout;
    private int action;                             //1 - add, 2 - edit, 3 - view
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

    public AddEditViewForm(int action, int index) {
        this.action = action;
        this.indexOfItem = index;
        initializeCompoments();
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
            case 1:
                setTitle("Add new book");
                break;
            case 2:
                setTitle("Edit book information");
                textFieldId.setText(Integer.toString(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getId()));
                textFieldAuthor.setText(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getBook().getAuthor());
                textFieldTitle.setText(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getBook().getTitle());
                textFieldPages.setText(Integer.toString(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getBook().getPageNumber()));
                textFieldYear.setText(Integer.toString(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getBook().getYear()));
                if(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getPresent()) {
                    comboBox.setSelectedIndex(0);
                }
                else {
                    comboBox.setSelectedIndex(1);
                }
                break;
            case 3:
                setTitle("View book information");
                okBtn.setVisible(false);
                textFieldId.setText(Integer.toString(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getId()));
                textFieldId.setEditable(false);
                textFieldAuthor.setText(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getBook().getAuthor());
                textFieldAuthor.setEditable(false);
                textFieldTitle.setText(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getBook().getTitle());
                textFieldTitle.setEditable(false);
                textFieldPages.setText(Integer.toString(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getBook().getPageNumber()));
                textFieldPages.setEditable(false);
                textFieldYear.setText(Integer.toString(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getBook().getYear()));
                textFieldYear.setEditable(false);
                if(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getPresent()) {
                    comboBox.setSelectedIndex(0);
                }
                else {
                    comboBox.setSelectedIndex(1);
                }
                comboBox.setEditable(false);
                comboBox.setEnabled(false);
                break;
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setSize(300, 280);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initializeCompoments(){
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
                if (!Character.isDigit(a)) {
                    e.consume();
                }
            }
        });
        textFieldAuthor = new JTextField(8);
        textFieldTitle = new JTextField(8);
        textFieldPages = new JTextField();
        textFieldPages.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char a = e.getKeyChar();
                if (!Character.isDigit(a)) {
                    e.consume();
                }
            }
        });
        textFieldYear = new JTextField();
        textFieldYear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char a = e.getKeyChar();
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
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] answers = {textFieldId.getText(), textFieldAuthor.getText(), textFieldTitle.getText(),
                        textFieldPages.getText(), textFieldYear.getText(), comboBox.getSelectedItem().toString()};

                System.out.println("Action:" + action);
                System.out.println("textFieldId: " + textFieldId.getText());
                if(action == 2)
                    System.out.println("Id: " + Integer.toString(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getId()));
                System.out.println("checkId: " + ClientXMLHandler.clientLibrary.checkId(textFieldId.getText()));
                if((action == 1 && ClientXMLHandler.clientLibrary.checkId(textFieldId.getText()))
                        ||(action == 2 && !textFieldId.getText().equals(Integer.toString(ClientXMLHandler.clientLibrary.getElement(indexOfItem).getId())) && ClientXMLHandler.clientLibrary.checkId(textFieldId.getText()))) {
                    JOptionPane.showMessageDialog(null, "Identification number is already in use", "System message", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    try {
                        if (action == 1) {
                            Client.out.println("ADDBOOK" + ClientXMLHandler.createXMLRequest(answers));
                        } else if (action == 2){
                            Client.out.println("EDITBOOK" + ClientXMLHandler.getIdOfCopyByIndexOfList(indexOfItem) + "+" + ClientXMLHandler.createXMLRequest(answers));
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
                if(action == 2)
                    Client.out.println("EDITFINISH" + ClientXMLHandler.clientLibrary.getElement(indexOfItem).getId());
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if(action == 2)
                    Client.out.println("EDITFINISH" + ClientXMLHandler.clientLibrary.getElement(indexOfItem).getId());
            }
        });
    }
}

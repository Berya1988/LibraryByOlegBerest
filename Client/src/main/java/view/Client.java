package view;

/**
 * Created by Oleg on 26.08.2015.
 */

import controller.ClientXMLHandler;
import controller.ConnectModelWithView;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The client follows the Library Protocol which is as follows.
 * When the server sends "SUBMITNAME" the client replies with the
 * desired screen name.  The server will keep sending "SUBMITNAME"
 * requests as long as the client submits screen names that are
 * already in use.  When the server sends a line beginning
 * with "NAMEACCEPTED" the client is now allowed to start
 * sending the server arbitrary strings to be broadcast to all
 * users connected to the server.
 */
public class Client {
    public static BufferedReader in;
    public static PrintWriter out;
    public JFrame frame;
    public static Socket socket;
    public static String name;
    public static boolean flagEdit = false;
    public Client() {
    }
    /**
     * Prompt for and return the address of the server.
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Library Server:",
                "Welcome to the Library",
                JOptionPane.QUESTION_MESSAGE);
    }
    /**
     * Prompt for and return the desired screen name.
     */
    private String getName() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }
    /**
     * Connects to the server then enters the processing loop.
     */
    public void run() throws IOException {
        // Make connection and initialize streams
        boolean flagAdress = true;
        String serverAddress = null;
        while (flagAdress){
            serverAddress = getServerAddress();
            if(serverAddress.equals("127.0.0.1"))
                flagAdress = false;
        }

        socket = new Socket(serverAddress, 9001);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        String line = null;
        // Process all messages from server, according to the protocol.
        while (true) {
            line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
                boolean flagName = true;
                while (flagName){
                    name = getName();
                    if(!name.isEmpty())
                        flagName = false;
                }
                out.println(name);
                
            } else if (line.startsWith("DATABASE")) {
                System.out.println("Data base was detected. XML file is reading...");
                System.out.println(line.substring(62));
                ClientXMLHandler.parseXMLLibrary(line.substring(62));
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ViewForm ex = new ViewForm();
                        ex.setVisible(true);
                        ViewForm.setNewStatus("Hello, " + name + "! You are welcome to the best library in the world.");
                    }
                });
            } else if (line.startsWith("UPDATEADD")) {
                ViewForm.setNewStatus("Updates were received. New book was added.");
                ClientXMLHandler.addNewBook(line.substring(9), ClientXMLHandler.clientLibrary.length());
                int position = ClientXMLHandler.clientLibrary.length();
                if (!line.substring(9).isEmpty()) {
                    ViewForm.model.addRow(new Object[]{position,
                            ClientXMLHandler.clientLibrary.getElement(position-1).getBook().getAuthor(),
                            ClientXMLHandler.clientLibrary.getElement(position-1).getBook().getTitle(),
                            ClientXMLHandler.clientLibrary.getElement(position-1).getBook().getYear(),
                            ClientXMLHandler.clientLibrary.getElement(position-1).getBook().getPageNumber(),
                            ClientXMLHandler.clientLibrary.getElement(position-1).getId(),
                            ClientXMLHandler.clientLibrary.getElement(position-1).getPresent()});
                }
                ViewForm.setNewStatus("New book was added to the library.");
            } else if (line.startsWith("UPDATEREMOVE")) {
                ViewForm.setNewStatus("Updates were received. Some books were deleted.");
                String[] stringIds = (line.substring(12)).split("[+]");
                int[] intgerIds = new int[stringIds.length];
                for (int i = 0; i < intgerIds.length; i++) {
                    intgerIds[i] = Integer.parseInt(stringIds[i]);
                    System.out.println(intgerIds[i]);
                }
                int[] elementsToRemove = ClientXMLHandler.removeBooks(intgerIds);
                for (int i = 0; i < elementsToRemove.length; i++) {
                    System.out.println(elementsToRemove[i]);
                }
                if (!line.substring(12).isEmpty()) {
                    for (int i = 0; i < elementsToRemove.length; i++) {
                        ViewForm.model.removeRow(elementsToRemove[i]);
                        System.out.println("Element " + elementsToRemove[i] + " was deleted");
                    }
                }
                ConnectModelWithView connectionModel = new ConnectModelWithView(ViewForm.model, ClientXMLHandler.clientLibrary);
                connectionModel.updateList();
            } else if (line.startsWith("EDITERROR" + name)) {
                JOptionPane.showMessageDialog(null, "This item is edited by other user. Try in a few minutes", "System message", JOptionPane.WARNING_MESSAGE);
            } else if (line.startsWith("EDITENABLE" + name)) {
                flagEdit = true;
                int selectedRow = ViewForm.table.getSelectedRow();
                System.out.println(selectedRow);
                System.out.println("Start edit!");
                AddEditViewForm form = new AddEditViewForm(2, selectedRow);

            } else if (line.startsWith("UPDATEEDIT")) {
                System.out.println(line.substring(10));
                String[] stringIds = (line.substring(10)).split("[+]");
                int id = Integer.parseInt(stringIds[0]);
                int []ids = {id};
                int index = ClientXMLHandler.clientLibrary.getIndexOfElementById(id);

                ViewForm.setNewStatus("Updates were received. Book " + (index + 1) + " was edited.");

                if (!stringIds[1].isEmpty()) {
                    ClientXMLHandler.removeBooks(ids);
                    ClientXMLHandler.addNewBook(stringIds[1], index);
                    ConnectModelWithView connectionModel = new ConnectModelWithView(ViewForm.model, ClientXMLHandler.clientLibrary);
                    connectionModel.updateList();
                }
            }
        }
    }
}
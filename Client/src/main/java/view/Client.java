package view;

/**
 * Created by Oleg on 26.08.2015.
 */

import controller.ClientXMLHandler;
import controller.ConnectModelWithView;
import model.ACTIONS;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

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
    private static final Logger Log = Logger.getLogger(Client.class);
    private BufferedReader in;
    private static PrintWriter out;
    private JFrame frame;
    private Socket socket;
    private static String name;
    public Client() {
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
     * @throws IOException If any client disconnects from server
     *                      and can not get and set messages
     */
    public void run() throws IOException {
        // Make connection and initialize streams
        socket = new Socket("127.0.0.1", 9001);
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
                Log.info("Data base was detected. XML file is reading...");
                ClientXMLHandler.parseXMLLibrary(line.substring(62));
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        ViewForm ex = new ViewForm();
                        ex.setVisible(true);
                        ViewForm.setNewStatus("Hello, " + name + "! You are welcome to the best library in the world.");
                    }
                });
            }  else if (line.startsWith("UPDATEDATABASE")) {
                ClientXMLHandler.updateXMLLibrary(line.substring(68));
                ConnectModelWithView connection = new ConnectModelWithView(ViewForm.getModel(), ClientXMLHandler.getClientLibraryLibrary());
                connection.updateList();
            }else if (line.startsWith("UPDATEADD")) {
                ViewForm.setNewStatus("Updates were received. New book was added.");
                ClientXMLHandler.addNewBook(line.substring(9), ClientXMLHandler.getClientLibraryLibrary().length());
                int position = ClientXMLHandler.getClientLibraryLibrary().length();
                if (!line.substring(9).isEmpty()) {
                    ViewForm.getModel().addRow(new Object[]{position,
                            ClientXMLHandler.getClientLibraryLibrary().getElement(position - 1).getBook().getAuthor(),
                            ClientXMLHandler.getClientLibraryLibrary().getElement(position - 1).getBook().getTitle(),
                            ClientXMLHandler.getClientLibraryLibrary().getElement(position - 1).getBook().getYear(),
                            ClientXMLHandler.getClientLibraryLibrary().getElement(position - 1).getBook().getPageNumber(),
                            ClientXMLHandler.getClientLibraryLibrary().getElement(position - 1).getId(),
                            ClientXMLHandler.getClientLibraryLibrary().getElement(position - 1).getPresent()});
                }
                ViewForm.setNewStatus("New book was added to the library.");
            } else if (line.startsWith("UPDATEREMOVE")) {
                ViewForm.setNewStatus("Updates were received. Some books were deleted.");
                String[] stringIds = (line.substring(12)).split("[+]");
                int[] intgerIds = new int[stringIds.length];
                for (int i = 0; i < intgerIds.length; i++) {
                    intgerIds[i] = Integer.parseInt(stringIds[i]);
                }
                int[] elementsToRemove = ClientXMLHandler.removeBooks(intgerIds);
                for (int i = 0; i < elementsToRemove.length; i++) {
                }
                if (!line.substring(12).isEmpty()) {
                    for (int i = 0; i < elementsToRemove.length; i++) {
                        ViewForm.getModel().removeRow(elementsToRemove[i]);
                        Log.info("Element " + elementsToRemove[i] + " was deleted");
                    }
                }
                ConnectModelWithView connectionModel = new ConnectModelWithView(ViewForm.getModel(), ClientXMLHandler.getClientLibraryLibrary());
                connectionModel.updateList();
            } else if (line.startsWith("EDITERROR")) {
                JOptionPane.showMessageDialog(null, "This item is edited by other user. Try in a few minutes", "System message", JOptionPane.WARNING_MESSAGE);
            } else if (line.startsWith("EDITENABLE")) {
                    int selectedRow = ViewForm.getTable().getSelectedRow();
                    Log.info("Start edit!");
                    AddEditViewForm form = new AddEditViewForm(ACTIONS.ADD, selectedRow);
                    form.showForm();
            } else if (line.startsWith("UPDATEEDIT")) {
                String[] stringIds = (line.substring(10)).split("[+]");
                int id = Integer.parseInt(stringIds[0]);
                int []ids = {id};
                int index = ClientXMLHandler.getClientLibraryLibrary().getIndexOfElementById(id);

                ViewForm.setNewStatus("Updates were received. Book " + (index + 1) + " was edited.");

                if (!stringIds[1].isEmpty()) {
                    ClientXMLHandler.removeBooks(ids);
                    ClientXMLHandler.addNewBook(stringIds[1], index);
                    ConnectModelWithView connectionModel = new ConnectModelWithView(ViewForm.getModel(), ClientXMLHandler.getClientLibraryLibrary());
                    connectionModel.updateList();
                }
            }
        }
    }
    public static PrintWriter getPrintWriterOut(){
        return out;
    }

    public static String getNameOfClient(){
        return name;
    }
}
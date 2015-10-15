package model;
/**
 * LibraryServer is the public class
 * to handle all requests from clients
 *
 * @author      Oleg Berest
 * @version     %I%, %G%
 */
import controller.IdChecker;
import controller.ServerXMLHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class LibraryServer extends Thread {
    private static final Logger log = Logger.getLogger(LibraryServer.class);
    /**
     * The set of clients which are currently connected to the server.
     */
    public static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    /**
     * The set of pairs [name of client - client].
     */
    private static HashMap<String, PrintWriter> namesWriters = new HashMap<String, PrintWriter>();
    /**
     * The set of pairs [id of edited book - date of edit beginning], which helps to control edited ids.
     */
    public static HashMap<Integer, Date> editedIds = new HashMap<Integer, Date>();
    private String name;
    private BufferedReader in;
    private PrintWriter out;
    private Date currentDate;
    private Socket socket;
    /**
     * The adress of library data base.
     */
    private final String nameOfDataBase = "Server//src//main//resources//library.xml";
    /**
     * Library data base which is converted to the simple string.
     */
    private String initialDataBaseString;

    /**
     * Class constructor.
     *  @param socket   initializes socket of server program
     *                  with default port 9001
     * @see controller.MainClassForServer
     */
    public LibraryServer(Socket socket) {
        this.socket = socket;
    }

    /**
     * Initializes client database.
     * Gets requests from clients
     * and sends necessary responses
     */
    public void run() {
        try {
            Thread newThread = new Thread(new IdChecker(currentDate, editedIds));
            newThread.start();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                out.println("SUBMITNAME");
                name = in.readLine();
                if (name == null) {
                    return;
                }
                synchronized (namesWriters) {
                    if (!namesWriters.containsKey(name)) {
                        namesWriters.put(name, out);
                        break;
                    }
                }
            }
            log.info("NAMEACCEPTED:" + name);
            writers.add(out);
            initialDataBaseString = ServerXMLHandler.transformXMLToString(nameOfDataBase);
            out.println("DATABASE"+initialDataBaseString);
            while (true) {
                String input = in.readLine();
                if (input == null) {
                    return;
                }
                else if (input.startsWith("ADDBOOK")) {
                    log.info("New request to add book");
                    ServerXMLHandler.addNewBook(input.substring(61), ServerXMLHandler.serverLibrary.length());
                    for (PrintWriter writer : writers) {
                        writer.println("UPDATEADD" + input.substring(61));
                    }
                    try {
                        ServerXMLHandler.createBackUpXMLLibrary();
                    } catch (ParserConfigurationException e) {
                        log.error("Threw a ParseConfigurationException in LibraryServer class::" + e.getMessage());
                    } catch (TransformerException e) {
                        log.error("Threw a TransformerException in LibraryServer class::" + e.getMessage());
                    }
                }
                else if (input.startsWith("REMOVE")) {
                    log.info("New request to remove book from the library.");
                    String[] stringIds = (input.substring(6)).split("[+]");
                    int[] intgerIds = new int[stringIds.length];
                    for (int i = 0; i < intgerIds.length; i++) {
                        intgerIds[i] = Integer.parseInt(stringIds[i]);
                    }
                    ServerXMLHandler.removeBooks(intgerIds);
                    for (PrintWriter writer : writers) {
                        writer.println("UPDATEREMOVE" + input.substring(6));
                    }
                    try {
                        ServerXMLHandler.createBackUpXMLLibrary();
                    } catch (ParserConfigurationException e) {
                        log.error("Threw a ParseConfigurationException in LibraryServer class::" + e.getMessage());
                    } catch (TransformerException e) {
                        log.error("Threw a TransformerException in LibraryServer class::" + e.getMessage());
                    }
                }
                else if (input.startsWith("EDITREQUEST")) {
                    String[] stringElements = (input.substring(11)).split("[+]");
                    int id = Integer.parseInt(stringElements[0]);
                    String nameClient = stringElements[1];
                    PrintWriter currentUser = null;

                    for(HashMap.Entry<String, PrintWriter> entry: namesWriters.entrySet()) {
                        if (entry.getKey().equals(nameClient)) {
                            currentUser = namesWriters.get(nameClient);
                        }
                    }
                    if (editedIds.containsKey(id)) {
                        currentUser.println("EDITERROR");
                    }
                    else {
                        currentDate = new Date();
                        editedIds.put(id, currentDate);
                        currentUser.println("EDITENABLE");
                    }
                }
                else if (input.startsWith("EDITFINISH")) {
                    int id = Integer.parseInt(input.substring(10));
                    if (editedIds.containsKey(id)) {
                        editedIds.remove(id);
                    }
                }
                else if (input.startsWith("UPDATEDATABASE")) {
                    String nameClient = input.substring(14);
                    PrintWriter currentUser = null;
                    for(HashMap.Entry<String, PrintWriter> entry: namesWriters.entrySet()) {
                        if (entry.getKey().equals(nameClient)) {
                            currentUser = namesWriters.get(nameClient);
                        }
                    }
                    currentUser.println("UPDATEDATABASE" + ServerXMLHandler.transformXMLToString(nameOfDataBase));
                }
                else if (input.startsWith("EDITBOOK")) {
                    log.info("New request to edit book from the library.");
                    String[] stringIds = (input.substring(8)).split("[+]");
                    int[] id = {Integer.parseInt(stringIds[0])};

                    int index = ServerXMLHandler.serverLibrary.getIndexOfElementById(id[0]);
                    ServerXMLHandler.removeBooks(id);
                    ServerXMLHandler.addNewBook(stringIds[1].substring(54), index);
                    editedIds.remove(id[0]);
                    for (PrintWriter writer: writers) {
                        writer.println("UPDATEEDIT" + input.substring(8));
                    }

                    try {
                        ServerXMLHandler.createBackUpXMLLibrary();
                    } catch (ParserConfigurationException e) {
                        log.error("Threw a ParseConfigurationException in LibraryServer class::" + e.getMessage());
                    } catch (TransformerException e) {
                        log.error("Threw a TransformerException in LibraryServer class::" + e.getMessage());
                    }
                }
            }
        }
        catch (IOException e) {
            log.error("Threw a IOException in LibraryServer class::" + e.getMessage());
        }
        finally {
            if (name != null) {
                namesWriters.remove(name);
            }
            if (out != null) {
                writers.remove(out);
            }
            try {
                socket.close();
            }
            catch (IOException e) {
                log.error("Threw a IOException in LibraryServer class::" + e.getMessage());
            }
        }
    }
}

package model;
import controller.ServerXMLHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

import org.apache.log4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Created by Oleg on 26.08.2015.
 */
public class LibraryServer extends Thread {
    private static final Logger log = Logger.getLogger(LibraryServer.class);
    private HashSet<String> names = new HashSet<String>();
    public  HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    private HashSet<Integer> editedIds = new HashSet<Integer>();
    private String name;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private final String nameOfDataBase = "Server//src//main//resources//library.xml";
    private String initialDataBaseString;

    public LibraryServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                out.println("SUBMITNAME");
                name = in.readLine();
                if (name == null) {
                    return;
                }
                synchronized (names) {
                    if (!names.contains(name)) {
                        names.add(name);
                        break;
                    }
                }
            }
            out.println("NAMEACCEPTED");
            log.info("NAMEACCEPTED:" + name.toUpperCase());
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
                    System.out.println(input);
                    String[] stringIds = (input.substring(6)).split("[+]");
                    int[] intgerIds = new int[stringIds.length];
                    for (int i = 0; i < intgerIds.length; i++) {
                        intgerIds[i] = Integer.parseInt(stringIds[i]);
                        System.out.println(intgerIds[i]);
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
                    System.out.println(input);
                    String[] stringIds = (input.substring(11)).split("[+]");
                    int id = Integer.parseInt(stringIds[0]);
                    String nameClient = stringIds[1];
                    System.out.println(stringIds[0]);
                    System.out.println(stringIds[1]);
                    for (int ide: editedIds) {
                        System.out.println("Element: " + ide);
                    }
                    if (editedIds.contains(id)) {
                        System.out.println("EDITERROR" + nameClient);
                        for (PrintWriter writer : writers) {
                            writer.println("EDITERROR" + nameClient);
                        }
                    }
                    else {
                        editedIds.add(id);
                        System.out.println("EDITENABLE" + nameClient);
                        for (PrintWriter writer : writers) {
                            writer.println("EDITENABLE" + nameClient);
                        }
                    }
                }
                else if (input.startsWith("EDITFINISH")) {
                    int id = Integer.parseInt(input.substring(10));
                    if (editedIds.contains(id)) {
                        editedIds.remove(id);
                    }
                }
                else if (input.startsWith("EDITBOOK")) {
                    log.info("New request to edit book from the library.");
                    System.out.println(input.substring(8));
                    String[] stringIds = (input.substring(8)).split("[+]");
                    int[] id = {Integer.parseInt(stringIds[0])};
                    System.out.println(stringIds[1].substring(54));

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
                names.remove(name);
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

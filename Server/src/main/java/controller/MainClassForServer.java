package controller;

/**
 * Created by Oleg on 26.08.2015.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import model.LibraryServer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class MainClassForServer {
    private static final Logger log = Logger.getLogger(MainClassForServer.class);
    private static final int PORT = 9001;

    public static void main(String[] args) throws Exception {
        log.info("The library server is launching...Wait...");
        ServerSocket listener = new ServerSocket(PORT);
        makeChoice();
        try {
            while (true) {
                new LibraryServer(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static void makeChoice() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int choice = 0;
        boolean flag = true;
        while(flag){
            System.out.println("Select option:\n\t1 - download temp library;\n\t2 - create new empty library;\n\t3 - download existing library.");
            choice = Integer.parseInt(bf.readLine());
            switch(choice){
                case 1:
                    ServerXMLHandler.createLibraryArchive();
                    ServerXMLHandler.addBooks();
                    ServerXMLHandler.parseLibraryArchive();
                    log.info("Library was extracted from the archive.");
                    flag = !flag;
                    break;
                case 2:
                    ServerXMLHandler.createLibraryArchive();
                    ServerXMLHandler.parseLibraryArchive();
                    log.info("New empty library was created.");
                    flag = !flag;
                    break;
                case 3:
                    ServerXMLHandler.parseLibraryArchive();
                    log.info("New empty library was created.");
                    flag = !flag;
                    break;
                default:
                    System.out.println("Your choice was incorrect. Try again.");
            }
        }
    }
}

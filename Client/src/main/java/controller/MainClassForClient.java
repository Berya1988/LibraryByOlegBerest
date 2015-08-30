package controller;
/**
 * Created by Oleg on 26.08.2015.
 */
import view.Client;
import java.io.IOException;

public class MainClassForClient {
    public static void main( String[] args ) throws IOException {
        Client client = new Client();
        client.run();
    }
}
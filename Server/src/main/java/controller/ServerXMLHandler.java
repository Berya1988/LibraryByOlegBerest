package controller;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import model.Book;
import model.Copy;
import model.Library;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Created by Oleg on 26.08.2015.
 */
public class ServerXMLHandler {
    public static Library serverLibrary = new Library();

    public static void createLibraryArchive() throws ParserConfigurationException, TransformerException, IOException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document xmlDoc = docBuilder.newDocument();

        Element rootElement = xmlDoc.createElement("library");
        Element mainElement = xmlDoc.createElement("items");
        rootElement.appendChild(mainElement);
        xmlDoc.appendChild(rootElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(xmlDoc);
        StreamResult streamResult = new StreamResult(new File("Server//src//main//resources//library.xml"));
        transformer.transform(domSource, streamResult);
    }

    public static void addBooks() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document xmlDoc = documentBuilder.parse("Server//src//main//resources//library.xml");

        Node items = xmlDoc.getElementsByTagName("items").item(0);

        Element tempElement = xmlDoc.createElement("book");
        Element tempElement1 = xmlDoc.createElement("book");

        tempElement.setAttribute("id", "10");
        tempElement.appendChild(setProductAttribute("authors", "Herbert Schildt", xmlDoc));
        tempElement.appendChild(setProductAttribute("title", "Java Ninth Edition. The Complete Reference", xmlDoc));
        tempElement.appendChild(setProductAttribute("pageNumber", "2000", xmlDoc));
        tempElement.appendChild(setProductAttribute("year", "2015", xmlDoc));
        tempElement.appendChild(setProductAttribute("isPresent", "true", xmlDoc));

        tempElement1.setAttribute("id", "22");
        tempElement1.appendChild(setProductAttribute("authors", "Robert Lafore", xmlDoc));
        tempElement1.appendChild(setProductAttribute("title", "Data Structures and Algorithms in Java", xmlDoc));
        tempElement1.appendChild(setProductAttribute("pageNumber", "702", xmlDoc));
        tempElement1.appendChild(setProductAttribute("year", "2013", xmlDoc));
        tempElement1.appendChild(setProductAttribute("isPresent", "false", xmlDoc));

        items.appendChild(tempElement);
        items.appendChild(tempElement1);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        DOMSource domSource = new DOMSource(xmlDoc);
        StreamResult streamResult = new StreamResult(new File("Server//src//main//resources//library.xml"));
        transformer.transform(domSource, streamResult);
    }

    public static void parseLibraryArchive() throws ParserConfigurationException, SAXException, IOException {
        File xmlFile = new File("Server//src//main//resources//library.xml");
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document document = docBuilder.parse(xmlFile);
        document.getDocumentElement().normalize();

        Copy copy;
        Book book;
        int id;
        String author;
        String title;
        int pageNumber;
        int year;
        boolean isPresent;

        NodeList list = document.getElementsByTagName("book");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if(node instanceof Element) {
                Element element = (Element)list.item(i);
                //System.out.println("Id of this book is: " + element.getAttribute("id"));
                id = Integer.parseInt(element.getAttribute("id"));
                //System.out.println("Authors: " + element.getElementsByTagName("authors").item(0).getTextContent());
                author = element.getElementsByTagName("authors").item(0).getTextContent();
                //System.out.println("Title: " + element.getElementsByTagName("title").item(0).getTextContent());
                title = element.getElementsByTagName("title").item(0).getTextContent();
                //System.out.println("Page number: " + element.getElementsByTagName("pageNumber").item(0).getTextContent());
                pageNumber = Integer.parseInt(element.getElementsByTagName("pageNumber").item(0).getTextContent());
                //System.out.println("Published in " + element.getElementsByTagName("year").item(0).getTextContent());
                year = Integer.parseInt(element.getElementsByTagName("year").item(0).getTextContent());
                //System.out.println("The book is present: " + element.getElementsByTagName("isPresent").item(0).getTextContent());
                isPresent = Boolean.parseBoolean(element.getElementsByTagName("isPresent").item(0).getTextContent());
                book = new Book(author, title, pageNumber, year);
                copy = new Copy(book, id, isPresent);
                serverLibrary.add(copy);
            }
        }
    }

    private static Element setProductAttribute(String name, String value, Document xmlDoc) {
        Text attributeText = xmlDoc.createTextNode(value);
        Element attributeName = xmlDoc.createElement(name);
        attributeName.appendChild(attributeText);
        return attributeName;
    }

    public static synchronized void addNewBook(String xmlRecords, int position){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlRecords));
            Document doc = db.parse(is);

            Copy copy;
            Book book;
            String author;
            String title;
            int id;
            int pageNumber;
            int year;
            boolean isPresent;
            //System.out.println("There are " + serverLibrary.length() + " books in the serverLibrary before new book was added");

            Element element = (Element)doc.getElementsByTagName("book").item(0);
            //System.out.println("Id of this book is: " + element.getAttribute("id"));
            id = Integer.parseInt(element.getAttribute("id"));
            //System.out.println("Authors: " + element.getElementsByTagName("authors").item(0).getTextContent());
            author = element.getElementsByTagName("authors").item(0).getTextContent();
            //System.out.println("Title: " + element.getElementsByTagName("title").item(0).getTextContent());
            title = element.getElementsByTagName("title").item(0).getTextContent();
            //System.out.println("Page number: " + element.getElementsByTagName("pageNumber").item(0).getTextContent());
            pageNumber = Integer.parseInt(element.getElementsByTagName("pageNumber").item(0).getTextContent());
            //System.out.println("Published in " + element.getElementsByTagName("year").item(0).getTextContent());
            year = Integer.parseInt(element.getElementsByTagName("year").item(0).getTextContent());
            //System.out.println("The book is present: " + element.getElementsByTagName("isPresent").item(0).getTextContent());
            isPresent = Boolean.parseBoolean(element.getElementsByTagName("isPresent").item(0).getTextContent());
            book = new Book(author, title, pageNumber, year);
            copy = new Copy(book, id, isPresent);
            serverLibrary.addAt(position, copy);
            //System.out.println("There are " + serverLibrary.length() + " books in the serverLibrary after new book was added");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createBackUpXMLLibrary() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document xmlDoc = docBuilder.newDocument();

        Element rootElement = xmlDoc.createElement("library");
        Element mainElement = xmlDoc.createElement("items");
        Element tempElement;

        for (int i = 0; i < serverLibrary.length(); i++) {
            tempElement = xmlDoc.createElement("book");
            tempElement.setAttribute("id", String.valueOf(serverLibrary.getElement(i).getId()));
            tempElement.appendChild(setProductAttribute("authors", serverLibrary.getElement(i).getBook().getAuthor(), xmlDoc));
            tempElement.appendChild(setProductAttribute("title", serverLibrary.getElement(i).getBook().getTitle(), xmlDoc));
            tempElement.appendChild(setProductAttribute("pageNumber", String.valueOf(serverLibrary.getElement(i).getBook().getPageNumber()), xmlDoc));
            tempElement.appendChild(setProductAttribute("year", String.valueOf(serverLibrary.getElement(i).getBook().getYear()), xmlDoc));
            tempElement.appendChild(setProductAttribute("isPresent", String.valueOf(serverLibrary.getElement(i).getPresent()), xmlDoc));
            mainElement.appendChild(tempElement);
        }

        rootElement.appendChild(mainElement);
        xmlDoc.appendChild(rootElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(xmlDoc);
        StreamResult streamResult = new StreamResult(new File("Server//src//main//resources//library.xml"));
        transformer.transform(domSource, streamResult);
    }

    public static String transformXMLToString(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
        String line;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine())!= null){
            sb.append(line.trim());
        }
        return sb.toString();
    }

    public static synchronized void removeBooks(int[] indecesToRemove) {
        for (int i = 0; i < indecesToRemove.length; i++) {
            for (int j = 0; j < serverLibrary.length(); j++) {
                if(serverLibrary.getElement(j).getId() == indecesToRemove[i]) {
                    serverLibrary.remove(j);
                    break;
                }
            }
        }
    }
}

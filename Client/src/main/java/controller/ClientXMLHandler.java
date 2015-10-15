package controller;
/**
 * Created by Oleg on 26.08.2015.
 * */
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;
import org.w3c.dom.*;
import model.Library;
import model.Book;
import model.Copy;

import java.io.*;

public class ClientXMLHandler {
    public static Library clientLibrary = new Library();

    public static void updateXMLLibrary(String xmlRecords){
        for (int i = clientLibrary.length()-1; i >=0 ; i--) {
            clientLibrary.remove(i);
        }
        parseXMLLibrary(xmlRecords);
    }

    public static void parseXMLLibrary(String xmlRecords){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlRecords));

            Document doc = db.parse(is);
            NodeList list = doc.getElementsByTagName("book");
            Copy copy;
            Book book;
            int id;
            String author;
            String title;
            int pageNumber;
            int year;
            boolean isPresent;

            //System.out.println("There are " + clientLibrary.length() + " books in the clientLibrary before parsing");

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if(node instanceof Element) {
                    Element element = (Element)list.item(i);
                    id = Integer.parseInt(element.getAttribute("id"));
                    author = element.getElementsByTagName("authors").item(0).getTextContent();
                    title = element.getElementsByTagName("title").item(0).getTextContent();
                    pageNumber = Integer.parseInt(element.getElementsByTagName("pageNumber").item(0).getTextContent());
                    year = Integer.parseInt(element.getElementsByTagName("year").item(0).getTextContent());
                    isPresent = Boolean.parseBoolean(element.getElementsByTagName("isPresent").item(0).getTextContent());
                    book = new Book(author, title, pageNumber, year);
                    copy = new Copy(book, id, isPresent);
                    clientLibrary.add(copy);
                }
            }
            //System.out.println("There are " + clientLibrary.length() + " books in the clientLibrary after parsing");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String createXMLRequest(String []answers) throws ParserConfigurationException, TransformerException, IOException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document xmlDoc = docBuilder.newDocument();

        Element rootElement = xmlDoc.createElement("library");
        Element mainElement = xmlDoc.createElement("items");
        Element tempElement = xmlDoc.createElement("book");

        tempElement.setAttribute("id", answers[0]);
        tempElement.appendChild(setProductAttribute("authors", answers[1], xmlDoc));
        tempElement.appendChild(setProductAttribute("title", answers[2], xmlDoc));
        tempElement.appendChild(setProductAttribute("pageNumber", answers[3], xmlDoc));
        tempElement.appendChild(setProductAttribute("year", answers[4], xmlDoc));
        tempElement.appendChild(setProductAttribute("isPresent", answers[5], xmlDoc));

        mainElement.appendChild(tempElement);
        rootElement.appendChild(mainElement);
        xmlDoc.appendChild(rootElement);

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(xmlDoc);
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch(TransformerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addNewBook(String xmlRecords, int position){
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
            //System.out.println("There are " + clientLibrary.length() + " books in the serverLibrary before new book was added");

            Element element = (Element)doc.getElementsByTagName("book").item(0);
            id = Integer.parseInt(element.getAttribute("id"));
            //System.out.println("Authors: " + element.getElementsByTagName("authors").item(0).getTextContent());
            author = element.getElementsByTagName("authors").item(0).getTextContent();
            title = element.getElementsByTagName("title").item(0).getTextContent();
            pageNumber = Integer.parseInt(element.getElementsByTagName("pageNumber").item(0).getTextContent());
            year = Integer.parseInt(element.getElementsByTagName("year").item(0).getTextContent());
            isPresent = Boolean.parseBoolean(element.getElementsByTagName("isPresent").item(0).getTextContent());
            book = new Book(author, title, pageNumber, year);
            copy = new Copy(book, id, isPresent);
            clientLibrary.addAt(position, copy);
            //System.out.println("There are " + clientLibrary.length() + " books in the serverLibrary after new book was added");
            //System.out.println(clientLibrary.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Element setProductAttribute(String name, String value, Document xmlDoc) {
        Text attributeText = xmlDoc.createTextNode(value);
        Element attributeName = xmlDoc.createElement(name);
        attributeName.appendChild(attributeText);
        return attributeName;
    }

    public static int getIdOfCopyByIndexOfList(int indexOfList){
        return clientLibrary.getElement(indexOfList).getId();
    }

    public static int[] removeBooks(int[] indecesToRemove) {
        int [] indexesOfList = new int[indecesToRemove.length];
        int count = 0;
        for (int i = 0; i < indecesToRemove.length; i++) {
            for (int j = 0; j < clientLibrary.length(); j++) {
                if(clientLibrary.getElement(j).getId() == indecesToRemove[i]) {
                    clientLibrary.remove(j);
                    indexesOfList[count++] = j;
                    break;
                }
            }
        }
        return indexesOfList;
    }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import hr.algebra.model.MailingList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author RnaBo
 */
public class DOMUtils {
    
    private static final String FILENAME_CONTACTS = "mailinglists.xml";
    

    public static void saveMailingLists(ObservableList<MailingList> mailingLists) {

        try {
            Document document = DOMUtils.createDocument("mailinglists");
            mailingLists.forEach(ml -> document.getDocumentElement().appendChild(createMailingListElement(ml, document)));
            saveDocument(document, FILENAME_CONTACTS);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static Element createMailingListElement(MailingList ml, Document document) throws DOMException {
        Element mailingList = document.createElement("mailinglist");
        mailingList.setAttributeNode(createAttribute(document, "name", ml.getName() ));
        mailingList.appendChild(createListElement(ml.getEmails(),document));
        return mailingList;
    }

   

    private static Element createListElement(List<String> list, Document document) throws DOMException {
        Element emails = document.createElement("emails");
        list.forEach(email -> emails.appendChild(createElement(document, "email", email)));
        return emails;
    }

    private static Document createDocument(String element) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation domImplementation = builder.getDOMImplementation();
        DocumentType documentType = domImplementation.createDocumentType("DOCTYPE", null, "emails.dtd");
        return domImplementation.createDocument(null, element, documentType);
    }

    private static Attr createAttribute(Document document, String name, String value) {
        Attr attr = document.createAttribute(name);
        attr.setValue(value);
        return attr;
    }

    private static Node createElement(Document document, String tagName, String data) {
        Element element = document.createElement(tagName);
        Text text = document.createTextNode(data);
        element.appendChild(text);
        return element;
    }

    private static void saveDocument(Document document, String fileName) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        //transformer.transform(new DOMSource(document), new StreamResult(System.out));
        transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
    }

    public static ObservableList<MailingList> loadMailingLists() {
        ObservableList<MailingList> mailingLists = FXCollections.observableArrayList();
        try {
            Document document = createDocument(new File(FILENAME_CONTACTS));
            NodeList nodes = document.getElementsByTagName("mailinglist");
            for (int i = 0; i < nodes.getLength(); i++) {
                // dangerous class cast exception
                mailingLists.add(processMailingListNode((Element) nodes.item(i)));
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return mailingLists;
    }
    
    private static MailingList processMailingListNode(Element mailingList) {
        return new MailingList(
                mailingList.getAttribute("name"),
                processEmailNode(mailingList.getElementsByTagName("email")));
    }

    private static Document createDocument(File file) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        return document;
    }

    private static List<String> processEmailNode(NodeList emails) {
        List<String> list = new ArrayList<>();
        
        for (int i = 0; i < emails.getLength(); i++) {
           
            Node email = emails.item(i);
            list.add(email.getTextContent());
        }
        
        return list;
    }

    
    
}

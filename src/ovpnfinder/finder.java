/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ovpnfinder;


import java.io.IOException;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author jdeu
 */
public class finder {

    public finder(String url) throws SAXException, IOException, ParserConfigurationException{
        
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = f.newDocumentBuilder();
        Document doc = b.parse(url);
 
        doc.getDocumentElement().normalize();
        System.out.println ("Root element: " + doc.getDocumentElement().getNodeName());

        // loop through each item
        NodeList device = doc.getElementsByTagName("device");
        for (int i = 0; i < device.getLength(); i++)
        {
            System.out.println(device.item(i).getAttributes().getNamedItem("title").getNodeValue());
        }
    }
    
}

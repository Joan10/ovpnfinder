/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ovpnfinder;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author jdeu
 */
public class finder {

    public static final int MAX_SIZE = 10;
    servidor_ovpn srv_list[] = new servidor_ovpn[MAX_SIZE]; 
    int n=0;
    
    int getLength(){
        return n;
    }
    servidor_ovpn[] getSrvList(){
        return srv_list;
    }
    
    public finder(String url){
        

        boolean isVPN;
        boolean isIP;
        boolean fi;
        String IP="";
        int i,j;
        
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b;
        Document doc;
        try {
            b = f.newDocumentBuilder();
            doc = b.parse(url);

 
            doc.getDocumentElement().normalize();
            System.out.println ("Root element: " + doc.getDocumentElement().getNodeName());

            // loop through each item
            NodeList device = doc.getElementsByTagName("device");
            System.out.println(device.getLength());
            for (i = 0; i < device.getLength() && n<10; i++)
            {
                //Recorrem tots els fills cercant un dispositiu amb VPN i IP.               
                NodeList child = device.item(i).getChildNodes();
                System.out.println(device.item(i).getAttributes().getNamedItem("title"));
                j=0;
                fi=false;
                isIP=false;
                isVPN=false;
                while ( j < child.getLength() && fi == false && n<10) {
                    
                    // Per cada fill cercam si té VPN i la IP
                    try {
                        if ("VPN".equals(child.item(j).getAttributes().getNamedItem("type").getNodeValue())){
                            isVPN=true;
                        }
                    } catch (NullPointerException ex) {}
                    
                    try {
                        
                        if (child.item(j).getAttributes().getNamedItem("ipv4") != null){
                            IP = child.item(j).getAttributes().getNamedItem("ipv4").getNodeValue();
                            isIP=true;
                        }
                    } catch (NullPointerException ex) {}
                    
                    if (isVPN == true && isIP == true){
                        //Si és vpn i li hem trobat la IP l'afegim a la llista.
                        fi = true;
                        srv_list[n] = new servidor_ovpn(IP);
                        n=n+1;
                        
                    }
                    j++;
                }
     
                
            }
        
        } catch  ( SAXException | IOException ex) {
            System.out.println("Impossible arribar al servidor de guifi.net"+ex.toString());
            //hardcoded servers per si no podem accedir a Internet.
            srv_list[0] = new servidor_ovpn("vpnalq.guifisoller.cat");
            srv_list[1] = new servidor_ovpn("proxyvic.guifisoller.cat");
            srv_list[2] = new servidor_ovpn("proxydvic.guifisoller.cat");
            srv_list[3] = new servidor_ovpn("proxydevic.guifisoller.cat");
            n=4;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(finder.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Fi");
    }
    
}

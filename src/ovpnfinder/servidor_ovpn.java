/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ovpnfinder;

import java.net.*;
import java.io.*;
import java.lang.Object;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
/**
 *
 * @author jdeu
 */
public class servidor_ovpn {
    
    public enum tEstat {
        ATURAT, ENCES
    }

    String Nom;
    String IP;
    
    public servidor_ovpn(String Id) {
//Id pot ser un hostname o una IP
        if ( validIP(Id) ) {
            //És una IP
            this.IP = Id;
            try {
                this.Nom = InetAddress.getByName(this.IP).getHostName();
            } catch (UnknownHostException ex) {
                this.Nom = null;
            }
        }else{
            this.Nom=Id;
            try {
                this.IP = InetAddress.getByName(this.Nom).getHostAddress();
            } catch (UnknownHostException ex) {
                this.IP = null;
            }
        }
           
        
    }
    
    private tEstat comprova_estat(){     
        //Comprova si el servidor està UP.
        InetAddress addr;    
        try {
            if (Nom == null)
                addr = InetAddress.getByName(IP);
            else
                addr = InetAddress.getByName(Nom);
        
            if (addr.isReachable(5000))
                return tEstat.ENCES;
            else
                return tEstat.ATURAT;
        } catch (IOException ex) {
            return tEstat.ATURAT;
        }
        
    }

    public String getIP() {
        return IP;
    }

    public String getNom() {
        return Nom;
    }
    
    public String getValidField() {
        if ( Nom == null )
            return IP;
        else if (IP == null)
            return Nom;
        else
            //Tornam el nom per defecte.
            return Nom;
                   
    }

    public tEstat getEstat() {
        return comprova_estat();
    }
 
    
    public static boolean validIP (String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if(ip.endsWith(".")) {
                    return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
}



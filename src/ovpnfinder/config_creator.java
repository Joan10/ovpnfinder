/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ovpnfinder;

/**
 *
 * @author jdeu
 */
public class config_creator {
   
    private String vpnconf = "";
    private String pass = "";
    
    public config_creator(String remotes[], int len, boolean remote_random, String Usuari, String Contrasenya) {
    
        if ( remotes.length == 0 ) {
            throw new IllegalArgumentException("Llista de servidors remots buida!");
        }

        vpnconf =   "client\n" +
                    "auth-user-pass /etc/openvpn/pass.txt\n" +
                    "ca /etc/openvpn/ca/ca.crt\n" +
                    "comp-lzo yes\n" +
                    "dev tun\n" +
                    "#log /tmp/openvpn.log\n" +
                    "proto udp\n" +
                    "verb 5\n";
        for (int i = 0; i < len; i++){
            //Afegim tants remote com noms de servidors passats
            vpnconf = vpnconf + ("remote " + remotes[i] + " 1194\n");
        }
        
        if (remote_random)
            vpnconf = vpnconf + "remote-random yes";
        
        if (Usuari != null && Contrasenya != null)
            //Si li hem passat usuari i contrasenya els canviam. Sino, no.
            pass = Usuari + "\n" + Contrasenya;
    }
    
    public String getConfFile(){
        return vpnconf;
    }
    public String getPassFile(){
        if ( pass.length() == 0 ) {
            throw new IllegalArgumentException("No s'han inclòs usuaris.");
        }
        return pass;
    }
}

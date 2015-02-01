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
    
    public String command_creator(){
        //Generam les comandes per enviar al router.
        if (vpnconf.equals("") || pass.equals("")) return "";
        
        String vpnconf_command = "echo '"+vpnconf+"'>  /tmp/vpn.conf";
        String pass_command = "echo '"+pass+"'>  /tmp/pass.conf";
        
        String openvpnconf = "config openvpn client_tun_0\n" +
"        option enabled 1\n" +
"	option config /etc/openvpn/vpn.conf";
        
        String vpnconf2_command = "echo '"+openvpnconf+"' > /tmp/openvpn.conf";
        String rstservice_command = "/etc/init.d/openvpn restart";
        return vpnconf_command+";"+pass_command+";"+vpnconf2_command+";"+rstservice_command;
    }
    public config_creator(String remotes[], int len, boolean remote_random, String Usuari, String Contrasenya) {
    
        if ( remotes.length == 0 ) {
            throw new IllegalArgumentException("Llista de servidors remots buida!");
        }else if (Usuari.equals("") || Contrasenya.equals("")) {
            throw new IllegalArgumentException("Usuari o contrasenya buits!");
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

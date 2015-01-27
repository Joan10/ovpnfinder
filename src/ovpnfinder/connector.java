/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ovpnfinder;

import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author jdeu
 */
public class connector {

    String IP_router = "192.168.1.2";
    String User = "root";
    String Password = "blanquet";
    
    public connector() {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(User, IP_router, 22);
            session.setPassword(Password);

/*            String host = null;
            host = JOptionPane.showInputDialog("Enter username@hostname",
                    System.getProperty("user.name")
                    + "@localhost");
            
            String user = host.substring(0, host.indexOf('@'));
            host = host.substring(host.indexOf('@') + 1);

            session = jsch.getSession(user, host, 22);

            String passwd = JOptionPane.showInputDialog("Enter password");
        
            session.setPassword(passwd);
*/
            UserInfo ui = new MyUserInfo() {
                public void showMessage(String message) {
                    JOptionPane.showMessageDialog(null, message);
                }

                public boolean promptYesNo(String message) {
                    Object[] options = {"yes", "no"};
                    int foo = JOptionPane.showOptionDialog(null,
                            message,
                            "Warning",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    return foo == 0;
                }
            };

            session.setUserInfo(ui);

            session.connect(30000);
            Channel channel = session.openChannel("shell");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect(3 * 1000);
            
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /*
    public Escriu_config(){
        
        
        
    }*/

    public static abstract class MyUserInfo
            implements UserInfo, UIKeyboardInteractive {

        public String getPassword() {
            return null;
        }

        public boolean promptYesNo(String str) {
            return false;
        }

        public String getPassphrase() {
            return null;
        }

        public boolean promptPassphrase(String message) {
            return false;
        }

        public boolean promptPassword(String message) {
            return false;
        }

        public void showMessage(String message) {
        }

        public String[] promptKeyboardInteractive(String destination,
                String name,
                String instruction,
                String[] prompt,
                boolean[] echo) {
            return null;
        }
    }
}

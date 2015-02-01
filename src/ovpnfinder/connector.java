/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ovpnfinder;

import com.jcraft.jsch.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import javax.swing.*;

/**
 *
 * @author jdeu
 */
public class connector  implements Runnable {
/*
    Connector és un fil que fa les connexions ssh. És un fil perquè
    no es quedi bloquejat el main principal.
*/
    
    String IP_router = "192.168.1.2";
    String User = "root";
    String Password = "blanquet";

    
    String comanda;
    javax.swing.JButton accept_button;
    javax.swing.JLabel load_icon;
    
    private boolean connecta0(String command) throws JSchException {
        JSch jsch = new JSch();
        Session session;
        boolean retorn = false;
        
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

        session = jsch.getSession(User, IP_router, 22);
        session.setPassword(Password);
        session.setUserInfo(ui);
        session.connect();


        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        channel.setInputStream(null);

        channel.setOutputStream(System.out);

        ((ChannelExec) channel).setErrStream(System.err);

        try {
            InputStream in = channel.getInputStream();

            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) {
                        continue;
                    }
                    System.out.println("exit-status: " + channel.getExitStatus());
                    if (channel.getExitStatus() == 0)
                        retorn = true;
                    
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
            channel.disconnect();
            session.disconnect();
            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(connector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorn;
    }

    private void connecta(String command){

        int surt = 0;
        JSch jsch = new JSch();
        Session session;

        while (surt == 0) {

            try {
                if (connecta0(command))
                    JOptionPane.showMessageDialog(null,
                    "La configuració s'ha carregat correctament.",
                    "Missatge",
                    JOptionPane.PLAIN_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null,
                    "Alguna cosa ha anat malament configurant el router. Consultau amb l'administrador.",
                    "Error al router",
                    JOptionPane.ERROR_MESSAGE);
                surt = 1;

            } catch (JSchException ex) {
                Object[] options = {"Sí", "No"};
                surt = JOptionPane.showOptionDialog(null, "Impossible arribar al router. És possible que la IP o les credencials estiguin malament. Voleu tornar-ho a provar?", "Error de connexió", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                System.out.println("Surt" + surt);
                System.out.println("session error");
                if (surt == 0) {
                    IP_router = JOptionPane.showInputDialog("Introdueix IP del router: ",
                            "192.168.1.2");
                    User = JOptionPane.showInputDialog("Introdueix usuari: ",
                            "root");
                    Password = JOptionPane.showInputDialog("Introdueix contrasenya: ",
                            "blanquet");
                }
            }

        }

    }

    
    public connector(String command, javax.swing.JButton accept_button, javax.swing.JLabel load_icon){
        this.comanda=command;
        this.accept_button = accept_button;
        this.load_icon = load_icon;

    }
    /*
     public Escriu_config(){
        
        
        
     }*/

    @Override
    public void run() {
        //Els elements de la interfície que canvien es modifiquen aquí.
        load_icon.setVisible(true);
        accept_button.setEnabled(false);
        connecta(this.comanda);
        load_icon.setVisible(false);
        accept_button.setEnabled(true);
    }

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

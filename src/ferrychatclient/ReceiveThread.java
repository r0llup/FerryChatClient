/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ferrychatclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

/**
 *
 * @author Sh1fT
 */

public final class ReceiveThread extends Thread {
    protected Client_Chat parent;
    protected Boolean enabled;

    /**
     * Creates new instance ReceiveThread
     * @param parent 
     */
    public ReceiveThread(Client_Chat parent) {
        this.setParent(parent);
        this.setEnabled(false);
    }

    public Client_Chat getParent() {
        return this.parent;
    }

    protected void setParent(Client_Chat parent) {
        this.parent = parent;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    protected void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void run() {
        this.setEnabled(true);
        while (this.enabled)
        {
            try
            {
                byte[] buf = new byte[1024];
                DatagramPacket dtg = new DatagramPacket(buf, buf.length);
                this.getParent().getGroupSocket().receive(dtg);
                String info = new String(buf).trim();
                StringTokenizer st = new StringTokenizer(info, "#");
                String nick = st.nextToken();
                if (info.contains("#JOINGRP#")) {
                    if (!this.getParent().getNickListModel().contains(nick)) {
                        if (this.getParent().getLogin().getUsername().compareTo(nick) != 0) {
                            this.getParent().getNickListModel().addElement(nick);
                            this.getParent().getReceiveTextArea().append(nick + " a rejoint la conversation.\n");
                        }
                    }
                } else if (info.contains("#LEAVEGRP#")) {
                    this.getParent().getNickListModel().removeElement(nick);
                    this.getParent().getReceiveTextArea().append(nick + " a quitté la conversation.\n");
                } else
                    this.getParent().getReceiveTextArea().append(info + "\n");
            } catch (IOException ex ) {
                this.setEnabled(false);
                JOptionPane.showMessageDialog(this.getParent(), ex.getLocalizedMessage(),
                    "Aïe Aïe Aïe !", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }
}
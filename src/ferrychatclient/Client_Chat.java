/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Client_Chat.java
 *
 * Created on 17 nov. 2011, 17:24:22
 */

package ferrychatclient;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import utils.PropertiesLauncher;

/**
 *
 * @author Sh1fT
 */

public final class Client_Chat extends javax.swing.JFrame implements KeyListener, MouseListener {
    protected PropertiesLauncher propertiesLauncher;
    protected Login login;
    protected String groupHost;
    protected Integer groupPort;
    protected InetAddress groupAddress;
    protected MulticastSocket groupSocket;
    protected ReceiveThread receiveThread;
    protected DefaultListModel nickListModel;

    /**
     * Creates new form Client_Chat
     */
    public Client_Chat() {
        this.initComponents();
        this.setPropertiesLauncher(new PropertiesLauncher(
            System.getProperty("file.separator") + "properties" +
            System.getProperty("file.separator") + "FerryChatClient.properties"));
        this.setLogin(new Login(this, true));
        this.setGroupHost(null);
        this.setGroupPort(null);
        this.setGroupAddress(null);
        this.setGroupSocket(null);
        this.setReceiveThread(new ReceiveThread(this));
        this.setNickListModel(new DefaultListModel());
        this.nickList.setModel(this.getNickListModel());
        this.nickList.addMouseListener(this);
        this.sendTextArea.addKeyListener(this);
    }

    public PropertiesLauncher getPropertiesLauncher() {
        return this.propertiesLauncher;
    }

    protected void setPropertiesLauncher(PropertiesLauncher propertiesLauncher) {
        this.propertiesLauncher = propertiesLauncher;
    }

    public Login getLogin() {
        return this.login;
    }

    protected void setLogin(Login login) {
        this.login = login;
    }

    public String getGroupHost() {
        return this.groupHost;
    }

    protected void setGroupHost(String groupHost) {
        this.groupHost = groupHost;
    }

    public Integer getGroupPort() {
        return this.groupPort;
    }

    protected void setGroupPort(Integer groupPort) {
        this.groupPort = groupPort;
    }

    public InetAddress getGroupAddress() {
        return this.groupAddress;
    }

    protected void setGroupAddress(InetAddress groupAddress) {
        this.groupAddress = groupAddress;
    }

    public MulticastSocket getGroupSocket() {
        return this.groupSocket;
    }

    protected void setGroupSocket(MulticastSocket groupSocket) {
        this.groupSocket = groupSocket;
    }

    public ReceiveThread getReceiveThread() {
        return this.receiveThread;
    }

    protected void setReceiveThread(ReceiveThread receiveThread) {
        this.receiveThread = receiveThread;
    }

    public DefaultListModel getNickListModel() {
        return this.nickListModel;
    }

    protected void setNickListModel(DefaultListModel nickListModel) {
        this.nickListModel = nickListModel;
    }

    public String getServerHost() {
        return this.getPropertiesLauncher().getProperties().getProperty("serverHost");
    }

    public Integer getServerPort() {
        return Integer.parseInt(this.getPropertiesLauncher().getProperties().getProperty("serverPort"));
    }

    public String getSaveFilename() {
        return this.getPropertiesLauncher().getProperties().getProperty("saveFilename");
    }

    public JTextArea getReceiveTextArea() {
        return this.receiveTextArea;
    }

    public JTextArea getSendTextArea() {
        return this.sendTextArea;
    }

    /**
     * Get Nicks
     */
    protected void getNicks() {
        try {
            Socket socket = new Socket(this.getServerHost(), this.getServerPort());
            DataInputStream dis = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(socket.getOutputStream()));
            String commande = "GETNICKS";
            dos.write((commande + "\n").getBytes());
            dos.flush();
            StringBuilder info = new StringBuilder();
            info.setLength(0);
            byte b = 0;
            while ((b=dis.readByte()) != (byte) '\n') {
                if (b != '\n')
                    info.append((char) b);
            }
            List<String> nicks = Arrays.asList(info.toString().split(":"));
            if (nicks != null) {
                for (String nick : nicks) {
                    if (this.getLogin().getUsername().compareTo(nick) != 0)
                        this.getNickListModel().addElement(nick);
                }
            }
            dos.close();
            dis.close();
            socket.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(),
                "Aïe Aïe Aïe !", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * Get Nick
     * @param nick
     * @return Boolean
     */
    protected Boolean getNick(String nick) {
        try {
            Socket socket = new Socket(this.getServerHost(), this.getServerPort());
            DataInputStream dis = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(socket.getOutputStream()));
            String commande = "GETNICK";
            dos.write((commande + ";" + nick + "\n").getBytes());
            dos.flush();
            StringBuilder info = new StringBuilder();
            info.setLength(0);
            byte b = 0;
            while ((b=dis.readByte()) != (byte) '\n') {
                if (b != '\n')
                    info.append((char) b);
            }
            dos.close();
            dis.close();
            socket.close();
            return Boolean.parseBoolean(info.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(),
                "Aïe Aïe Aïe !", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return null;
        }
    }

    /**
     * Delete Nick
     * @param nick 
     */
    protected void delNick(String nick) {
        try {
            Socket socket = new Socket(this.getServerHost(), this.getServerPort());
            DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(socket.getOutputStream()));
            String commande = "DELNICK";
            dos.write((commande + ";" + nick + "\n").getBytes());
            dos.flush();
            dos.close();
            socket.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(),
                "Aïe Aïe Aïe !", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.isShiftDown()) {
            if ((e.getKeyCode() == KeyEvent.VK_ENTER))
                this.sendTextArea.append("\n");
        } else {
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
                this.sendTextAreaActionPerformed(null);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2) {
            if (!this.getNickListModel().isEmpty()) {
                int index = this.nickList.locationToIndex(e.getPoint());
                String nick = this.getNickListModel().getElementAt(index).toString();
                this.nickList.ensureIndexIsVisible(index);
                this.sendTextArea.insert(nick, this.sendTextArea.getCaretPosition());
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        receiveTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        sendTextArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        nickList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client_Chat");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jScrollPane1.setBorder(null);

        receiveTextArea.setEditable(false);
        receiveTextArea.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        receiveTextArea.setLineWrap(true);
        receiveTextArea.setRows(5);
        receiveTextArea.setWrapStyleWord(true);
        receiveTextArea.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        receiveTextArea.setDoubleBuffered(true);
        jScrollPane1.setViewportView(receiveTextArea);

        jScrollPane2.setBorder(null);

        sendTextArea.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        sendTextArea.setLineWrap(true);
        sendTextArea.setWrapStyleWord(true);
        sendTextArea.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane2.setViewportView(sendTextArea);

        jScrollPane3.setBorder(null);

        nickList.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        nickList.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        nickList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(nickList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            this.getLogin().setVisible(true);
            if (this.getLogin().getLogged()) {
                this.setTitle("Client_Chat - " + this.getLogin().getUsername());
                this.getNicks();
                this.setGroupAddress(InetAddress.getByName(this.getGroupHost()));
                this.setGroupSocket(new MulticastSocket(this.getGroupPort()));
                this.getGroupSocket().joinGroup(this.getGroupAddress());
                this.getReceiveThread().start();
                String msg = this.getLogin().getUsername() + "#JOINGRP#";
                DatagramPacket dtg = new DatagramPacket(msg.getBytes(), msg.length(),
                    this.getGroupAddress(), this.getGroupPort());
                this.getGroupSocket().send(dtg);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(),
                "Aïe Aïe Aïe !", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            if (this.getLogin().getLogged()) {
                this.delNick(this.getLogin().getUsername());
                this.getReceiveThread().setEnabled(false);
                String msg = this.getLogin().getUsername() + "#LEAVEGRP#";
                DatagramPacket dtg = new DatagramPacket(msg.getBytes(), msg.length(),
                this.getGroupAddress(), this.getGroupPort());
                this.getGroupSocket().send(dtg);
                this.getGroupSocket().leaveGroup(this.getGroupAddress());
                this.getGroupSocket().close();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(),
                "Aïe Aïe Aïe !", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }//GEN-LAST:event_formWindowClosing

    private void sendTextAreaActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (this.getLogin().getLogged()) {
                if (this.sendTextArea.getText().trim().compareTo("") != 0) {
                    String msg = this.getLogin().getUsername() + "> " +
                        this.sendTextArea.getText();
                    DatagramPacket dtg = new DatagramPacket(msg.getBytes(), msg.length(),
                        this.getGroupAddress(), this.getGroupPort());
                    this.getGroupSocket().send(dtg);
                    this.sendTextArea.setText(null);
                    this.sendTextArea.setCaretPosition(0);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(),
                "Aïe Aïe Aïe !", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client_Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Client_Chat().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList nickList;
    private javax.swing.JTextArea receiveTextArea;
    private javax.swing.JTextArea sendTextArea;
    // End of variables declaration//GEN-END:variables
}
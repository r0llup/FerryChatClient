/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sh1fT
 */

public final class TXTLoader {
    protected String filename;
    protected BufferedReader bufferedReader;

    /**
     * Creates new instance TXTLoader
     * @param filename
     * @throws FileNotFoundException 
     */
    public TXTLoader(String filename) throws FileNotFoundException {
        this.setFilename(filename);
        this.setBufferedReader(null);
        this.loadFile();
    }

    public String getFilename() {
        return this.filename;
    }

    protected void setFilename(String filename) {
        this.filename = filename;
    }

    public BufferedReader getBufferedReader() {
        return this.bufferedReader;
    }

    protected void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    /**
     * Load The TXT File
     */
    protected void loadFile() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(this.getFilename()); 
        InputStreamReader isr = new InputStreamReader(fis);
        this.setBufferedReader(new BufferedReader(isr));
    }

    /**
     * Retrieve Data From The TXT File
     * @return List<String>
     */
    public List<String> retrieveData() {
        try {
            List<String> listeString = new LinkedList<>();
            String ligne;
            while ((ligne = this.getBufferedReader().readLine()) != null)
                listeString.add(ligne);
            return listeString;
        } catch (IOException ex) {
            System.out.println("Aïe Aïe Aïe !\n" + ex.getLocalizedMessage());
            System.exit(1);
        }
        return null;
    }

    @Override
    public void finalize() {
        try {
            this.getBufferedReader().close();
            super.finalize();
        } catch (Throwable ex) {
            System.out.println("Aïe Aïe Aïe !\n" + ex.getLocalizedMessage());
            System.exit(1);
        }
    }
}
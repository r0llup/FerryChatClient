/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 *
 * @author Sh1fT
 */

public final class TXTSaver {
    protected String filename;
    protected List<String> infos;

    public TXTSaver(String filename, List<String> infos) {
        this.setFilename(filename);
        this.setInfos(infos);
    }

    public String getFilename() {
        return this.filename;
    }

    protected void setFilename(String filename) {
        this.filename = filename;
    }

    public List<String> getInfos() {
        return this.infos;
    }

    protected void setInfos(List<String> infos) {
        this.infos = infos;
    }

    /**
     * Save The TXT File
     */
    public void saveFile(List<String> infos) {
        try {
            FileOutputStream fos = new FileOutputStream(this.getFilename()); 
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            for (String info : infos) {
                bw.write(info);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            System.out.println("Aïe Aïe Aïe !\n" + ex.getLocalizedMessage());
            System.exit(1);
        }
    }
}
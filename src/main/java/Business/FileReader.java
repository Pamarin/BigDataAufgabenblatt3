package Business;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-05-04
 */
public class FileReader {
    private String address;
    private ArrayList<String> lines;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }
    
    public FileReader(String address) {
        this.address = address;
    }
    
    public void read() throws FileNotFoundException, IOException {
        // Variable for each line in the document.
        String lineContent = "";
        // Re-initialize the array of lines.
        this.lines = new ArrayList<>();
        
        // Get file access.
        BufferedReader br = new BufferedReader(new java.io.FileReader(this.address));
        
        // Add all found lines to the array of lines.
        while((lineContent = br.readLine()) != null) {
            this.lines.add(lineContent);
        }
    }
}

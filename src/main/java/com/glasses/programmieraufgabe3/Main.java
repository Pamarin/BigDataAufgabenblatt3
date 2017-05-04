package com.glasses.programmieraufgabe3;

import Business.FileReader;
import java.io.IOException;



/**
 *
 * @author Jean-Luc Burot
 * @since 2017-05-04
 */
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Willkommen bei Programmieraufgabe 3!");
        
        FileReader reader = new FileReader("/home/jean/Schreibtisch/Big Data/Aufgabenblatt 3/assignment1-data/documents.json");
        reader.read(true);
        
        for(String line : reader.getLines()) {
            System.out.println(line);
        }
    }
}

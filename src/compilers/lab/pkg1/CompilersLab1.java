/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilers.lab.pkg1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Character.isDigit;
import java.util.ArrayList;

/**
 *
 * @author ahmedalsai
 */
public class CompilersLab1 {

    public static ArrayList<String> reserved = new ArrayList<String>();
    public static ArrayList<String> singleCharSpecial = new ArrayList<String>();
    public static ArrayList<String> multipleCharSpecialsStartChar = new ArrayList<String>();
    public static int CurrentCharASCII;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            init();
            File file = new File("/Users/ahmedalsai/Desktop/code.txt");
            FileReader fr;
            fr = new FileReader(file);
            readNext(fr);
            while ((CurrentCharASCII) != -1) {

                char currentChar = (char) CurrentCharASCII;
                if (currentChar == '{') {
                    takeComment(fr);
                } else if (singleCharSpecial.contains((char) CurrentCharASCII + "")) {
                    System.out.println("Special symbol: " + (char) CurrentCharASCII);
                    readNext(fr);
                }
                else if (multipleCharSpecialsStartChar.contains((char) CurrentCharASCII + "")){
                    seeIfMultipleCharSpecial((char)CurrentCharASCII, fr);
                }
                else if (Character.isLetter((char)CurrentCharASCII)){
                    seeIfReservedOrIdentifier(fr);
                }
                else if (Character.isDigit((char)CurrentCharASCII)){
                    getNumber(fr);
                }
                
                else {
                    readNext(fr);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public static void takeComment(FileReader fr) {
        try {
            String comment = "";
            char temp;
            while ((temp = (char) readNext(fr)) != '}') {
                comment += (char) temp;
            }
            System.out.println("Comment: " + "{" + comment + "}");
            readNext(fr);
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public static void getMultipleCharSpecial(char x, FileReader fr) throws IOException {
        switch (x) {
            case ':':
                char secondChar = (char) fr.read();
                if (secondChar == '=') {
                    System.out.println("Special: :=");
                }

        }
    }
    
    private static void init() {
        reserved.add("read");
        reserved.add("if");
        reserved.add("else");
        reserved.add("then");
        reserved.add("repeat");
        reserved.add("until");
        reserved.add("write");
        reserved.add("end");
        singleCharSpecial.add("+");
        singleCharSpecial.add("-");
        singleCharSpecial.add("*");
        singleCharSpecial.add("/");
        singleCharSpecial.add("(");
        singleCharSpecial.add(")");
        singleCharSpecial.add(";");
        singleCharSpecial.add("<");
        multipleCharSpecialsStartChar.add(":");

    }

    private static int readNext(FileReader fr) throws IOException {
        CurrentCharASCII = fr.read();
        return CurrentCharASCII;
    }

    private static void seeIfMultipleCharSpecial(char c, FileReader fr) throws IOException {
        if (c == ':'){
            readNext(fr);
            if ((char)CurrentCharASCII == '='){
                System.out.println("Special symbol: :=");
                readNext(fr);
            }
        }
    }

    private static void seeIfReservedOrIdentifier(FileReader fr) throws IOException {
        String tempStr = (char) CurrentCharASCII + "";
        char tempChar;
        while (Character.isLetter(tempChar = (char) readNext(fr))) {
            tempStr += tempChar;
        }
        if (reserved.contains(tempStr)) {
            System.out.println("Reserved symbol: " + tempStr);
        } else {
            System.out.println("Identifier: " + tempStr);

        }
    }
    
    private static void getNumber(FileReader fr) throws IOException{
        String tempStr = (char) CurrentCharASCII + "";
        char tempChar;
        while (Character.isDigit(tempChar = (char) readNext(fr))) {
            tempStr += tempChar;
        }
        System.out.println("Number: " + tempStr);
    }
}

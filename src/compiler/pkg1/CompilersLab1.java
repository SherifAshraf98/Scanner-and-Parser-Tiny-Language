/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.pkg1;

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
    public static State currentState = State.START;
    public static String rubbish = "";
    public static int currentLine = 1;
    enum State {
        START, INNUM, INID, INASSIGN, INCOMMENT, DONE
    }
    
    

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\Sherif Ashraf\\Dropbox\\Semester 6\\Compilers\\Scanner-and-Parser-Tiny-Language\\code.txt");
        FileReader fr = new FileReader(file);
        ArrayList<Token> x = Scanner.getTokens(fr);
        for (int i = 0; i < x.size(); i++){
            System.out.println(x.get(i).getValue());
        }
        Parser p = new Parser(x);
//    while((CurrentCharASCII) != -1){
//        CurrentCharASCII = fr.read();
//        System.out.println(CurrentCharASCII);
//    }

        
    }
}

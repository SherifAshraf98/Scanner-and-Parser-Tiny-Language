/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilers.lab.pkg1;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author ahmedalsai
 */
public class Scanner {
    public static ArrayList<String> reserved = new ArrayList<String>();
    public static ArrayList<String> singleCharSpecial = new ArrayList<String>();
    public static ArrayList<String> multipleCharSpecialsStartChar = new ArrayList<String>();
    public static int CurrentCharASCII;
    public static State currentState = State.START;
    public static String rubbish = "";
    public static int currentLine = 1;
    public static ArrayList<Token> tokenList;
    enum State {
        START, INNUM, INID, INASSIGN, INCOMMENT, DONE
    }
    class UnknownToken extends Exception{
        public UnknownToken (String str){
            super(str);
        }
    }
    

    public static ArrayList<Token> getTokens() {
        try {
            init();
            File file = new File("/Users/ahmedalsai/Desktop/code.txt");
            FileReader fr;
            fr = new FileReader(file);
            readNext(fr);
            while ((CurrentCharASCII) != -1) {

                char currentChar = (char) CurrentCharASCII;
                if (currentChar == '{') {
                    currentState = State.INCOMMENT;
                    //takeComment(fr);
                } else if (((char) CurrentCharASCII) == ':') {
                    currentState = State.INASSIGN;
                } else if (Character.isLetter((char) CurrentCharASCII)) {
                    currentState = State.INID;
                } else if (Character.isDigit((char) CurrentCharASCII)) {
                    currentState = State.INNUM;
                } 
                //Other symbols:
                else {
                    if (singleCharSpecial.contains((char) CurrentCharASCII + "")) {
                        tokenList.add(new Token((char) CurrentCharASCII + "", TokenType.SPECIALSYMBOL));
                        readNext(fr);
                    }
                    else{
                        
                        if ((char)CurrentCharASCII == ' ' || CurrentCharASCII == 10 ){
                            clearRubbish();
                        }
                        else {
                            addRubbish((char)CurrentCharASCII);
                        }
                        if (CurrentCharASCII == 10){
                            currentLine++;
                        }
                        readNext(fr);
                    }
                    
                }

                switch (currentState) {
                    case DONE: {
                        currentState = State.START;
                        break;
                    }
                    case INCOMMENT: {
                        String comment = "";
                        char temp;
                        while ((temp = (char) readNext(fr)) != '}') {
                            comment += (char) temp;
                        }
                        clearRubbish();
                        readNext(fr);
                        currentState = State.DONE;
                      
                        break;
                    }
                    case INASSIGN: {
                        readNext(fr);
                        if ((char) CurrentCharASCII == '=') {
                            clearRubbish();
                            tokenList.add(new Token(":=", TokenType.SPECIALSYMBOL));
                            readNext(fr);
                        } else{
                            addRubbish(':');
                        }
                        currentState = State.DONE;
                        break;
                    }
                    case INID: {
                        String tempStr = (char) CurrentCharASCII + "";
                        char tempChar;
                        while (Character.isLetter(tempChar = (char) readNext(fr))) {
                            tempStr += tempChar;
                        }
                        if (tempStr != ""){
                            if (reserved.contains(tempStr)) {
                                clearRubbish();
                                tokenList.add(new Token(tempStr, TokenType.RESERVED));
                            } else {
                                clearRubbish();
                                tokenList.add(new Token(tempStr, TokenType.IDENTIFIER));
                            }
                        }
                        currentState = State.DONE;
                        break;
                    }
                    case INNUM: {
                        String tempStr = (char) CurrentCharASCII + "";
                        char tempChar;
                        while (Character.isDigit(tempChar = (char) readNext(fr))) {
                            tempStr += tempChar;
                        }
                        clearRubbish();
                        tokenList.add(new Token(tempStr, TokenType.NUMBER));
                        currentState = State.DONE;
                        break;
                    }
                    case START: {
                        if((char)CurrentCharASCII == ' '){
                            while (((char) readNext(fr)) == ' ') {
                            continue;
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return tokenList;
    }
    
    public static void addRubbish(char c){
        if ((int)c != 10){
        rubbish += c;
        }
    }
    
    public static void clearRubbish(){
        if (rubbish != ""){
            throw new RuntimeException("Unknow token in line " + currentLine + ", near: " + rubbish);
        }
        rubbish = "";
    }

    private static void init() {
        tokenList.clear();
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
}

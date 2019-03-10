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
                } else if (!isDigit(currentChar)) {
                    String strIfExists = "";
                    while (true) {
                        String tempStr = seeIfReserved(currentChar, fr);
                        System.out.println(tempStr);
                        if (!tempStr.equals("") && !Character.isLetter(tempStr.charAt(0))) {
                            //Add identifier token
                            if (strIfExists.length() != 0) {
                                System.out.println("Identifier: " + strIfExists);
                            }
                            //Call ,multiple character symbol function
                            seeIfMultipleCharSymbol(tempStr);
                            break;
                        } else if (!tempStr.equals("") && Character.isLetter(tempStr.charAt(0))) {
                            //keep going because is is a letter
                            strIfExists += tempStr;
                            continue;
                        } else {
                            break;
                        }
                    }
                } else if (isDigit(currentChar)) {
                    seeIfvalidDigit(currentChar, fr);
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

    public static String seeIfReserved(char x, FileReader fr) throws IOException {
        String tempStr = "";
        if (x == 'i') {
            tempStr += "i";
            char y = (char) readNext(fr);
            if (y == 'f') {
                System.out.println("Reserved word: if");
                readNext(fr);
                return "";
            }
        } else if (x == 'r') {
            tempStr += "r";
            if ((char) readNext(fr) == 'e') {
                tempStr += "e";
                char temp = (char) readNext(fr);
                if (temp == 'a') {
                    tempStr += "a";
                    if ((char) readNext(fr) == 'd') {
                        System.out.println("Reserved word: read");
                        readNext(fr);
                        return "";
                    }
                } else if (temp == 'p') {
                    tempStr += "p";
                    if ((char) readNext(fr) == 'e') {
                        tempStr += "e";
                        if ((char) readNext(fr) == 'a') {
                            tempStr += "a";
                            if ((char) readNext(fr) == 't') {
                                System.out.println("Reserved word: repeat");
                                readNext(fr);
                                return "";
                            }
                        }
                    }
                }
            }
        } else if (x == 'e') {
            tempStr += "e";
            char temp = (char) readNext(fr);
            if (temp == 'l') {
                tempStr += "l";
                if ((char) readNext(fr) == 's') {
                    tempStr += "s";
                    if ((char) readNext(fr) == 'e') {
                        System.out.println("Reserved word: else");
                        readNext(fr);
                        return "";
                    }
                }
            } else if (temp == 'n') {
                tempStr += "n";
                if ((char) readNext(fr) == 'd') {
                    System.out.println("Reserved word: end");
                    readNext(fr);
                    return "";
                }
            }
        } else if (x == 't') {
            tempStr += "t";
            if ((char) readNext(fr) == 'h') {
                tempStr += "h";
                if ((char) readNext(fr) == 'e') {
                    tempStr += "e";
                    if ((char) readNext(fr) == 'n') {
                        System.out.println("Reserved word: then");
                        readNext(fr);
                        return "";
                    }
                }
            }
        } else if (x == 'u') {
            tempStr += "u";
            if ((char) readNext(fr) == 'n') {
                tempStr += "n";
                if ((char) readNext(fr) == 't') {
                    tempStr += "t";
                    if ((char) readNext(fr) == 'i') {
                        tempStr += "i";
                        if ((char) readNext(fr) == 'l') {

                            System.out.println("Reserved word: until");
                            readNext(fr);
                            return "";
                        }
                    }
                }
            }
        } else if (x == 'w') {
            tempStr += "w";
            if ((char) readNext(fr) == 'r') {
                tempStr += "r";
                if ((char) readNext(fr) == 'i') {
                    tempStr += "i";
                    if ((char) readNext(fr) == 't') {
                        tempStr += "t";
                        if ((char) readNext(fr) == 'e') {

                            System.out.println("Reserved word: write");
                            readNext(fr);
                            return "";

                        }
                    }
                }
            }
        } else {
            System.out.println("WILL read next");
            readNext(fr);
            System.out.println("Current char: " + (char) CurrentCharASCII);
            return x + "";
        }
        return tempStr;

    }

    public static void seeIfvalidDigit(char x, FileReader fr) throws IOException {

        char temp;
        String number = x + "";
        while (true) {
            temp = (char) readNext(fr);
            if (isDigit(temp)) {
                number += temp;
            } else {
                break;
            }
        }
        System.out.println("Number: " + number);
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
        singleCharSpecial.add(":=");
        singleCharSpecial.add(";");
        singleCharSpecial.add("<");

    }

    private static int readNext(FileReader fr) throws IOException {
        CurrentCharASCII = fr.read();
        return CurrentCharASCII;
    }

    private static void seeIfMultipleCharSymbol(String tempStr) {
    }
}

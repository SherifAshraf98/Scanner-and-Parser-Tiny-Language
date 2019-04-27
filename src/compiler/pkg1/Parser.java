/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.pkg1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ahmedalsai
 */
public class Parser {

    public ArrayList<Token> tokenList;
    public ArrayList<NodeTree> nodeList = new ArrayList<NodeTree>();
    public ArrayList<String> foundStmts = new ArrayList<String>();

    public int counter = 0;
    public int readCount = 0;
    public int writeCount = 0;
    public int assignCount = 0;
    public int ifCount = 0;
    public int repeatCount = 0;
    public boolean errorExists = false;
    public BufferedWriter bw;

    public Parser(ArrayList<Token> x, BufferedWriter bw) throws IOException {
        tokenList = x;
        this.bw = bw;
        program();
        printFoundStmts();
    }

    public void increaseCounter() {
        if (!isDone() && !tokenList.get(counter).getValue().equals(";") && !tokenList.get(counter).getValue().equals("end") && !tokenList.get(counter).getValue().equals("until")) {
            counter++;
        }
    }

    public boolean match(String s1, String s2) {
        if (s1.equalsIgnoreCase(s2)) {
            counter++;
            return true;
        }
        return false;
    }

    public boolean match(TokenType x, TokenType y) {
        if (x == y) {
            counter++;
            return true;
        }
        return false;
    }

    public boolean isDone() {
        if (counter == tokenList.size()) {
            return true;
        }
        return false;
    }

    public void program() {
        foundStmts.add("program is found");
        stmt_seq();
        System.out.println("Read count: " + readCount);
        System.out.println("Write count: " + writeCount);
        System.out.println("Assignment count: " + assignCount);
        System.out.println("If count: " + ifCount);
        System.out.println("Repeat count: " + repeatCount);
        System.out.println("Error: " + errorExists);
        //ParseTree pt = new ParseTree(nodeList);
        //pt.draw();
    }

    public void stmt_seq() {

//        statement();
//        for (int i = 0; i < tokenList.size(); i++){
//            if (!isDone() && !tokenList.get(counter).getValue().equals("end") && !tokenList.get(counter).getValue().equals("until")){
//                if (!match(tokenList.get(counter).getValue(), ";")){
//                    System.out.println("Missing semi-colon");
//                }
//                statement();
//            }
//        }


        foundStmts.add("stmt_sequence is found");
        statement();

        while (!isDone() && !tokenList.get(counter).getValue().equals("end") && !tokenList.get(counter).getValue().equals("until")) {
            if (match(tokenList.get(counter).getValue(), ";")) {
                foundStmts.add(";");
                statement();

            } else {
                foundStmts.add("Missing semi-colon --> in stmt_seq");
                errorExists = true;
                statement();
            }

        }
    }

    public boolean statement() {
        if (!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase("read")) {
            foundStmts.add("statement is found");
            return read_stmt();
        } else if (!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase("write")) {
            foundStmts.add("statement is found");
            return write_stmt();
        } else if (!isDone() && tokenList.get(counter).getType() == TokenType.IDENTIFIER) {
            foundStmts.add("statement is found");
            return assign_stmt();
        } else if (!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase("if")) {
            foundStmts.add("statement is found");
            return if_stmt();
        } else if (!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase("repeat")) {
            foundStmts.add("statement is found");
            return repeat_stmt();
        }
        if (!isDone()){
            System.out.println("Unexpected token:" + tokenList.get(counter).getValue());
            counter++;
        }
        foundStmts.add("Unexpected token:" + tokenList.get(counter).getValue() + " --> in stmt");
        errorExists = true;
        counter++;
        return false;
    }
//    public boolean isOnStatementStarter(){
//        if (!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase("read")) {
//            return true;
//        } else if (!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase("write")) {
//            return true;
//        } else if (!isDone() && tokenList.get(counter).getType() == TokenType.IDENTIFIER) {
//            return true;
//        } else if (!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase("if")) {
//            return true;
//        } else if (!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase("repeat")) {
//            return true;
//        }
//        return false;
//    }

    public boolean assign_stmt() {
        foundStmts.add("assign-stmt is found");
        if (!isDone() && match(tokenList.get(counter).getType(), TokenType.IDENTIFIER)) {
            foundStmts.add("identifier");
            if (!isDone() && match(tokenList.get(counter).getValue(), ":=")) {
                foundStmts.add(":=");
                if (exp()) {
                    assignCount++;
                    return true;
                }
            } else {
                foundStmts.add("Syntax error: Expected assignment symbol --> in assign_stmt");
                errorExists = true;
                increaseCounter();
            }
        } else {
            foundStmts.add("Syntax error: Expected identifier --> in assign_stmt");
            errorExists = true;
            if (!tokenList.get(counter).getValue().equals(";") && !tokenList.get(counter).getValue().equals("end")) {
                increaseCounter();
            }

        }
        return false;
    }

    public boolean read_stmt() {
        foundStmts.add("read-stmt is found");
        if (!isDone() && match(tokenList.get(counter).getValue(), "read")) {
            foundStmts.add("read is found");
            if (!isDone() && match(tokenList.get(counter).getType(), TokenType.IDENTIFIER)) {
                foundStmts.add("identifier");
                readCount++;
                return true;
            } else {
                foundStmts.add("Syntax error: Expected identifier --> in read_stmt");
                errorExists = true;
                increaseCounter();
            }
        } else {
            foundStmts.add("Syntax error: Expected read --> in read_stmt");
            errorExists = true;
            increaseCounter();
        }
        return false;
    }

    public boolean write_stmt() {
        foundStmts.add("write-stmt is found");
        if (!isDone() && match(tokenList.get(counter).getValue(), "write")) {
            foundStmts.add("write is found");

            if (exp()) {
                writeCount++;
                return true;
            } else {
                foundStmts.add("Syntax error: expecting an expression after write statement --> in write_stmt");
                errorExists = true;
                increaseCounter();
            }
        } else {
            foundStmts.add("Syntax error: Expected write --> in write_stmt");
            errorExists = true;
            increaseCounter();
        }
        return false;
    }

    public boolean addop() {
        if (!isDone() && match(tokenList.get(counter).getValue(), "+")) {
            foundStmts.add("addop is found");
            return true;
        } else if (!isDone() && match(tokenList.get(counter).getValue(), "-")) {
            foundStmts.add("addop is found");
            return true;
        } else {
            return false;
        }

    }

    public boolean mulop() {
        if (!isDone() && match(tokenList.get(counter).getValue(), "*")) {
            foundStmts.add("mulop is found");
            return true;
        } else if (!isDone() && match(tokenList.get(counter).getValue(), "/")) {
            return true;
        } else {
            return false;
        }

    }

    public boolean comparisonop() {
        if (!isDone() && match(tokenList.get(counter).getValue(), "<")) {
            foundStmts.add("comparison-op is found");
            return true;
        } else if (!isDone() && match(tokenList.get(counter).getValue(), "=")) {
            foundStmts.add("comparison-op is found");
            return true;
        } else {
            return false;
        }

    }

    public boolean exp() {
        foundStmts.add("exp is found");
        boolean temp;
        temp = simple_exp();
        if (temp) {
            if (comparisonop()) {
                temp = simple_exp();
                if (!temp) {
                    foundStmts.add("Syntax error: expecting an expression after comparison operation --> in exp");
                    errorExists = true;
                    increaseCounter();
                }
            }
        }
        return temp;
    }

    public boolean simple_exp() {
        foundStmts.add("simple-exp is found");
        boolean temp;
        temp = term();
        if (temp) {
            while (addop()) {
                temp = term();
                if (!temp) {
                    foundStmts.add("Syntax error: expecting a term after add operation --> in simple_exp");
                    errorExists = true;
                    increaseCounter();
                }
            }
        }
        return temp;
    }

    public boolean term() {
        foundStmts.add("term is found");
        boolean temp;
        temp = factor();
        if (temp) {
            while (mulop()) {
                temp = factor();
                if (!temp) {
                    foundStmts.add("Syntax error: expecting a factor after mul operation --> in term");
                    errorExists = true;
                    increaseCounter();
                }
            }
        }
        return temp;
    }

    public boolean factor() {
        foundStmts.add("factor is found");
        boolean temp;
        if (!isDone() && match(tokenList.get(counter).getValue(), "(")) {
            foundStmts.add("(");
            if (exp() && !isDone() && match(tokenList.get(counter).getValue(), ")")) {
                foundStmts.add(")");
                return true;
            } else {
                foundStmts.add("Syntax error: Expecting expression in balanced parenthesis --> factor");
                errorExists = true;
                increaseCounter();
            }
        } else if (!isDone() && match(tokenList.get(counter).getType(), TokenType.NUMBER)) {
            foundStmts.add("number");
            return true;
        } else if (!isDone() && match(tokenList.get(counter).getType(), TokenType.IDENTIFIER)) {
            foundStmts.add("identifier");
            return true;
        } else {
            foundStmts.add("Syntax error: Expecting ID/Number/Parenthesis --> in factor");
            errorExists = true;
            increaseCounter();
            return false;
        }
        return false;
    }

    public boolean if_stmt() {
        if (!isDone() && match(tokenList.get(counter).getValue(), "if")) {
            foundStmts.add("if-stmt is found");
            foundStmts.add("if");
            if (exp()) {
                if (!isDone() && match(tokenList.get(counter).getValue(), "then")) {
                    foundStmts.add("then");
                    stmt_seq();
                    if (!isDone() && match(tokenList.get(counter).getValue(), "else")) {
                        foundStmts.add("else");
                        stmt_seq();
                        if (match(tokenList.get(counter).getValue(), "end")) {
                            foundStmts.add("end");
                            ifCount++;
                            return true;
                        } else {
                            foundStmts.add("Syntax error: missing \"end\" keyword --> in if_stmt");
                            errorExists = true;
                            increaseCounter();
                        }
                    }
                    if (!isDone() && match(tokenList.get(counter).getValue(), "end")) {
                        ifCount++;
                        return true;
                    } else {
                        foundStmts.add("Syntax error: missing \"end\" keyword --> if_stmt");
                        errorExists = true;
                        increaseCounter();
                    }
                } else {
                    foundStmts.add("Syntax error: missing \"then\" keyword --> if_stmt");
                    errorExists = true;
                    increaseCounter();
                }
            } else {
                foundStmts.add("Syntax error: missing if's condition --> if_stmt");
                errorExists = true;
                increaseCounter();
            }
        }
        return false;
    }

    public boolean repeat_stmt() {
        if (match(tokenList.get(counter).getValue(), "repeat")) {
            foundStmts.add("repeat-stmt is found");
            foundStmts.add("repeat");
            stmt_seq();
            if (!isDone() && match(tokenList.get(counter).getValue(), "until")) {
                foundStmts.add("until");
                if (exp()) {
                    repeatCount++;
                    return true;
                } else {
                    foundStmts.add("Syntax error: missing repeat ending statement --> in repeat_stmt");
                    errorExists = true;
                    increaseCounter();
                }
            } else {
                foundStmts.add("Syntax error: missing until statement --> in repeat_stmt");
                errorExists = true;
                increaseCounter();
            }

        }
        return false;
    }

    public void printFoundStmts() throws IOException {
        for (int i = 0; i < foundStmts.size(); i++) {
            bw.write(foundStmts.get(i));
            if ((i + 1) != foundStmts.size()) {
                bw.newLine();
            }
        }
        bw.close();
    }
}

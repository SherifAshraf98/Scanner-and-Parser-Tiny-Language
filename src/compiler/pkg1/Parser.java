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
    public ArrayList<String> foundStmts = new ArrayList<String>();

    public int counter = 0;
    public int readCount = 0;
    public int writeCount = 0;
    public int assignCount = 0;
    public int ifCount = 0;
    public int repeatCount = 0;
    public boolean errorExists = false;

    public BufferedWriter bw;
    MyNode syntaxTree;

    public Parser(ArrayList<Token> x, BufferedWriter bw) throws IOException {
        tokenList = x;
        this.bw = bw;
        syntaxTree = program();
        printFoundStmts();
    }

    public MyNode program() {
        foundStmts.add("program is found");
        MyNode n = stmt_seq();
        System.out.println("Read count: " + readCount);
        System.out.println("Write count: " + writeCount);
        System.out.println("Assignment count: " + assignCount);
        System.out.println("If count: " + ifCount);
        System.out.println("Repeat count: " + repeatCount);
        System.out.println("Error: " + errorExists);
        return returnTree(n);
    }

    public MyNode stmt_seq() {

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
        MyNode n = statement();

        MyNode n2 = n;

        while (!isDone() && !tokenList.get(counter).getValue().equals("end") && !tokenList.get(counter).getValue().equals("until") && !tokenList.get(counter).getValue().equals("else")) {
            if (match(tokenList.get(counter).getValue(), ";")) {
                foundStmts.add(";");
                MyNode temp = statement();
                if (n2 != null) {
                    n2.sibling = temp;
                    n2 = temp;
                }

            } else {
                if (n2 != null) {
                    foundStmts.add("Missing semi-colon --> in stmt_seq");
                }
                errorExists = true;
                MyNode temp = statement();
                if (n2 != null) {
                    n2.sibling = temp;
                    n2 = temp;
                }
            }

        }
        return n;
    }

    public MyNode statement() {
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
        if (!isDone()) {
            foundStmts.add("Unexpected token:" + tokenList.get(counter).getValue() + " --> in stmt");
            counter++;
        }

        return null;
    }

    public MyNode assign_stmt() {
        foundStmts.add("assign-stmt is found");
        MyNode tempNode = null;
        MyNode assignNode = new MyNode("assign ");
        if (!isDone() && match(tokenList.get(counter).getType(), TokenType.IDENTIFIER)) {
            foundStmts.add("identifier");
            assignNode.data += "(" + tokenList.get(counter - 1).getValue() + ")";
            if (!isDone() && match(tokenList.get(counter).getValue(), ":=")) {
                foundStmts.add(":=");
                tempNode = exp();
                if (tempNode != null) {
                    assignCount++;
                    assignNode.children.add(tempNode);
                    return assignNode;
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
        return null;
    }

    public MyNode read_stmt() {
        foundStmts.add("read-stmt is found");
        if (!isDone() && match(tokenList.get(counter).getValue(), "read")) {
            foundStmts.add("read is found");
            if (!isDone() && match(tokenList.get(counter).getType(), TokenType.IDENTIFIER)) {
                foundStmts.add("identifier");
                readCount++;
                MyNode n = new MyNode("read (" + tokenList.get(counter - 1).getValue() + ")");
                return n;
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
        return null;
    }

    public MyNode write_stmt() {
        foundStmts.add("write-stmt is found");
        MyNode tempNode = null;
        MyNode writeNode = new MyNode("write");
        if (!isDone() && match(tokenList.get(counter).getValue(), "write")) {
            foundStmts.add("write is found");
            tempNode = exp();
            if (tempNode != null) {
                writeCount++;
                writeNode.children.add(tempNode);
                return writeNode;
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
        return null;
    }

    public MyNode addop() {
        MyNode returnNode = null;
        if (!isDone() && match(tokenList.get(counter).getValue(), "+")) {
            foundStmts.add("addop is found");
            returnNode = new MyNode("op (+)");
            return returnNode;
        } else if (!isDone() && match(tokenList.get(counter).getValue(), "-")) {
            foundStmts.add("addop is found");
            returnNode = new MyNode("op (-)");
            return returnNode;
        } else {
            return null;
        }

    }

    public MyNode mulop() {
        MyNode returnNode = null;
        if (!isDone() && match(tokenList.get(counter).getValue(), "*")) {
            foundStmts.add("mulop is found");
            returnNode = new MyNode("op (*)");
            return returnNode;
        } else if (!isDone() && match(tokenList.get(counter).getValue(), "/")) {
            returnNode = new MyNode("op (/)");
            return returnNode;
        } else {
            return null;
        }

    }

    public MyNode comparisonop() {
        MyNode returnNode = null;
        if (!isDone() && match(tokenList.get(counter).getValue(), "<")) {
            foundStmts.add("comparison-op is found");
            returnNode = new MyNode("op (<)");
            return returnNode;
        } else if (!isDone() && match(tokenList.get(counter).getValue(), "=")) {
            foundStmts.add("comparison-op is found");
            returnNode = new MyNode("op (=)");
            return returnNode;
        } else {
            return null;
        }

    }

    public MyNode exp() {
        foundStmts.add("exp is found");
        boolean temp;
        MyNode n1;
        MyNode n2;
        MyNode n3;
        n1 = simple_exp();
        if (n1 != null) {
            MyNode opNode = comparisonop();
            if (opNode != null) {
                n2 = n1;
                n1 = opNode;
                n3 = simple_exp();
                n1.children.add(n2);
                n1.children.add(n3);
                if (n3 == null) {
                    foundStmts.add("Syntax error: expecting an expression after comparison operation --> in exp");
                    errorExists = true;
                    increaseCounter();
                }
            }
        }
        return n1;
    }

    public MyNode simple_exp() {
        foundStmts.add("simple-exp is found");
        boolean temp;
        MyNode n1;
        MyNode n2;
        MyNode n3;
        n1 = term();
        MyNode opNode = null;
        if (n1 != null) {
            while ((opNode = addop()) != null) {
                n2 = n1;
                n1 = opNode;
                n3 = term();
                n1.children.add(n2);
                n1.children.add(n3);
                if (n3 == null) {
                    foundStmts.add("Syntax error: expecting a term after add operation --> in simple_exp");
                    errorExists = true;
                    increaseCounter();
                }
            }
        }
        return n1;
    }

    public MyNode term() {
        foundStmts.add("term is found");
        boolean temp;
        MyNode n1;
        MyNode n2;
        MyNode n3;
        n1 = factor();
        MyNode opNode = null;
        if (n1 != null) {
            while ((opNode = mulop()) != null) {
                n2 = n1;
                n1 = opNode;
                n3 = factor();
                n1.children.add(n2);
                n1.children.add(n3);
                if (n3 == null) {
                    foundStmts.add("Syntax error: expecting a factor after mul operation --> in term");
                    errorExists = true;
                    increaseCounter();
                }
            }
        }
        return n1;
    }

    public MyNode factor() {
        foundStmts.add("factor is found");
        MyNode returnNode = null;
        boolean temp;
        if (!isDone() && match(tokenList.get(counter).getValue(), "(")) {
            foundStmts.add("(");
            returnNode = exp();
            if (returnNode != null && !isDone() && match(tokenList.get(counter).getValue(), ")")) {
                foundStmts.add(")");
                return returnNode;
            } else {
                foundStmts.add("Syntax error: Expecting expression in balanced parenthesis --> factor");
                errorExists = true;
                increaseCounter();
            }
        } else if (!isDone() && match(tokenList.get(counter).getType(), TokenType.NUMBER)) {
            foundStmts.add("number");
            returnNode = new MyNode(tokenList.get(counter - 1).getValue());
            return returnNode;
        } else if (!isDone() && match(tokenList.get(counter).getType(), TokenType.IDENTIFIER)) {
            returnNode = new MyNode(tokenList.get(counter - 1).getValue());
            return returnNode;
        } else {
            foundStmts.add("Syntax error: Expecting ID/Number/Parenthesis --> in factor");
            errorExists = true;
            increaseCounter();
            return null;
        }
        return null;
    }

    public MyNode if_stmt() {
        MyNode ifNode = new MyNode("if");
        MyNode testNode = null;
        MyNode thenNode = null;
        MyNode elseNode = null;
        if (!isDone() && match(tokenList.get(counter).getValue(), "if")) {
            foundStmts.add("if-stmt is found");
            foundStmts.add("if");
            testNode = exp();
            if (testNode != null) {
                ifNode.children.add(testNode);
                if (!isDone() && match(tokenList.get(counter).getValue(), "then")) {
                    foundStmts.add("then");
                    thenNode = stmt_seq();
                    ifNode.children.add(thenNode);
                    if (!isDone() && match(tokenList.get(counter).getValue(), "else")) {
                        foundStmts.add("else");
                        elseNode = stmt_seq();
                        ifNode.children.add(elseNode);
                        if (match(tokenList.get(counter).getValue(), "end")) {
                            foundStmts.add("end");
                            ifCount++;
                            return ifNode;
                        } else {
                            foundStmts.add("Syntax error: missing \"end\" keyword --> in if_stmt");
                            errorExists = true;
                            increaseCounter();
                        }
                    }
                    if (!isDone() && match(tokenList.get(counter).getValue(), "end")) {
                        ifCount++;
                        return ifNode;
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
        return null;
    }

    public MyNode repeat_stmt() {
        MyNode repeatNode = new MyNode("repeat");
        MyNode bodyNode = null;
        MyNode testNode = null;
        if (match(tokenList.get(counter).getValue(), "repeat")) {
            foundStmts.add("repeat-stmt is found");
            foundStmts.add("repeat");
            bodyNode = stmt_seq();
            repeatNode.children.add(bodyNode);
            if (!isDone() && match(tokenList.get(counter).getValue(), "until")) {
                foundStmts.add("until");
                testNode = exp();
                if (testNode != null) {
                    repeatNode.children.add(testNode);
                    repeatCount++;
                    return repeatNode;
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
        return null;
    }

    public MyNode getSyntaxTree() {
        return syntaxTree;
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

    public void printFoundStmts() throws IOException {
        for (int i = 0; i < foundStmts.size(); i++) {
            bw.write(foundStmts.get(i));
            if ((i + 1) != foundStmts.size()) {
                bw.newLine();
            }
        }
        bw.close();
    }

    public MyNode returnTree(MyNode n) {
        if (!errorExists) {
            return n;
        }
        return null;
    }
}

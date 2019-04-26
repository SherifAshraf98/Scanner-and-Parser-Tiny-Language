/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.pkg1;

import java.util.ArrayList;

/**
 *
 * @author ahmedalsai
 */
public class Parser {

    public ArrayList<Token> tokenList;
    public ArrayList<NodeTree> nodeList;
    public int counter = 0;
    public int readCount = 0;
    public int writeCount = 0;
    public int assignCount = 0;
    public int ifCount = 0;
    public int repeatCount = 0;

    public Parser(ArrayList<Token> x) {
        tokenList = x;
        program();
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
        stmt_seq();
        System.out.println("Read count: " + readCount);
        System.out.println("Write count: " + writeCount);
        System.out.println("Assignment count: " + assignCount);
        System.out.println("If count: " + ifCount);
        System.out.println("Repeat count: " + repeatCount);
        //ParseTree pt = new ParseTree(nodeList);
        //pt.draw();
    }

    public void stmt_seq() {

        statement();

        while (!isDone() && !tokenList.get(counter).getValue().equals("end") && !tokenList.get(counter).getValue().equals("until")) {
            if (match(tokenList.get(counter).getValue(), ";")) {
                statement();

            } else {
                System.out.println("Missing semi-colon");
                statement();
            }

        }

//        statement();
//        do{
//            match(tokenList.get(counter).getValue(), ";");
//            statement();
//        }
//        while(!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase(";"));
//        temp = statement();
//        for (int i = counter; i < tokenList.size(); i++) {
//            if (!isDone() && match(tokenList.get(i).getValue(), ";")){
//                temp = statement();
//            }
//            
//        }
//        return temp;
    }

    public boolean statement() {
        if (!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase("read")) {
            return read_stmt();
        } else if (!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase("write")) {
            return write_stmt();
        } else if (!isDone() && tokenList.get(counter).getType() == TokenType.IDENTIFIER) {
            return assign_stmt();
        } else if (!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase("if")) {
            return if_stmt();
        } else if (!isDone() && tokenList.get(counter).getValue().equalsIgnoreCase("repeat")) {
            return repeat_stmt();
        }
        System.out.println("Unexpected token:" + tokenList.get(counter).getValue());
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
        if (!isDone() && match(tokenList.get(counter).getType(), TokenType.IDENTIFIER)) {
            if (!isDone() && match(tokenList.get(counter).getValue(), ":=")) {
                if (exp()) {
                    assignCount++;
                    return true;
                }
            } else {
                System.out.println("Syntax error: Expected assignment symbol");
                increaseCounter();
            }
        } else {
            System.out.println("Syntax error: Expected identifier");
            if (!tokenList.get(counter).getValue().equals(";") && !tokenList.get(counter).getValue().equals("end")) {
                increaseCounter();
            }

        }
        return false;
    }

    public boolean read_stmt() {
        if (!isDone() && match(tokenList.get(counter).getValue(), "read")) {
            if (!isDone() && match(tokenList.get(counter).getType(), TokenType.IDENTIFIER)) {
                readCount++;
                return true;
            } else {
                System.out.println("Syntax error: Expected identifier");
                increaseCounter();
            }
        } else {
            System.out.println("Syntax error: Expected read");
            increaseCounter();
        }
        return false;
    }

    public boolean write_stmt() {
        if (!isDone() && match(tokenList.get(counter).getValue(), "write")) {
            if (exp()) {
                writeCount++;
                return true;
            } else {
                System.out.println("Syntax error: expecting an expression after write statement");
                increaseCounter();
            }
        } else {
            System.out.println("Syntax error: Expected write");
            increaseCounter();
        }
        return false;
    }

    public boolean addop() {
        if (!isDone() && match(tokenList.get(counter).getValue(), "+")) {
            return true;
        } else if (!isDone() && match(tokenList.get(counter).getValue(), "-")) {
            return true;
        } else {
            return false;
        }

    }

    public boolean mulop() {
        if (!isDone() && match(tokenList.get(counter).getValue(), "*")) {
            return true;
        } else if (!isDone() && match(tokenList.get(counter).getValue(), "/")) {
            return true;
        } else {
            return false;
        }

    }

    public boolean comparisonop() {
        if (!isDone() && match(tokenList.get(counter).getValue(), "<")) {
            return true;
        } else if (!isDone() && match(tokenList.get(counter).getValue(), "=")) {
            return true;
        } else {
            return false;
        }

    }

    public boolean exp() {
        boolean temp;
        temp = simple_exp();
        if (temp) {
            if (comparisonop()) {
                temp = simple_exp();
                if (!temp) {
                    System.out.println("Syntax error: expecting an expression after comparison operation");
                    increaseCounter();
                }
            }
        }
        return temp;
    }

    public boolean simple_exp() {
        boolean temp;
        temp = term();
        if (temp) {
            while (addop()) {
                temp = term();
                if (!temp) {
                    System.out.println("Syntax error: expecting a term after add operation");
                    increaseCounter();
                }
            }
        }
        return temp;
    }

    public boolean term() {
        boolean temp;
        temp = factor();
        if (temp) {
            while (mulop()) {
                temp = factor();
                if (!temp) {
                    System.out.println("Syntax error: expecting a factor after mul operation");
                    increaseCounter();
                }
            }
        }
        return temp;
    }

    public boolean factor() {
        boolean temp;
        if (!isDone() && match(tokenList.get(counter).getValue(), "(")) {
            if (exp() && !isDone() && match(tokenList.get(counter).getValue(), ")")) {
                return true;
            } else {
                System.out.println("Syntax error: Expecting expression in balanced parenthesis");
                increaseCounter();
            }
        } else if (!isDone() && match(tokenList.get(counter).getType(), TokenType.NUMBER)) {
            return true;
        } else if (!isDone() && match(tokenList.get(counter).getType(), TokenType.IDENTIFIER)) {
            return true;
        } else {
            System.out.println("Syntax error: Expecting ID/Number/Parenthesis");
            increaseCounter();
            return false;
        }
        return false;
    }

    public boolean if_stmt() {
        if (!isDone() && match(tokenList.get(counter).getValue(), "if")) {
            if (exp()) {
                if (!isDone() && match(tokenList.get(counter).getValue(), "then")) {
                    stmt_seq();

                    if (!isDone() && match(tokenList.get(counter).getValue(), "else")) {
                        stmt_seq();
                        if (match(tokenList.get(counter).getValue(), "end")) {
                            ifCount++;
                            return true;
                        } else {
                            System.out.println("Syntax error: missing \"end\" keyword");
                            increaseCounter();
                        }

                    }

                    if (!isDone() && match(tokenList.get(counter).getValue(), "end")) {
                        ifCount++;
                        return true;
                    } else {
                        System.out.println("Syntax error: missing \"end\" keyword");
                        increaseCounter();
                    }

                } else {
                    System.out.println("Syntax error: missing \"then\" keyword");
                    increaseCounter();
                }
            } else {
                System.out.println("Syntax error: missing if's condition");
                increaseCounter();
            }

        }
        return false;
    }

    public boolean repeat_stmt() {
        if (match(tokenList.get(counter).getValue(), "repeat")) {
            stmt_seq();
            if (!isDone() && match(tokenList.get(counter).getValue(), "until")) {
                if (exp()) {
                    repeatCount++;
                    return true;
                } else {
                    System.out.println("Syntax error: missing repeat ending statement");
                    increaseCounter();
                }
            } else {
                System.out.println("Syntax error: missing until statement");
                increaseCounter();
            }

        }
        return false;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilers.lab.pkg1;

enum TokenType{
    COMMENT, SPECIALSYMBOL, NUMBER, IDENTIFIER, RESERVED
}
public class Token {
    private String value;
    private TokenType type;

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }
    
}

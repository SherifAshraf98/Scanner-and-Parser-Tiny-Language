/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.pkg1;

import java.util.ArrayList;

/**
 *
 * @author Sherif Ashraf
 */
public class NodeTree {
    private String value;
    public ArrayList<NodeTree> childNode = new ArrayList<>();

    public NodeTree(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    
    
    
}
